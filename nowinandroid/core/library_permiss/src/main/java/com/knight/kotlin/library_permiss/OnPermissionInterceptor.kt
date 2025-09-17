package com.knight.kotlin.library_permiss

import android.app.Activity
import com.knight.kotlin.library_permiss.core.PermissionRequestMainLogic
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
     * @param requestList               请求的权限列表
     * @param fragmentFactory           权限 Fragment 工厂类
     * @param permissionDescription     权限描述器
     * @param callback                  权限申请回调
     */
    fun onRequestPermissionStart(
         activity: Activity,
         requestList: List<IPermission>,
         fragmentFactory: PermissionFragmentFactory<*, *>,
         permissionDescription: OnPermissionDescription,
         callback: OnPermissionCallback
    ) {
        dispatchPermissionRequest(activity, requestList, fragmentFactory, permissionDescription, callback)
    }

    /**
     * 权限请求完成
     *
     * @param grantedList               授予权限列表
     * @param deniedList                拒绝权限列表
     * @param skipRequest               是否跳过了申请过程
     * @param callback                  权限申请回调
     */
    fun onRequestPermissionEnd(
         activity: Activity, skipRequest: Boolean,
         requestList: List<IPermission>,
         grantedList: List<IPermission>,
         deniedList: List<IPermission>,
         callback: OnPermissionCallback
    ) {
        if (callback == null) {
            return
        }
        callback.onResult(grantedList, deniedList)
    }

    /**
     * 派发权限请求
     *
     * @param requestList               请求的权限列表
     * @param callback                  权限申请回调
     */
    fun dispatchPermissionRequest(
         activity: Activity,
         requestList: List<IPermission>,
         fragmentFactory: PermissionFragmentFactory<*, *>,
         permissionDescription: OnPermissionDescription,
         callback: OnPermissionCallback
    ) {
        PermissionRequestMainLogic(activity, requestList, fragmentFactory, this, permissionDescription, callback)
            .request()
    }
}