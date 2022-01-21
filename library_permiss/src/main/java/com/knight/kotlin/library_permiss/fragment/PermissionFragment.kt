package com.knight.kotlin.library_permiss.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.knight.kotlin.library_permiss.listener.IPermissionInterceptor
import com.knight.kotlin.library_permiss.listener.OnPermissionCallback
import com.knight.kotlin.library_permiss.permissions.Permission
import com.knight.kotlin.library_permiss.PermissionSettingPage
import com.knight.kotlin.library_permiss.utils.PermissionUtils
import java.util.Arrays
import java.util.Random

/**
 * Author:Knight
 * Time:2022/1/20 15:00
 * Description:PermissionFragment
 */
class PermissionFragment:Fragment(),Runnable {

    companion object{
        /** 请求的权限组  */
        private val REQUEST_PERMISSIONS = "request_permissions"

        /** 请求码（自动生成） */
        private val REQUEST_CODE = "request_code"

        /** 权限请求码存放集合  */
        private val REQUEST_CODE_ARRAY = mutableListOf<Int>()
        /**
         * 开启权限申请
         */
        fun beginRequest(
            activity: FragmentActivity?, permissions: ArrayList<String>,
            interceptor: IPermissionInterceptor?, callback: OnPermissionCallback?
        ) {
            val fragment = PermissionFragment()
            val bundle = Bundle()
            var requestCode: Int
            // 请求码随机生成，避免随机产生之前的请求码，必须进行循环判断
            do {
                // 新版本的 Support 库限制请求码必须小于 65536
                // 旧版本的 Support 库限制请求码必须小于 256
                requestCode = Random().nextInt(Math.pow(2.0, 8.0).toInt())
            } while (REQUEST_CODE_ARRAY.contains(requestCode))
            // 标记这个请求码已经被占用
            REQUEST_CODE_ARRAY.add(requestCode)
            bundle.putInt(REQUEST_CODE, requestCode)
            bundle.putStringArrayList(REQUEST_PERMISSIONS, permissions)
            fragment.arguments = bundle
            // 设置保留实例，不会因为屏幕方向或配置变化而重新创建
            fragment.retainInstance = true
            // 设置权限申请标记
            fragment.setRequestFlag(true)
            // 设置权限回调监听
            callback?.let { fragment.setCallBack(it) }
            // 设置权限请求拦截器
            interceptor?.let { fragment.setInterceptor(it) }
            // 绑定到 Activity 上面
            activity?.let { fragment.attachActivity(it) }
        }

    }

    /** 是否申请了特殊权限  */
    private var mSpecialRequest = false

    /** 是否申请了危险权限  */
    private var mDangerousRequest = false

    /** 权限申请标记  */
    private var mRequestFlag = false

    /** 权限回调对象  */
    private var mCallBack: OnPermissionCallback? = null

    /** 权限请求拦截器  */
    private var mInterceptor: IPermissionInterceptor? = null

    /** Activity 屏幕方向  */
    private var mScreenOrientation = 0

    /**
     * 绑定 Activity
     */
    fun attachActivity(activity: FragmentActivity) {
        activity.supportFragmentManager.beginTransaction().add(this, this.toString())
            .commitAllowingStateLoss()
    }

    /**
     * 解绑 Activity
     */
    fun detachActivity(activity: FragmentActivity) {
        activity.supportFragmentManager.beginTransaction().remove(this).commitAllowingStateLoss()
    }

    /**
     * 设置权限监听回调监听
     */
    fun setCallBack(callback: OnPermissionCallback) {
        mCallBack = callback
    }

    /**
     * 权限申请标记（防止系统杀死应用后重新触发请求的问题）
     */
    fun setRequestFlag(flag: Boolean) {
        mRequestFlag = flag
    }

    /**
     * 设置权限请求拦截器
     */
    fun setInterceptor(interceptor: IPermissionInterceptor) {
        mInterceptor = interceptor
    }

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onAttach(context: Context) {
        super.onAttach(context)
        val activity = activity ?: return
        // 如果当前没有锁定屏幕方向就获取当前屏幕方向并进行锁定
        mScreenOrientation = activity.requestedOrientation
        if (mScreenOrientation != ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED) {
            return
        }
        try {
            // 兼容问题：在 Android 8.0 的手机上可以固定 Activity 的方向，但是这个 Activity 不能是透明的，否则就会抛出异常
            // 复现场景：只需要给 Activity 主题设置 <item name="android:windowIsTranslucent">true</item> 属性即可
            when (activity.resources.configuration.orientation) {
                Configuration.ORIENTATION_LANDSCAPE -> activity.requestedOrientation =
                    if (PermissionUtils.isActivityReverse(activity)) ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE else ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                Configuration.ORIENTATION_PORTRAIT -> activity.requestedOrientation =
                    if (PermissionUtils.isActivityReverse(activity)) ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT else ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                else -> activity.requestedOrientation =
                    if (PermissionUtils.isActivityReverse(activity)) ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT else ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            }
        } catch (e: IllegalStateException) {
            // java.lang.IllegalStateException: Only fullscreen activities can request orientation
            e.printStackTrace()
        }
    }

