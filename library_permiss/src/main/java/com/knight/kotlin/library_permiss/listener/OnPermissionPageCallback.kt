package com.knight.kotlin.library_permiss.listener

/**
 * Author:Knight
 * Time:2023/8/29 16:53
 * Description:OnPermissionPageCallback
 */
interface OnPermissionPageCallback {

    /**
     * 权限已经授予
     */
    fun onGranted()

    /**
     * 权限已经拒绝
     */
    fun onDenied() {}
}