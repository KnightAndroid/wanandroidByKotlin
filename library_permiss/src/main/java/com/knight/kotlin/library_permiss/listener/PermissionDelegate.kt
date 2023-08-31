package com.knight.kotlin.library_permiss.listener

import android.app.Activity
import android.content.Context
import android.content.Intent


/**
 * Author:Knight
 * Time:2023/8/30 15:33
 * Description:PermissionDelegate
 */
interface PermissionDelegate {
    /**
     * 判断某个权限是否授予了
     */
    fun isGrantedPermission(context: Context, permission: String): Boolean

    /**
     * 判断某个权限是否永久拒绝了
     */
    fun isPermissionPermanentDenied(
        activity: Activity,
        permission: String
    ): Boolean

    /**
     * 获取权限设置页的意图
     */
    fun getPermissionIntent(context: Context, permission: String): Intent?
}