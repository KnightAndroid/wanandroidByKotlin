package com.knight.kotlin.library_permiss.listener

import androidx.fragment.app.FragmentActivity
import com.knight.kotlin.library_permiss.fragment.PermissionFragment

/**
 * Author:Knight
 * Time:2022/1/20 15:06
 * Description:IPermissionInterceptor
 */
interface IPermissionInterceptor {
    /**
     * 权限申请拦截，可在此处先弹 Dialog 再申请权限
     */
    fun requestPermissions(
        activity: FragmentActivity,
        callback: OnPermissionCallback?,
        allPermissions: List<String>
    ) {
        PermissionFragment.beginRequest(activity, ArrayList(allPermissions), this, callback)
    }

    /**
     * 权限授予回调拦截，参见 [OnPermissionCallback.onGranted]
     */
    fun grantedPermissions(
        activity: FragmentActivity, allPermissions: List<String>,
        grantedPermissions: List<String>, all: Boolean,
        callback: OnPermissionCallback?
    ) {
        if (callback == null) {
            return
        }
        callback.onGranted(grantedPermissions, all)
    }

    /**
     * 权限拒绝回调拦截，参见 [OnPermissionCallback.onDenied]
     */
    fun deniedPermissions(
        activity: FragmentActivity, allPermissions: List<String>,
        deniedPermissions: List<String>, never: Boolean,
        callback: OnPermissionCallback?
    ) {
        if (callback == null) {
            return
        }
        callback.onDenied(deniedPermissions, never)
    }
}