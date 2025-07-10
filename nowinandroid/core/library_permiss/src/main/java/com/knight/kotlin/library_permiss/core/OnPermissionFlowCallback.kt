package com.knight.kotlin.library_permiss.core

/**
 * @Description
 * @Author knight
 * @Time 2025/7/10 20:13
 *
 */
interface OnPermissionFlowCallback {
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