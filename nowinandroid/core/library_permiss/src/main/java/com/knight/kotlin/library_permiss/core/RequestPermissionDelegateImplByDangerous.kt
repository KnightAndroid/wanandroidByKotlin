package com.knight.kotlin.library_permiss.core

import android.app.Activity
import com.knight.kotlin.library_permiss.AndroidVersionTools.isAndroid6
import com.knight.kotlin.library_permiss.mnger.PermissionRequestCodeManager.releaseRequestCode
import com.knight.kotlin.library_permiss.fragment.IFragmentMethod
import com.knight.kotlin.library_permiss.utils.PermissionUtils
import androidx.annotation.IntRange

/**
 * @Description
 * @Author knight
 * @Time 2025/6/8 20:11
 *
 */
class RequestPermissionDelegateImplByDangerous(fragmentMethod: IFragmentMethod<*, *>) :
    RequestPermissionDelegateImpl(fragmentMethod) {
    override fun startPermissionRequest(
        activity: Activity, permissions: List<IPermission?>?,
        @IntRange(from = 1, to = 65535) requestCode: Int
    ) {
        if (!PermissionVersion.isAndroid6()) {
            // 如果当前系统是 Android 6.0 以下，则没有危险权限的概念，则直接回调权限监听
            // 有人看到这句代码，忍不住想吐槽了，你这不是太阳能手电筒，纯纯脱裤子放屁
            // 实则不然，也有例外的情况，GET_INSTALLED_APPS 权限虽然是危险权限
            // 但是框架在 miui 上面兼容到了 Android 6.0 以下，但是由于无法调用 requestPermissions
            // 只能通过跳转 Activity 授予该权限，所以只能告诉外层权限请求失败，迫使外层跳转 Activity 来授权
            sendTask({ this.handlerPermissionCallback() }, 0)
            return
        }

        // 如果不需要的话就直接申请全部的危险权限
        requestPermissions(PermissionUtils.convertPermissionArray(permissions), requestCode)
    }

    override fun onFragmentRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>?,
        grantResults: IntArray?
    ) {
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