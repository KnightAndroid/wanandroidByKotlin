package com.knight.kotlin.library_permiss

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.knight.kotlin.library_permiss.listener.IPermissionInterceptor
import com.knight.kotlin.library_permiss.listener.OnPermissionCallback
import com.knight.kotlin.library_permiss.utils.PermissionUtils

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
//
//    public static XXPermissions with(android.support.v4.app.Fragment fragment) {
//        return with(fragment.getActivity());
//    }

        //
        //    public static XXPermissions with(android.support.v4.app.Fragment fragment) {
        //        return with(fragment.getActivity());
        //    }
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
        fun isGranted(context: Context?, vararg permissions: String): Boolean {
            return isGranted(context, PermissionUtils.asArrayList(*permissions))
        }

        fun isGranted(context: Context?, vararg permissions: Array<String>): Boolean {
            return isGranted(context, PermissionUtils.asArrayLists(*permissions))
        }

        fun isGranted(context: Context?, permissions: List<String>): Boolean {
            return PermissionUtils.isGrantedPermissions(context!!, permissions)
        }

        /**
         * 获取没有授予的权限
         */
        fun getDenied(context: Context?, vararg permissions: String): List<String>? {
            return getDenied(context, PermissionUtils.asArrayList(*permissions))
        }

        fun getDenied(context: Context?, vararg permissions: Array<String>): List<String>? {
            return getDenied(context, PermissionUtils.asArrayLists(*permissions))
        }

        fun getDenied(context: Context?, permissions: List<String>): List<String>? {
            return context?.let { PermissionUtils.getDeniedPermissions(it, permissions) }
        }

        /**
         * 判断某个权限是否为特殊权限
         */
        fun isSpecial(permission: String?): Boolean {
            return PermissionUtils.isSpecialPermission(permission)
        }

        /**
         * 判断一个或多个权限是否被永久拒绝了
         *
         * （注意不能在请求权限之前调用，应该在 [OnPermissionCallback.onDenied] 方法中调用）
         */
        fun isPermanentDenied(activity: Activity?, vararg permissions: String?): Boolean {
            return isPermanentDenied(activity, PermissionUtils.asArrayList(*permissions))
        }

        fun isPermanentDenied(activity: Activity?, vararg permissions: Array<String>): Boolean {
            return isPermanentDenied(activity, PermissionUtils.asArrayLists(*permissions))
        }

        fun isPermanentDenied(activity: Activity?, permissions: List<String?>): Boolean {
            return activity?.let { PermissionUtils.isPermissionPermanentDenied(it, permissions) } == true
        }

        /**
         * 跳转到应用权限设置页
         *
         * @param permissions           没有授予或者被拒绝的权限组
         */
        fun startPermissionActivity(context: Context, permissions: List<String>) {
            val activity: Activity? = PermissionUtils.findActivity(context)
            if (activity != null) {
                startPermissionActivity(activity, permissions)
                return
            }
            val intent = PermissionSettingPage.getSmartPermissionIntent(context, permissions)
            if (context !is Activity) {
                intent!!.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }

        /* android.app.Activity */

        /* android.app.Activity */
        fun startPermissionActivity(activity: Activity) {
            startPermissionActivity(activity, null as List<String>)
        }

        fun startPermissionActivity(activity: Activity, vararg permissions: String) {
            startPermissionActivity(activity, PermissionUtils.asArrayList(*permissions))
        }

        fun startPermissionActivity(activity: Activity, vararg permissions: Array<String>) {
            startPermissionActivity(activity, PermissionUtils.asArrayLists(*permissions))
        }

        fun startPermissionActivity(activity: Activity, permissions: List<String>) {
            startPermissionActivity(activity, permissions, REQUEST_CODE)
        }

        /**
         * 跳转到应用权限设置页
         *
         * @param permissions           没有授予或者被拒绝的权限组
         * @param requestCode           Activity 跳转请求码
         */
        fun startPermissionActivity(activity: Activity, permissions: List<String?>?, requestCode: Int) {
            activity.startActivityForResult(
                PermissionSettingPage.getSmartPermissionIntent(
                    activity,
                    permissions
                ), requestCode
            )
        }

        /* android.app.Fragment */

        /* android.app.Fragment */
        fun startPermissionActivity(fragment: Fragment) {
            startPermissionActivity(fragment, null as List<String>)
        }

        fun startPermissionActivity(fragment: Fragment, vararg permissions: String) {
            startPermissionActivity(fragment, PermissionUtils.asArrayList(*permissions))
        }

        fun startPermissionActivity(fragment: Fragment, vararg permissions: Array<String>) {
            startPermissionActivity(fragment, PermissionUtils.asArrayLists(*permissions))
        }

        fun startPermissionActivity(fragment: Fragment, permissions: List<String>) {
            startPermissionActivity(fragment, permissions, REQUEST_CODE)
        }

        /**
         * 跳转到应用权限设置页
         *
         * @param permissions           没有授予或者被拒绝的权限组
         * @param requestCode           Activity 跳转请求码
         */
        fun startPermissionActivity(fragment: Fragment, permissions: List<String?>?, requestCode: Int) {
            val activity = fragment.activity ?: return
            fragment.startActivityForResult(
                PermissionSettingPage.getSmartPermissionIntent(
                    activity,
                    permissions
                ), requestCode
            )
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
    fun request(callback: OnPermissionCallback?) {
        if (mContext == null) {
            return
        }
        if (mInterceptor == null) {
            mInterceptor = getInterceptor()
        }

        // 权限请求列表（为什么直接不用字段？因为框架要兼容新旧权限，在低版本下会自动添加旧权限申请）
        val permissions = mPermissions
        if (mCheckMode == null) {
            if (sCheckMode == null) {
                sCheckMode = mContext?.let { PermissionUtils.isDebugMode(it) } == true
            }
            mCheckMode = sCheckMode
        }

        // 检查当前 Activity 状态是否是正常的，如果不是则不请求权限
        val activity = PermissionUtils.findActivity(mContext)
        if (!PermissionChecker.checkActivityStatus(activity, mCheckMode)) {
            return
        }

        // 必须要传入正常的权限或者权限组才能申请权限
        if (!PermissionChecker.checkPermissionArgument(permissions, mCheckMode)) {
            return
        }
        if (mCheckMode) {
            // 检查申请的存储权限是否符合规范
            mContext?.let {
                PermissionChecker.checkStoragePermission(it, permissions)
                // 检查申请的定位权限是否符合规范
                PermissionChecker.checkLocationPermission(it, permissions)
                // 检查申请的权限和 targetSdk 版本是否能吻合
                PermissionChecker.checkTargetSdkVersion(it, permissions)
            }


        }
        if (mCheckMode) {
            // 检测权限有没有在清单文件中注册
            mContext?.let {
                PermissionChecker.checkManifestPermissions(it, permissions)
            }

        }

        // 优化所申请的权限列表
        PermissionChecker.optimizeDeprecatedPermission(permissions)
        mContext?.let {
            if (PermissionUtils.isGrantedPermissions(it, permissions)) {
                // 证明这些权限已经全部授予过，直接回调成功
                if (callback != null) {
                    activity?.let {
                        mInterceptor?.grantedPermissions(
                            it,
                            permissions,
                            permissions,
                            true,
                            callback
                        )
                    }
                }
                return
            }
        }

        // 申请没有授予过的权限
        activity?.let { mInterceptor?.requestPermissions(it, callback, permissions) }
    }




}