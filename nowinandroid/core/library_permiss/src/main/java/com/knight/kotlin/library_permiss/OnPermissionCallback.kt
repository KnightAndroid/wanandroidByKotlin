package com.knight.kotlin.library_permiss

import com.knight.kotlin.library_permiss.permission.base.IPermission


/**
 * Author:Knight 权限请求结果回调接口
 * Time:2022/1/20 15:03
 * Description:OnPermissionCallback
 */
interface OnPermissionCallback {
    /**
     * 权限请求结果回调
     *
     * @param grantedList               授予权限列表
     * @param deniedList                拒绝权限列表
     */
    fun onResult(grantedList: List<IPermission>, deniedList: List<IPermission>)
}