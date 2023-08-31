package com.knight.kotlin.library_permiss.listener

/**
 * Author:Knight
 * Time:2022/1/20 15:03
 * Description:OnPermissionCallback
 */
interface OnPermissionCallback {
    /**
     * 有权限被同意授予时回调
     *
     * @param permissions 请求成功的权限组
     * @param all         是否全部授予了
     */
     fun onGranted(permissions: List<String>, all: Boolean)

    /**
     * 有权限被拒绝授予时回调
     *
     * @param permissions 请求失败的权限组
     * @param never       是否勾选了不再询问选项
     */
    fun onDenied(permissions: List<String>,  doNotAskAgain: Boolean) {}
}