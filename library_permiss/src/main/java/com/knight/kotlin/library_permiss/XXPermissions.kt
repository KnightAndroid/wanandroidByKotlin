package com.knight.kotlin.library_permiss

import android.app.Activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

import com.knight.kotlin.library_permiss.AndroidVersion.isAndroid13
import com.knight.kotlin.library_permiss.AndroidVersion.isAndroid4_2
import com.knight.kotlin.library_permiss.PermissionChecker.checkActivityStatus
import com.knight.kotlin.library_permiss.PermissionChecker.checkBodySensorsPermission
import com.knight.kotlin.library_permiss.PermissionChecker.checkLocationPermission
import com.knight.kotlin.library_permiss.PermissionChecker.checkManifestPermissions
import com.knight.kotlin.library_permiss.PermissionChecker.checkMediaLocationPermission
import com.knight.kotlin.library_permiss.PermissionChecker.checkNearbyDevicesPermission
import com.knight.kotlin.library_permiss.PermissionChecker.checkNotificationListenerPermission
import com.knight.kotlin.library_permiss.PermissionChecker.checkPermissionArgument
import com.knight.kotlin.library_permiss.PermissionChecker.checkPictureInPicturePermission
import com.knight.kotlin.library_permiss.PermissionChecker.checkStoragePermission
import com.knight.kotlin.library_permiss.PermissionChecker.checkTargetSdkVersion
import com.knight.kotlin.library_permiss.PermissionChecker.optimizeDeprecatedPermission
import com.knight.kotlin.library_permiss.PermissionIntentManager.getApplicationDetailsIntent
import com.knight.kotlin.library_permiss.fragment.PermissionPageFragment
import com.knight.kotlin.library_permiss.listener.IPermissionInterceptor
import com.knight.kotlin.library_permiss.listener.OnPermissionCallback
import com.knight.kotlin.library_permiss.listener.OnPermissionPageCallback
import com.knight.kotlin.library_permiss.permissions.PermissionApi.containsSpecialPermission
import com.knight.kotlin.library_permiss.permissions.PermissionApi.getDeniedPermissions
import com.knight.kotlin.library_permiss.permissions.PermissionApi.isGrantedPermissions
import com.knight.kotlin.library_permiss.permissions.PermissionApi.isPermissionPermanentDenied
import com.knight.kotlin.library_permiss.permissions.PermissionApi.isSpecialPermission
import com.knight.kotlin.library_permiss.utils.PermissionUtils
import com.knight.kotlin.library_permiss.utils.PermissionUtils.asArrayList
import com.knight.kotlin.library_permiss.utils.PermissionUtils.asArrayLists
import com.knight.kotlin.library_permiss.utils.PermissionUtils.findActivity
import com.knight.kotlin.library_permiss.utils.PermissionUtils.getAndroidManifestInfo
import com.knight.kotlin.library_permiss.utils.PermissionUtils.getSmartPermissionIntent
import com.knight.kotlin.library_permiss.utils.PermissionUtils.isDebugMode


/**
 * Author:Knight
 * Time:2022/1/20 16:57
 * Description:XXPermissions
 */
