package com.knight.kotlin.library_permiss.core

/**
 * @Description 权限 Fragment 回调
 * @Author knight
 * @Time 2025/7/10 20:13
 *
 */
interface OnPermissionFragmentCallback {
    /**
     * 权限请求时回调
     */
    fun onRequestPermissionNow() {}

    /**
     * 权限请求完成回调
     */
    fun onRequestPermissionFinish()

    /**
     * 权限请求异常回调
     */
    fun onRequestPermissionAnomaly() {}
}