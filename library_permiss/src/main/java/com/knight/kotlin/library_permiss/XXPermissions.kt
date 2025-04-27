package com.knight.kotlin.library_permiss

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.knight.kotlin.library_permiss.AndroidVersion.isAndroid13
import com.knight.kotlin.library_permiss.AndroidVersion.isAndroid4_2
import com.knight.kotlin.library_permiss.PermissionChecker.checkActivityStatus
import com.knight.kotlin.library_permiss.PermissionChecker.checkPermissionArgument
import com.knight.kotlin.library_permiss.PermissionIntentManager.getApplicationDetailsIntent
import com.knight.kotlin.library_permiss.fragment.RequestSpecialPermissionFragment
import com.knight.kotlin.library_permiss.listener.OnPermissionCallback
import com.knight.kotlin.library_permiss.listener.OnPermissionInterceptor
import com.knight.kotlin.library_permiss.listener.OnPermissionPageCallback
import com.knight.kotlin.library_permiss.permissions.PermissionApi
import com.knight.kotlin.library_permiss.permissions.PermissionApi.containsSpecialPermission
import com.knight.kotlin.library_permiss.permissions.PermissionApi.getDeniedPermissions
import com.knight.kotlin.library_permiss.permissions.PermissionApi.getSmartPermissionIntent
import com.knight.kotlin.library_permiss.permissions.PermissionApi.isGrantedPermissions
import com.knight.kotlin.library_permiss.permissions.PermissionApi.isSpecialPermission
import com.knight.kotlin.library_permiss.utils.PermissionUtils
import com.knight.kotlin.library_permiss.utils.PermissionUtils.asArrayList
import com.knight.kotlin.library_permiss.utils.PermissionUtils.asArrayLists
import com.knight.kotlin.library_permiss.utils.PermissionUtils.findActivity
import com.knight.kotlin.library_permiss.utils.PermissionUtils.getAndroidManifestInfo
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
        private var sInterceptor: OnPermissionInterceptor? = null

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
        fun setInterceptor(interceptor: OnPermissionInterceptor) {
            sInterceptor = interceptor
        }

        /**
         * 获取全局权限请求拦截器
         */
        fun getInterceptor(): OnPermissionInterceptor? {
            if (sInterceptor == null) {
                sInterceptor = object : OnPermissionInterceptor {}
            }
            return sInterceptor
        }

        /**
         * 判断一个或多个权限是否全部授予了
         */
        @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
        fun isGranted(context: Context, vararg permissions: String): Boolean {
            return isGranted(context, asArrayList(*permissions))
        }

        @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
        fun isGranted(
            context: Context,
            vararg permissions: Array<String>
        ): Boolean {
            return isGranted(context, asArrayLists<String>(*permissions))
        }

        @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
        fun isGranted(context: Context, permissions: List<String>): Boolean {
            return isGrantedPermissions(context, permissions)
        }

        /**
         * 获取没有授予的权限
         */
        @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
        fun getDenied(
            context: Context,
            vararg permissions: String
        ): List<String?>? {
            return getDenied(context, asArrayList(*permissions))
        }

        @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
        fun getDenied(
            context: Context,
            vararg permissions: Array<String>
        ): List<String?>? {
            return getDenied(context, asArrayLists<String>(*permissions))
        }

        @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
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

        fun containsSpecial( permissions: List<String>): Boolean {
            return containsSpecialPermission(permissions)
        }

        /**
         * 判断一个或多个权限是否被勾选了不再询问的选项
         *
         * 注意不能在请求权限之前调用，一定要在 [OnPermissionCallback.onDenied] 方法中调用
         * 如果你在应用启动后，没有申请过这个权限，然后去判断它有没有勾选不再询问的选项，这样系统会一直返回 true，也就是不再询问
         * 但是实际上还能继续申请，系统只是不想让你知道权限是否勾选了不再询问的选项，你必须要申请过这个权限，才能去判断这个权限是否勾选了不再询问的选项
         */
        @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
        fun isDoNotAskAgainPermissions(
            activity: Activity,
            vararg permissions: String
        ): Boolean {
            return isDoNotAskAgainPermissions(activity, asArrayList(*permissions))
        }

        @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
        fun isDoNotAskAgainPermissions(
            activity: Activity,
            vararg permissions: Array<String>
        ): Boolean {
            return isDoNotAskAgainPermissions(activity, asArrayLists<String>(*permissions))
        }

        @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
        fun isDoNotAskAgainPermissions(
            activity: Activity,
            permissions: List<String>
        ): Boolean {
            return PermissionApi.isDoNotAskAgainPermissions(activity, permissions)
        }

        /* android.content.Context */

        /* android.content.Context */
        @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
        fun startPermissionActivity(context: Context) {
            startPermissionActivity(context, ArrayList(0))
        }

        @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
        fun startPermissionActivity(context: Context, vararg permissions: String) {
            startPermissionActivity(context, asArrayList<String>(*permissions))
        }

        @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
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
        @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
        fun startPermissionActivity(context: Context, permissions: List<String>) {
            val activity = findActivity(
                context
            )
            if (activity!= null) {
                startPermissionActivity(activity, permissions)
                return
            }
            val intent = PermissionApi.getSmartPermissionIntent(
                context, permissions
            )
            if (context !is Activity) {
                intent!!.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            PermissionActivityIntentHandler.startActivity(context, intent!!)
        }

        /* android.app.Activity */

        /* android.app.Activity */
        @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
        fun startPermissionActivity(activity: Activity) {
            startPermissionActivity(activity, ArrayList(0))
        }

        @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
        fun startPermissionActivity(
            activity: Activity,
            vararg permissions: String
        ) {
            startPermissionActivity(activity, asArrayList<String>(*permissions))
        }

        @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
        fun startPermissionActivity(
            activity: Activity,
            vararg permissions: Array<String>
        ) {
            startPermissionActivity(activity, asArrayLists<String>(*permissions))
        }

        @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
        fun startPermissionActivity(
            activity: Activity,
            permissions: List<String>
        ) {
            startPermissionActivity(activity, permissions, REQUEST_CODE)
        }

        @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
        fun startPermissionActivity(
            activity: Activity,
            permissions: List<String>,
            requestCode: Int
        ) {
            val intent = getSmartPermissionIntent(
                activity, permissions
            )
            PermissionActivityIntentHandler.startActivityForResult(activity, intent!!, requestCode)
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
                PermissionActivityIntentHandler.startActivity(
                    activity, getApplicationDetailsIntent(
                        activity
                    )
                )
                return
            }
            RequestSpecialPermissionFragment.launch(activity, permissions, callback);
        }


        @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
        fun startPermissionActivity(
            fragment: Fragment,
            vararg permissions: String
        ) {
            startPermissionActivity(fragment, asArrayList<String>(*permissions))
        }

        @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
        fun startPermissionActivity(
            fragment: Fragment,
            vararg permissions: Array<String>
        ) {
            startPermissionActivity(fragment, asArrayLists<String>(*permissions))
        }

        @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
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

        @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
        fun startPermissionActivity(fragment: Fragment) {
            startPermissionActivity(fragment, ArrayList())
        }




        @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
        fun startPermissionActivity(
            fragment: Fragment,
            permissions: List<String>,
            requestCode: Int
        ) {
            val activity: Activity = fragment.getActivity() ?: return
            if (permissions.isEmpty()) {
                PermissionActivityIntentHandler.startActivity(fragment, getApplicationDetailsIntent(activity))
                return
            }
            val intent = getSmartPermissionIntent(activity, permissions)
            intent?.let { PermissionActivityIntentHandler.startActivityForResult(fragment, it, requestCode) }
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
                PermissionActivityIntentHandler.startActivity(fragment, getApplicationDetailsIntent(activity))
                return
            }
            RequestSpecialPermissionFragment.launch(activity, permissions as ArrayList<String>, callback)
        }

    }

    /** 权限列表  */
    private var mPermissions: MutableList<String> = mutableListOf()

    /** 权限请求拦截器  */
    private var mInterceptor: OnPermissionInterceptor? = null

    /** 设置不检查  */
    private var mCheckMode: Boolean = false



    /**
     * 添加权限组
     */
    fun permission(@PermissionLimit permission: String): XXPermissions {

        if (permission == null) {
            return this
        }
        if (PermissionUtils.containsPermission(mPermissions, permission)) {
            return this
        }
        mPermissions.add(permission)
        return this;
    }

    fun permission(vararg permissions: Array<String>): XXPermissions {
        return permission(PermissionUtils.asArrayLists(*permissions))
    }

    fun permission(permissions: List<String>): XXPermissions {
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
    fun interceptor(interceptor: OnPermissionInterceptor?): XXPermissions {
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
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    fun request(callback: OnPermissionCallback) {
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
            PermissionChecker.checkMediaLocationPermission(context, permissions)
            // 检查申请的存储权限是否符合规范
            PermissionChecker.checkStoragePermission(context, permissions, androidManifestInfo)
            // 检查申请的传感器权限是否符合规范
            PermissionChecker.checkBodySensorsPermission(permissions)
            // 检查申请的定位权限是否符合规范
            PermissionChecker.checkLocationPermission(permissions)
            // 检查申请的画中画权限是否符合规范
            PermissionChecker.checkPictureInPicturePermission(activity!!, permissions, androidManifestInfo)
            // 检查申请的通知栏监听权限是否符合规范
            PermissionChecker.checkNotificationListenerPermission(permissions, androidManifestInfo)
            // 检查蓝牙和 WIFI 权限申请是否符合规范
            PermissionChecker.checkNearbyDevicesPermission(permissions, androidManifestInfo)
            // 检查对照片和视频的部分访问权限申请是否符合规范
            PermissionChecker.checkReadMediaVisualUserSelectedPermission(permissions)
            // 检查申请的权限和 targetSdk 版本是否能吻合
            PermissionChecker.checkTargetSdkVersion(context, permissions)
            // 检测权限有没有在清单文件中注册
            PermissionChecker.checkManifestPermissions(context, permissions, androidManifestInfo)
        }

        // 优化所申请的权限列表
        PermissionChecker.optimizeDeprecatedPermission(permissions)
        if (isGrantedPermissions(context, permissions)) {
            // 证明这些权限已经全部授予过，直接回调成功
            if (callback != null && activity!=null) {
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