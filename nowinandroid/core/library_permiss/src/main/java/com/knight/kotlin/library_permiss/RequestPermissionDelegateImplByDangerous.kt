package com.knight.kotlin.library_permiss

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import com.knight.kotlin.library_permiss.AndroidVersionTools.isAndroid6
import com.knight.kotlin.library_permiss.PermissionHelper.getMaxWaitTimeByPermissions
import com.knight.kotlin.library_permiss.PermissionRequestCodeManager.releaseRequestCode
import com.knight.kotlin.library_permiss.listener.IFragmentMethod
import com.knight.kotlin.library_permiss.permissions.PermissionApi.isGrantedPermission
import com.knight.kotlin.library_permiss.utils.PermissionUtils


/**
 * @Description
 * @Author knight
 * @Time 2025/6/8 20:11
 *
 */
internal class RequestPermissionDelegateImplByDangerous( fragmentMethod: IFragmentMethod<*, *>?) :
    RequestPermissionDelegateImpl(fragmentMethod!!) {
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun startPermissionRequest(
         activity: Activity,
         permissions: List<String>,
        requestCode: Int
    ) {
        val permissionArray = permissions.toTypedArray<String>()
        if (!isAndroid6()) {
            // 如果当前系统是 Android 6.0 以下，则没有危险权限的概念，则直接回调权限监听
            val grantResults = IntArray(permissions.size)
            for (i in grantResults.indices) {
                // 这里解释一下，为什么不直接赋值 PackageManager.PERMISSION_GRANTED，而是选择动态判断
                // 这是因为要照顾一下 Permission.GET_INSTALLED_APPS 权限，这个权限还兼容了 miui 的 Android 6.0 以下的版本
                grantResults[i] = if (isGrantedPermission(
                        activity,
                        permissions[i]
                    )
                ) PackageManager.PERMISSION_GRANTED else PackageManager.PERMISSION_DENIED
            }
            onFragmentRequestPermissionsResult(requestCode, permissionArray, grantResults)
            return
        }

        // 如果不需要的话就直接申请全部的危险权限
        requestPermissions(permissionArray, requestCode)
    }

    override fun onFragmentRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        // Github issue 地址：https://github.com/getActivity/XXPermissions/issues/236
        if (permissions == null || permissions.size == 0 || grantResults == null || grantResults.size == 0) {
            return
        }

        // 如果回调中的请求码和请求时设置的请求码不一致，则证明回调有问题，则不往下执行代码
        if (requestCode != getPermissionRequestCode()) {
            return
        }

        // 释放对这个请求码的占用
        releaseRequestCode(requestCode)

        // 延迟处理权限请求的结果
        sendTask(
            { this.dispatchPermissionCallback() },
            getMaxWaitTimeByPermissions(permissions.toList()).toLong()
        )
    }

    private fun dispatchPermissionCallback() {
        if (isFragmentUnavailable()) {
            return
        }

        val activity: Activity = getActivity()
        if (PermissionUtils.isActivityUnavailable(activity)) {
            return
        }

        val callback = getCallBack()
        // 释放监听对象的引用
        setCallback(null)

        callback?.onRequestPermissionFinish()

        // 将 Fragment 从 Activity 移除
        commitDetach()
    }
}