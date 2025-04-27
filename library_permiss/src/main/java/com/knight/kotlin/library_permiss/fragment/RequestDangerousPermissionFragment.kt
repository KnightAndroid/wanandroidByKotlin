package com.knight.kotlin.library_permiss.fragment

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.knight.kotlin.library_permiss.AndroidVersion.isAndroid6
import com.knight.kotlin.library_permiss.listener.OnRequestPermissionsResultCallback
import com.knight.kotlin.library_permiss.permissions.PermissionApi.isGrantedPermission
import kotlin.random.Random


/**
 * @author created by luguian
 * @organize
 * @Date 2025/4/27 14:33
 * @descript: 危险权限申请专用的 Fragment
 */
class RequestDangerousPermissionFragment : RequestBasePermissionFragment() {
    /** 权限回调对象  */
    private var mCallBack: OnRequestPermissionsResultCallback? = null

    /**
     * 设置权限监听回调监听
     */
    fun setOnRequestPermissionsResultCallback(callback: OnRequestPermissionsResultCallback) {
        mCallBack = callback
    }

    override fun onDestroy() {
        super.onDestroy()
        // 取消引用监听器，避免内存泄漏
        mCallBack = null
    }

    /**
     * 开始权限请求
     */
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun startPermissionRequest() {
        val arguments = arguments
        val activity: Activity? = activity
        if (arguments == null || activity == null) {
            return
        }

        val allPermissions: List<String>? = arguments.getStringArrayList(REQUEST_PERMISSIONS)
        val requestCode = arguments.getInt(REQUEST_CODE)
        if (allPermissions == null || allPermissions.isEmpty()) {
            return
        }

        // 请求危险权限
        requestAllDangerousPermission(activity, requestCode, allPermissions)
    }

    /**
     * 申请所有危险权限
     */
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    private fun requestAllDangerousPermission(
        activity: Activity, requestCode: Int,
        allPermissions: List<String>
    ) {
        if (!isAndroid6()) {
            // 如果是 Android 6.0 以下，没有危险权限的概念，则直接回调监听
            val grantResults = IntArray(allPermissions.size)
            for (i in grantResults.indices) {
                grantResults[i] = if (isGrantedPermission(activity, allPermissions[i])) PackageManager.PERMISSION_GRANTED else PackageManager.PERMISSION_DENIED
            }
            onRequestPermissionsResult(requestCode, allPermissions.toTypedArray<String>(), grantResults)
            return
        }

        // 如果不需要的话就直接申请全部的危险权限
        requestPermissions(allPermissions.toTypedArray<String>(), requestCode)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        val arguments = arguments
        val activity: Activity? = activity
        if (activity == null || arguments == null || requestCode != arguments.getInt(REQUEST_CODE)) {
            return
        }

        // Github issue 地址：https://github.com/getActivity/XXPermissions/issues/236
        if (permissions == null || permissions.size == 0 || grantResults == null || grantResults.size == 0) {
            return
        }

        val callback = mCallBack
        // 释放监听对象的引用
        mCallBack = null

        // 释放对这个请求码的占用
        REQUEST_CODE_ARRAY.remove(requestCode)

        callback?.onRequestPermissionsResult(permissions, grantResults)

        // 将 Fragment 从 Activity 移除
        detachByActivity(activity)
    }

    companion object {
        /** 请求码（自动生成） */
        private const val REQUEST_CODE = "request_code"

        /** 权限请求码存放集合  */
        private val REQUEST_CODE_ARRAY: MutableList<Int> = ArrayList()

        /**
         * 开启权限申请
         */
        fun launch(
            activity: Activity, permissions: List<String?>,
            callback: OnRequestPermissionsResultCallback
        ) {
            val fragment = RequestDangerousPermissionFragment()
            var requestCode: Int
            // 请求码随机生成，避免随机产生之前的请求码，必须进行循环判断
            do {
                // 新版本的 Support 库限制请求码必须小于 65536
                // 旧版本的 Support 库限制请求码必须小于 256
                requestCode = Random.nextInt(256)
            } while (REQUEST_CODE_ARRAY.contains(requestCode))
            // 标记这个请求码已经被占用
            REQUEST_CODE_ARRAY.add(requestCode)

            val bundle = Bundle()
            bundle.putInt(REQUEST_CODE, requestCode)
            if (permissions is ArrayList<*>) {
                bundle.putStringArrayList(REQUEST_PERMISSIONS, permissions as ArrayList<String?>)
            } else {
                bundle.putStringArrayList(REQUEST_PERMISSIONS, ArrayList(permissions))
            }
            fragment.arguments = bundle
            // 设置保留实例，不会因为屏幕方向或配置变化而重新创建
            fragment.retainInstance = true
            // 设置权限申请标记
            fragment.setRequestFlag(true)
            // 设置权限回调监听
            fragment.setOnRequestPermissionsResultCallback(callback)
            // 绑定到 Activity 上面
            fragment.attachByActivity(activity)
        }
    }
}