    override fun onDetach() {
        super.onDetach()
        val activity: Activity? = activity
        if (activity == null || mScreenOrientation != ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED) {
            return
        }
        // 为什么这里不用跟上面一样 try catch ？因为这里是把 Activity 方向取消固定，只有设置横屏或竖屏的时候才可能触发 crash
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }

    override fun onDestroy() {
        super.onDestroy()
        // 取消引用监听器，避免内存泄漏
        mCallBack = null
    }

    override fun onResume() {
        super.onResume()

        // 如果当前 Fragment 是通过系统重启应用触发的，则不进行权限申请
        if (!mRequestFlag) {
            activity?.let { detachActivity(it) }
            return
        }

        // 如果在 Activity 不可见的状态下添加 Fragment 并且去申请权限会导致授权对话框显示不出来
        // 所以必须要在 Fragment 的 onResume 来申请权限，这样就可以保证应用回到前台的时候才去申请权限
        if (mSpecialRequest) {
            return
        }
        mSpecialRequest = true
        requestSpecialPermission()
    }

    /**
     * 申请特殊权限
     */
    fun requestSpecialPermission() {
        val arguments = arguments
        val activity: Activity? = activity
        if (arguments == null || activity == null) {
            return
        }
        val allPermissions: List<String>? =
            arguments.getStringArrayList(REQUEST_PERMISSIONS)

        // 是否需要申请特殊权限
        var requestSpecialPermission = false

        // 判断当前是否包含特殊权限
        for (permission in allPermissions!!) {
            if (PermissionUtils.isSpecialPermission(permission)) {
                if (PermissionUtils.isGrantedPermission(activity, permission)) {
                    // 已经授予过了，可以跳过
                    continue
                }
                if (Permission.MANAGE_EXTERNAL_STORAGE.equals(permission) && !PermissionUtils.isAndroid11()) {
                    // 当前必须是 Android 11 及以上版本，因为在旧版本上是拿旧权限做的判断
                    continue
                }
                // 跳转到特殊权限授权页面
                getArguments()?.getInt(REQUEST_CODE)?.let {
                    startActivityForResult(
                        PermissionSettingPage.getSmartPermissionIntent(
                            activity,
                            PermissionUtils.asArrayList(permission)
                        ), it
                    )
                }
                requestSpecialPermission = true
            }
        }
        if (requestSpecialPermission) {
            return
        }
        // 如果没有跳转到特殊权限授权页面，就直接申请危险权限
        requestDangerousPermission()
    }

