package com.knight.kotlin.library_permiss

import com.knight.kotlin.library_permiss.permission.base.IPermission



/**
 * Author:Knight 权限请求结果回调接口
 * Time:2022/1/20 15:03
 * Description:OnPermissionCallback
 */
interface OnPermissionCallback {
    /**
     * 有权限被同意授予时回调
     *
     * @param permissions           请求成功的权限组
     * @param allGranted            是否全部授予了
     */
    fun onGranted( permissions: List<IPermission>, allGranted: Boolean)

    /**
     * 有权限被拒绝授予时回调
     *
     * @param permissions            请求失败的权限组
     * @param doNotAskAgain          是否勾选了不再询问选项
     */
    fun onDenied( permissions: List<IPermission>, doNotAskAgain: Boolean) {}
}