class XXPermissions constructor(context: Context) {
    /** Context 对象  */
    private var mContext: Context? = null
    init {
        mContext = context
    }
    companion object {
        /** 权限设置页跳转请求码  */
        const val REQUEST_CODE = 1024 + 1

        /** 权限请求拦截器  */
        private var sInterceptor: IPermissionInterceptor? = null

        /** 当前是否为检查模式  */
        private var sCheckMode: Boolean = false

        /**
         * 设置请求的对象
         *
         * @param context          当前 Activity，可以传入栈顶的 Activity
         */
        fun with(context: Context): XXPermissions {
            return XXPermissions(context)
        }

        fun with(fragment: Fragment): XXPermissions? {
            return fragment.activity?.let { with(it) }
        }



        /**
         * 是否为检查模式
         */
        fun setCheckMode(checkMode: Boolean) {
            sCheckMode = checkMode
        }

        /**
         * 设置全局权限请求拦截器
         */
        fun setInterceptor(interceptor: IPermissionInterceptor?) {
            sInterceptor = interceptor
        }

        /**
         * 获取全局权限请求拦截器
         */
        fun getInterceptor(): IPermissionInterceptor? {
            if (sInterceptor == null) {
                sInterceptor = object : IPermissionInterceptor {}
            }
            return sInterceptor
        }

        /**
         * 判断一个或多个权限是否全部授予了
         */
        fun isGranted(context: Context,  vararg permissions: String): Boolean {
            return isGranted(context, asArrayList(*permissions))
        }

        fun isGranted(
            context: Context,
            vararg permissions: Array<String>
        ): Boolean {
            return isGranted(context, asArrayLists<String>(*permissions))
        }

        fun isGranted( context: Context,  permissions: List<String>): Boolean {
            return isGrantedPermissions(context, permissions)
        }

        /**
         * 获取没有授予的权限
         */
        fun getDenied(
            context: Context,
            vararg permissions: String
        ): List<String?>? {
            return getDenied(context, asArrayList(*permissions))
        }

        fun getDenied(
            context: Context,
            vararg permissions: Array<String>
        ): List<String?>? {
            return getDenied(context, asArrayLists<String>(*permissions))
        }

        fun getDenied(
            context: Context,
            permissions: List<String>
        ): List<String> {
            return getDeniedPermissions(context, permissions)
        }

        /**
         * 判断某个权限是否为特殊权限
         */
        fun isSpecial(permission: String): Boolean {
            return isSpecialPermission(permission)
        }

        /**
         * 判断权限列表中是否包含特殊权限
         */
        fun containsSpecial( vararg permissions: String): Boolean {
            return containsSpecial(asArrayList(*permissions))
        }

        fun containsSpecial( permissions: List<String?>?): Boolean {
            return containsSpecialPermission(permissions)
        }

        /**
         * 判断一个或多个权限是否被永久拒绝了
         *
         * 注意不能在请求权限之前调用，一定要在 [OnPermissionCallback.onDenied] 方法中调用
         * 如果你在应用启动后，没有申请过这个权限，然后去判断它有没有永久拒绝，这样系统会一直返回 true，也就是永久拒绝
         * 但是实际并没有永久拒绝，系统只是不想让你知道权限是否被永久拒绝了，你必须要申请过这个权限，才能去判断这个权限是否被永久拒绝
         */
        fun isPermanentDenied(
            activity: Activity,
            vararg permissions: String
        ): Boolean {
            return isPermanentDenied(activity, asArrayList(*permissions))
        }

        fun isPermanentDenied(
            activity: Activity,
            vararg permissions: Array<String>
        ): Boolean {
            return isPermanentDenied(activity, asArrayLists<String>(*permissions))
        }

        fun isPermanentDenied(
            activity: Activity,
            permissions: List<String>
        ): Boolean {
            return isPermissionPermanentDenied(activity, permissions)
        }

        /* android.content.Context */

        /* android.content.Context */
        fun startPermissionActivity( context: Context) {
            startPermissionActivity(context, ArrayList(0))
        }

        fun startPermissionActivity(context: Context,  vararg permissions: String) {
            startPermissionActivity(context, asArrayList<String>(*permissions))
        }

        fun startPermissionActivity(
            context: Context,
            vararg permissions: Array<String>
        ) {
            startPermissionActivity(context, asArrayLists<String>(*permissions))
        }

        /**
         * 跳转到应用权限设置页
         *
         * @param permissions           没有授予或者被拒绝的权限组
         */
        fun startPermissionActivity(context: Context,  permissions: List<String>) {
            val activity = findActivity(
                context
            )
            if (activity!= null) {
                startPermissionActivity(activity, permissions)
                return
            }
            val intent = getSmartPermissionIntent(
                context, permissions
            )
            if (context !is Activity) {
                intent!!.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            StartActivityManager.startActivity(context, intent!!)
        }

        /* android.app.Activity */

        /* android.app.Activity */
        fun startPermissionActivity( activity: Activity) {
            startPermissionActivity(activity, ArrayList(0))
        }

        fun startPermissionActivity(
            activity: Activity,
            vararg permissions: String
        ) {
            startPermissionActivity(activity, asArrayList<String>(*permissions))
        }

        fun startPermissionActivity(
            activity: Activity,
            vararg permissions: Array<String>
        ) {
            startPermissionActivity(activity, asArrayLists<String>(*permissions))
        }

        fun startPermissionActivity(
            activity: Activity,
            permissions: List<String>
        ) {
            startPermissionActivity(activity, permissions, REQUEST_CODE)
        }

        fun startPermissionActivity(
            activity: Activity,
            permissions: List<String>,
            requestCode: Int
        ) {
            val intent = getSmartPermissionIntent(
                activity, permissions
            )
            StartActivityManager.startActivityForResult(activity, intent!!, requestCode)
        }

        fun startPermissionActivity(
            activity: FragmentActivity,
            permission: String,
            callback: OnPermissionPageCallback
        ) {
            startPermissionActivity(activity, asArrayList(permission), callback)
        }

        fun startPermissionActivity(
            activity: Activity,
            permissions: Array<String>,
            callback: OnPermissionPageCallback
        ) {
            startPermissionActivity(activity, permissions, callback)
        }

        fun startPermissionActivity(
            activity: FragmentActivity,
            permissions: List<String>,
            callback: OnPermissionPageCallback
        ) {
            if (permissions.isEmpty()) {
                StartActivityManager.startActivity(
                    activity, getApplicationDetailsIntent(
                        activity
                    )
                )
                return
            }
            PermissionPageFragment.beginRequest(activity, permissions as ArrayList<String>, callback)
        }


        fun startPermissionActivity(
            fragment: Fragment,
            vararg permissions: String
        ) {
            startPermissionActivity(fragment, asArrayList<String>(*permissions))
        }

        fun startPermissionActivity(
            fragment: Fragment,
            vararg permissions: Array<String>
        ) {
            startPermissionActivity(fragment, asArrayLists<String>(*permissions))
        }

        fun startPermissionActivity(
            fragment: Fragment,
            permissions: List<String>
        ) {
            startPermissionActivity(fragment, permissions, REQUEST_CODE)
        }


        fun startPermissionActivity(
            fragment: Fragment,
            permission: String,
            callback: OnPermissionPageCallback
        ) {
            startPermissionActivity(fragment, asArrayList(permission), callback)
        }

        fun startPermissionActivity(
            fragment: Fragment,
            permissions: Array<String>,
            callback: OnPermissionPageCallback
        ) {
            startPermissionActivity(fragment, permissions, callback)
        }


        /* android.support.v4.app.Fragment */

        /* android.support.v4.app.Fragment */
        fun startPermissionActivity( fragment: Fragment) {
            startPermissionActivity(fragment, ArrayList())
        }




        fun startPermissionActivity(
            fragment: Fragment,
            permissions: List<String>,
            requestCode: Int
        ) {
            val activity: Activity = fragment.getActivity() ?: return
            if (permissions.isEmpty()) {
                StartActivityManager.startActivity(fragment, getApplicationDetailsIntent(activity))
                return
            }
            val intent = getSmartPermissionIntent(activity, permissions)
            intent?.let { StartActivityManager.startActivityForResult(fragment, it, requestCode) }
        }


        fun startPermissionActivity(
            fragment: Fragment,
            permissions: List<String>,
            callback: OnPermissionPageCallback
        ) {
            val activity: FragmentActivity? = fragment.getActivity()
            if (activity == null || activity.isFinishing) {
                return
            }
            if (isAndroid4_2() && activity.isDestroyed) {
                return
            }
            if (permissions.isEmpty()) {
                StartActivityManager.startActivity(fragment, getApplicationDetailsIntent(activity))
                return
            }
            PermissionPageFragment.beginRequest(activity, permissions as ArrayList<String>, callback)
        }

    }

    /** 权限列表  */
    private var mPermissions: MutableList<String> = mutableListOf()

    /** 权限请求拦截器  */
    private var mInterceptor: IPermissionInterceptor? = null

    /** 设置不检查  */
    private var mCheckMode: Boolean = false



    /**
     * 添加权限组
     */
    fun permission(vararg permissions: String): XXPermissions {

        return permission(PermissionUtils.asArrayList(*permissions))
    }

    fun permission(vararg permissions: Array<String>): XXPermissions {
        return permission(PermissionUtils.asArrayLists(*permissions))
    }

    private fun permission(permissions: List<String>): XXPermissions {
        if (permissions == null || permissions.isEmpty()) {
            return this
        }
        if (mPermissions == null) {
            mPermissions = ArrayList(permissions)
            return this
        }
        for (permission in permissions) {
            if (mPermissions?.contains(permission) == true) {
                continue
            }
            mPermissions?.add(permission)
        }
        return this
    }

    /**
     * 设置权限请求拦截器
     */
    fun interceptor(interceptor: IPermissionInterceptor?): XXPermissions {
        mInterceptor = interceptor
        return this
    }

    /**
     * 设置不触发错误检测机制
     */
    fun unchecked(): XXPermissions {
        mCheckMode = false
        return this
    }

    /**
     * 请求权限
     */
    fun request( callback: OnPermissionCallback) {
        if (mContext == null) {
            return
        }
        if (mInterceptor == null) {
            mInterceptor = getInterceptor()
        }
        val context: Context = mContext!!
        val interceptor = mInterceptor!!

        // 权限请求列表（为什么直接不用字段？因为框架要兼容新旧权限，在低版本下会自动添加旧权限申请，为了避免重复添加）
        val permissions = mPermissions
        val checkMode: Boolean = isCheckMode(context)

        // 检查当前 Activity 状态是否是正常的，如果不是则不请求权限
        val activity = findActivity(context)
        if (!checkActivityStatus(activity, checkMode)) {
            return
        }

        // 必须要传入正常的权限或者权限组才能申请权限
        if (!checkPermissionArgument(permissions, checkMode)) {
            return
        }
        if (checkMode) {
            // 获取清单文件信息
            val androidManifestInfo = getAndroidManifestInfo(context)
            // 检查申请的读取媒体位置权限是否符合规范
            checkMediaLocationPermission(context, permissions)
            // 检查申请的存储权限是否符合规范
            checkStoragePermission(context, permissions, androidManifestInfo)
            // 检查申请的传感器权限是否符合规范
            checkBodySensorsPermission(permissions)
            // 检查申请的定位权限是否符合规范
            checkLocationPermission(permissions)
            // 检查申请的画中画权限是否符合规范
            checkPictureInPicturePermission(activity!!, permissions, androidManifestInfo)
            // 检查申请的通知栏监听权限是否符合规范
            checkNotificationListenerPermission(permissions, androidManifestInfo)
            // 检查蓝牙和 WIFI 权限申请是否符合规范
            checkNearbyDevicesPermission(permissions, androidManifestInfo)
            // 检查申请的权限和 targetSdk 版本是否能吻合
            checkTargetSdkVersion(context, permissions)
            // 检测权限有没有在清单文件中注册
            checkManifestPermissions(context, permissions, androidManifestInfo)
        }

        // 优化所申请的权限列表
        optimizeDeprecatedPermission(permissions)
        if (isGrantedPermissions(context, permissions)) {
            // 证明这些权限已经全部授予过，直接回调成功
            if (callback != null) {
                interceptor.grantedPermissionRequest(
                    activity,
                    permissions,
                    permissions,
                    true,
                    callback
                )
                activity?.let { interceptor.finishPermissionRequest(it, permissions, true, callback) }
            }
            return
        }

        // 申请没有授予过的权限
        activity?.let { interceptor.launchPermissionRequest(it, permissions, callback) }
    }

    /**
     * 撤销权限并杀死当前进程
     *
     * @return          返回 true 代表成功，返回 false 代表失败
     */
    fun revokeOnKill(): Boolean {
        if (mContext == null) {
            return false
        }
        val context: Context = mContext!!
        val permissions: List<String> = mPermissions
        if (permissions.isEmpty()) {
            return false
        }
        return if (!isAndroid13()) {
            false
        } else try {
            if (permissions.size == 1) {
                // API 文档：https://developer.android.google.cn/reference/android/content/Context#revokeSelfPermissionOnKill(java.lang.String)
                context.revokeSelfPermissionOnKill(permissions[0])
            } else {
                // API 文档：https://developer.android.google.cn/reference/android/content/Context#revokeSelfPermissionsOnKill(java.util.Collection%3Cjava.lang.String%3E)
                context.revokeSelfPermissionsOnKill(permissions)
            }
            true
        } catch (e: IllegalArgumentException) {
            if (isCheckMode(context)) {
                throw e
            }
            e.printStackTrace()
            false
        }
    }

    /**
     * 当前是否为检测模式
     */
    private fun isCheckMode(context: Context): Boolean {
        if (mCheckMode == null) {
            if (sCheckMode == null) {
                sCheckMode = isDebugMode(context)
            }
            mCheckMode = sCheckMode
        }
        return mCheckMode
    }



}