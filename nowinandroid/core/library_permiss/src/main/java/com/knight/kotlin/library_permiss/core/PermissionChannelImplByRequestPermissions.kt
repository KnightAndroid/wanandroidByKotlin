package com.knight.kotlin.library_permiss.core

import android.app.Activity
import androidx.annotation.IntRange
import com.knight.kotlin.library_permiss.fragment.IFragmentMethod
import com.knight.kotlin.library_permiss.manager.AlreadyRequestPermissionsManager
import com.knight.kotlin.library_permiss.manager.PermissionRequestCodeManager.releaseRequestCode
import com.knight.kotlin.library_permiss.permission.base.IPermission
import com.knight.kotlin.library_permiss.tools.PermissionUtils
import com.knight.kotlin.library_permiss.tools.PermissionVersion.isAndroid6


/**
 * @Description 请求权限实现类（通过  {@link android.app.Activity#requestPermissions(String[], int)} 实现）
 * @Author knight
 * @Time 2025/6/8 20:11
 *
 */
class PermissionChannelImplByRequestPermissions(fragmentMethod: IFragmentMethod<*, *>) : PermissionChannelImpl(fragmentMethod) {
    protected override fun startPermissionRequest(
        activity: Activity,
        permissions: List<IPermission>,
        @IntRange(from = 1, to = 65535) requestCode: Int
    ) {
        if (!isAndroid6()) {
            // 如果当前系统是 Android 6.0 以下，则没有危险权限的概念，则直接回调权限监听
            sendTask({ this.handlerPermissionCallback() }, 0)
            return
        }

        // 如果不需要的话就直接申请全部的危险权限
        requestPermissions(PermissionUtils.convertPermissionArray(activity, permissions), requestCode)
        // 记录一下已申请过的权限（用于更加精准地判断用户是否勾选了《不再询问》）
        AlreadyRequestPermissionsManager.addAlreadyRequestPermissions(permissions)
    }

    override fun onFragmentRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        // 如果回调中的请求码和请求时设置的请求码不一致，则证明回调有问题，则不往下执行代码
        if (requestCode != getPermissionRequestCode()) {
            return
        }
        // 释放对这个请求码的占用
        releaseRequestCode(requestCode)
        // 通知权限请求回调
        notificationPermissionCallback()
    }
}