package com.knight.kotlin.library_permiss.listener

/**
 * @Description
 * @Author knight
 * @Time 2025/6/8 22:56
 * 
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