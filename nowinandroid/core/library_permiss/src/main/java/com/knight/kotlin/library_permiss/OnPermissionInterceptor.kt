package com.knight.kotlin.library_permiss

import android.app.Activity
import com.knight.kotlin.library_permiss.core.RequestPermissionLogicPresenter
import com.knight.kotlin.library_permiss.fragment.factory.PermissionFragmentFactory
import com.knight.kotlin.library_permiss.permission.base.IPermission


/**
 * Author:Knight 权限请求拦截器
 * Time:2022/1/20 15:06
 * Description:OnPermissionInterceptor
 */
interface OnPermissionInterceptor {
    /**
     * 发起权限申请（可在此处先弹 Dialog 再申请权限，如果用户已经授予权限，则不会触发此回调）
     *
     * @param requestPermissions        申请的权限
     * @param callback                  权限申请回调
     */
    fun launchPermissionRequest(
         activity: Activity,
         requestPermissions: List<IPermission>,
         fragmentFactory: PermissionFragmentFactory<*, *>,
         permissionDescription: OnPermissionDescription,
         callback: OnPermissionCallback
    ) {
        dispatchPermissionRequest(activity, requestPermissions, fragmentFactory, permissionDescription, callback)
    }

    /**
     * 用户授予了权限（注意需要在此处回调 [OnPermissionCallback.onGranted]）
     *
     * @param requestPermissions         申请的权限
     * @param grantedPermissions         已授予的权限
     * @param allGranted                 是否全部授予
     * @param callback                   权限申请回调
     */
    fun grantedPermissionRequest(
         activity: Activity,
         requestPermissions: List<IPermission>,
         grantedPermissions: List<IPermission>, allGranted: Boolean,
         callback: OnPermissionCallback
    ) {
        if (callback == null) {
            return
        }
        callback.onGranted(grantedPermissions, allGranted)
    }

    /**
     * 用户拒绝了权限（注意需要在此处回调 [OnPermissionCallback.onDenied]）
     *
     * @param requestPermissions        申请的权限
     * @param deniedPermissions         已拒绝的权限
     * @param doNotAskAgain             是否勾选了不再询问选项
     * @param callback                  权限申请回调
     */
    fun deniedPermissionRequest(
         activity: Activity,  requestPermissions: List<IPermission>,
         deniedPermissions: List<IPermission>, doNotAskAgain: Boolean,
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
     * @param requestPermissions        申请的权限
     * @param skipRequest               是否跳过了申请过程
     * @param callback                  权限申请回调
     */
    fun finishPermissionRequest(
         activity: Activity,  requestPermissions: List<IPermission>,
        skipRequest: Boolean,  callback: OnPermissionCallback
    ) {
    }

    /**
     * 派发权限请求
     *
     * @param requestPermissions        申请的权限
     * @param callback                  权限申请回调
     */
    fun dispatchPermissionRequest(
         activity: Activity,  requestPermissions: List<IPermission>,
         fragmentFactory: PermissionFragmentFactory<*, *>,
         permissionDescription: OnPermissionDescription,
         callback: OnPermissionCallback
    ) {
        RequestPermissionLogicPresenter(activity, requestPermissions, fragmentFactory, this, permissionDescription, callback)
            .request()
    }
}