package com.knight.kotlin.library_permiss.listener

/**
 * @Description 权限请求流程回调
 * @Author knight
 * @Time 2025/6/8 17:07
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