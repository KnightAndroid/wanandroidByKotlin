package com.knight.kotlin.library_permiss.listener


/**
 * @author created by luguian
 * @organize
 * @Date 2025/4/27 10:42
 * @descript:权限申请回调器
 */
interface OnRequestPermissionsResultCallback {
    /**
     * 权限申请回调结果
     *
     * @param permissions           权限集合
     * @param grantResults          授权结果
     */
    fun onRequestPermissionsResult(permissions: Array<String>,  grantResults: IntArray)
}