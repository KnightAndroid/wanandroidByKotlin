package com.knight.kotlin.library_permiss

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import com.knight.kotlin.library_permiss.PermissionHelper.getMaxWaitTimeByPermissions
import com.knight.kotlin.library_permiss.PermissionRequestCodeManager.releaseRequestCode
import com.knight.kotlin.library_permiss.listener.IFragmentMethod
import com.knight.kotlin.library_permiss.permissions.PermissionApi.getBestPermissionSettingIntent
import com.knight.kotlin.library_permiss.utils.PermissionUtils


/**
 * @Description
 * @Author knight
 * @Time 2025/6/8 20:17
 *
 */
internal class RequestPermissionDelegateImplBySpecial( fragmentMethod: IFragmentMethod<*, *>?) :
    RequestPermissionDelegateImpl(fragmentMethod!!) {
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun startPermissionRequest(
        activity: Activity,
        permissions: List<String>,
        requestCode: Int
    ) {
        PermissionActivityIntentHandler.startActivityForResult(
            getStartActivityDelegate(),
            getBestPermissionSettingIntent(activity, permissions), requestCode
        )
    }

    override fun onFragmentActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onFragmentActivityResult(requestCode, resultCode, data)

        // 如果回调中的请求码和请求时设置的请求码不一致，则证明回调有问题，则不往下执行代码
        if (requestCode != getPermissionRequestCode()) {
            return
        }

        // 释放对这个请求码的占用
        releaseRequestCode(requestCode)

        val permissions = getPermissionRequestList()
        if (permissions == null || permissions.isEmpty()) {
            return
        }

        // 延迟处理权限请求的结果
        sendTask(
            { this.dispatchPermissionCallback() },
            getMaxWaitTimeByPermissions(permissions).toLong()
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

        commitDetach()
    }
}