package com.knight.kotlin.library_permiss.listener

import android.app.Activity
import android.content.Context
import android.content.Intent


/**
 * Author:Knight 权限委托接口
 * Time:2023/8/30 15:33
 * Description:PermissionDelegate
 */
interface PermissionDelegate {
    /**
     * 判断某个权限是否授予了
     */
    fun isGrantedPermission(context: Context, permission: String,skipRequest:Boolean): Boolean

    /**
     * 判断某个权限是否勾选了不在询问
     */
    fun isDoNotAskAgainPermission(
        activity: Activity,
        permission: String
    ): Boolean

    /**
     * 获取权限设置页的意图
     */
    fun getPermissionSettingIntent(context: Context, permission: String): Intent?
}