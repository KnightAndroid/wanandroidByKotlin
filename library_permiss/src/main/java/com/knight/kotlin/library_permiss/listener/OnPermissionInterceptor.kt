package com.knight.kotlin.library_permiss.listener

import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentActivity
import com.knight.kotlin.library_permiss.PermissionHandler


/**
 * Author:Knight
 * Time:2022/1/20 15:06
 * Description:OnPermissionInterceptor
 */
interface OnPermissionInterceptor {
    /**
     * 发起权限申请（可在此处先弹 Dialog 再申请权限，如果用户已经授予权限，则不会触发此回调）
     *
     * @param allPermissions            申请的权限
     * @param callback                  权限申请回调
     */
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    fun launchPermissionRequest(
        activity: FragmentActivity, allPermissions: List<String>,
       callback: OnPermissionCallback
    ) {
        PermissionHandler.request(activity, allPermissions, callback, this)
    }

    /**
     * 用户授予了权限（注意需要在此处回调 [OnPermissionCallback.onGranted]）
     *
     * @param allPermissions             申请的权限
     * @param grantedPermissions         已授予的权限
     * @param allGranted                 是否全部授予
     * @param callback                   权限申请回调
     */
    fun grantedPermissionRequest(
         activity: Activity,  allPermissions: List<String>,
         grantedPermissions: List<String>, allGranted: Boolean,
       callback: OnPermissionCallback?
    ) {
        if (callback == null) {
            return
        }
        callback.onGranted(grantedPermissions, allGranted)
    }

    /**
     * 用户拒绝了权限（注意需要在此处回调 [OnPermissionCallback.onDenied]）
     *
     * @param allPermissions            申请的权限
     * @param deniedPermissions         已拒绝的权限
     * @param doNotAskAgain             是否勾选了不再询问选项
     * @param callback                  权限申请回调
     */
    fun deniedPermissionRequest(
         activity: Activity, allPermissions: List<String>,
         deniedPermissions: List<String>, doNotAskAgain: Boolean,
         callback: OnPermissionCallback
    ) {
        if (callback == null) {
            return
        }
        callback.onDenied(deniedPermissions, doNotAskAgain)
    }


    /**
     * 权限请求完成
     *
     * @param allPermissions            申请的权限
     * @param skipRequest               是否跳过了申请过程
     * @param callback                  权限申请回调
     */
    fun finishPermissionRequest(
        activity: Activity,  allPermissions: List<String>,
        skipRequest: Boolean,  callback: OnPermissionCallback
    ) {
    }
}