    /**
     * 申请危险权限
     */
    fun requestDangerousPermission() {
        val activity = activity
        val arguments = arguments
        if (activity == null || arguments == null) {
            return
        }
        val requestCode = arguments.getInt(REQUEST_CODE)
        val allPermissions = arguments.getStringArrayList(REQUEST_PERMISSIONS)
        if (allPermissions == null || allPermissions.isEmpty()) {
            return
        }
        if (!PermissionUtils.isAndroid6()) {
            // 如果是 Android 6.0 以下，没有危险权限的概念，则直接回调监听
            val grantResults = IntArray(allPermissions.size)
            for (i in grantResults.indices) {
                grantResults[i] = if (PermissionUtils.isGrantedPermission(
                        activity,
                        allPermissions[i]
                    )
                ) PackageManager.PERMISSION_GRANTED else PackageManager.PERMISSION_DENIED
            }
            onRequestPermissionsResult(requestCode, allPermissions.toTypedArray(), grantResults)
            return
        }
        var locationPermission: ArrayList<String>? = null
        // Android 10 定位策略发生改变，申请后台定位权限的前提是要有前台定位权限（授予了精确或者模糊任一权限）
        if (PermissionUtils.isAndroid10() && allPermissions.contains(Permission.ACCESS_BACKGROUND_LOCATION)) {
            locationPermission = ArrayList()
            if (allPermissions.contains(Permission.ACCESS_COARSE_LOCATION)) {
                locationPermission.add(Permission.ACCESS_COARSE_LOCATION)
            }
            if (allPermissions.contains(Permission.ACCESS_FINE_LOCATION)) {
                locationPermission.add(Permission.ACCESS_FINE_LOCATION)
            }
        }
        if (!PermissionUtils.isAndroid10() || locationPermission == null || locationPermission.isEmpty()) {
            getArguments()?.getInt(REQUEST_CODE)?.let {
                requestPermissions(
                    allPermissions.toTypedArray(),
                    it
                )
            }
            return
        }


        // 在 Android 10 的机型上，需要先申请前台定位权限，再申请后台定位权限
        beginRequest(activity, locationPermission, object : IPermissionInterceptor {},object :
            OnPermissionCallback {
                override fun onGranted(permissions: List<String>, all: Boolean) {
                    if (!all || !isAdded) {
                        return
                    }

                    // 前台定位权限授予了，现在申请后台定位权限
                    beginRequest(activity,
                        PermissionUtils.asArrayList(Permission.ACCESS_BACKGROUND_LOCATION),
                        object : IPermissionInterceptor {},
                        object : OnPermissionCallback {
                            override fun onGranted(permissions: List<String>, all: Boolean) {
                                if (!all || !isAdded) {
                                    return
                                }

                                // 前台定位权限和后台定位权限都授予了
                                val grantResults = IntArray(allPermissions.size)
                                Arrays.fill(grantResults, PackageManager.PERMISSION_GRANTED)
                                onRequestPermissionsResult(
                                    requestCode,
                                    allPermissions.toTypedArray(),
                                    grantResults
                                )
                            }

                            override fun onDenied(permissions: List<String>, never: Boolean) {
                                if (!isAdded) {
                                    return
                                }

                                // 后台定位授权失败，但是前台定位权限已经授予了
                                val grantResults = IntArray(allPermissions.size)
                                for (i in allPermissions.indices) {
                                    grantResults[i] =
                                        if (Permission.ACCESS_BACKGROUND_LOCATION.equals(
                                                allPermissions[i]
                                            )
                                        ) PackageManager.PERMISSION_DENIED else PackageManager.PERMISSION_GRANTED
                                }
                                onRequestPermissionsResult(
                                    requestCode,
                                    allPermissions.toTypedArray(),
                                    grantResults
                                )
                            }
                        })
                }

                override fun onDenied(permissions: List<String>, never: Boolean) {
                    if (!isAdded) {
                        return
                    }

                    // 前台定位授权失败，并且无法申请后台定位权限
                    val grantResults = IntArray(allPermissions.size)
                    Arrays.fill(grantResults, PackageManager.PERMISSION_DENIED)
                    onRequestPermissionsResult(
                        requestCode,
                        allPermissions.toTypedArray(),
                        grantResults
                    )
                }
            })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (permissions == null || permissions.size == 0 || grantResults == null || grantResults.size == 0) {
            return
        }
        val arguments = arguments
        val activity = activity
        if (activity == null || arguments == null || mInterceptor == null || requestCode != arguments.getInt(
                REQUEST_CODE
            )
        ) {
            return
        }
        val callback = mCallBack
        mCallBack = null
        val interceptor: IPermissionInterceptor? = mInterceptor
        mInterceptor = null

        // 优化权限回调结果
        PermissionUtils.optimizePermissionResults(activity, permissions, grantResults)

        // 将数组转换成 ArrayList
        val allPermissions: List<String> = PermissionUtils.asArrayList(*permissions)

        // 释放对这个请求码的占用
        REQUEST_CODE_ARRAY.remove(requestCode)
        // 将 Fragment 从 Activity 移除
        detachActivity(activity)

        // 获取已授予的权限
        val grantedPermissions: List<String> =
            PermissionUtils.getGrantedPermissions(allPermissions, grantResults)

        // 如果请求成功的权限集合大小和请求的数组一样大时证明权限已经全部授予
        if (grantedPermissions.size == allPermissions.size) {
            // 代表申请的所有的权限都授予了
            interceptor?.grantedPermissions(
                activity,
                allPermissions,
                grantedPermissions,
                true,
                callback
            )
            return
        }

        // 获取被拒绝的权限
        val deniedPermission: List<String> =
            PermissionUtils.getDeniedPermissions(allPermissions, grantResults)

        // 代表申请的权限中有不同意授予的，如果有某个权限被永久拒绝就返回 true 给开发人员，让开发者引导用户去设置界面开启权限
        interceptor?.deniedPermissions(
            activity, allPermissions, deniedPermission,
            PermissionUtils.isPermissionPermanentDenied(activity, deniedPermission), callback
        )

        // 证明还有一部分权限被成功授予，回调成功接口
        if (!grantedPermissions.isEmpty()) {
            interceptor?.grantedPermissions(
                activity,
                allPermissions,
                grantedPermissions,
                false,
                callback
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val activity: Activity? = activity
        val arguments = arguments
        if (activity == null || arguments == null || mDangerousRequest || requestCode != arguments.getInt(
                REQUEST_CODE
            )
        ) {
            return
        }
        mDangerousRequest = true
        // 需要延迟执行，不然有些华为机型授权了但是获取不到权限
        activity.window.decorView.postDelayed(this, 300)
    }

    override fun run() {
        // 如果用户离开太久，会导致 Activity 被回收掉
        // 所以这里要判断当前 Fragment 是否有被添加到 Activity
        // 可在开发者模式中开启不保留活动来复现这个 Bug
        if (!isAdded) {
            return
        }
        // 请求其他危险权限
        requestDangerousPermission()
    }


}