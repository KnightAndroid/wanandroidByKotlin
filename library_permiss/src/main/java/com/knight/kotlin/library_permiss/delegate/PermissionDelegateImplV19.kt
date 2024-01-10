package com.knight.kotlin.library_permiss.delegate

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.annotation.RequiresApi
import com.knight.kotlin.library_permiss.AndroidVersion
import com.knight.kotlin.library_permiss.AndroidVersion.isAndroid13
import com.knight.kotlin.library_permiss.GetInstalledAppsPermissionCompat
import com.knight.kotlin.library_permiss.NotificationPermissionCompat
import com.knight.kotlin.library_permiss.NotificationPermissionCompat.getPermissionIntent
import com.knight.kotlin.library_permiss.WindowPermissionCompat
import com.knight.kotlin.library_permiss.permissions.Permission
import com.knight.kotlin.library_permiss.utils.PermissionUtils.equalsPermission


/**
 * Author:Knight
 * Time:2023/8/30 15:43
 * Description:PermissionDelegateImplV19
 */
@RequiresApi(api = AndroidVersion.ANDROID_4_4)
open class PermissionDelegateImplV19 :PermissionDelegateImplV18() {

    override fun isGrantedPermission(context: Context,permission: String): Boolean {
        // 检测悬浮窗权限
        if (equalsPermission(permission, Permission.SYSTEM_ALERT_WINDOW)) {
            return WindowPermissionCompat.isGrantedPermission(context)
        }

        // 检查读取应用列表权限
        if (equalsPermission(permission, Permission.GET_INSTALLED_APPS)) {
            return GetInstalledAppsPermissionCompat.isGrantedPermission(context)
        }

        // 检测通知栏权限
        if (equalsPermission(permission, Permission.NOTIFICATION_SERVICE)) {
            return NotificationPermissionCompat.isGrantedPermission(context)
        }
        // 向下兼容 Android 13 新权限
        if (!isAndroid13()) {
            if (equalsPermission(permission, Permission.POST_NOTIFICATIONS)) {
                return NotificationPermissionCompat.isGrantedPermission(context)
            }
        }
        return super.isGrantedPermission(context, permission)
    }

    override fun isDoNotAskAgainPermission(
        activity: Activity,
        permission: String
    ): Boolean {
        if (equalsPermission(permission, Permission.SYSTEM_ALERT_WINDOW)) {
            return false
        }
        if (equalsPermission(permission, Permission.GET_INSTALLED_APPS)) {
            return GetInstalledAppsPermissionCompat.isDoNotAskAgainPermission(activity)
        }
        if (equalsPermission(permission, Permission.NOTIFICATION_SERVICE)) {
            return false
        }
        // 向下兼容 Android 13 新权限
        if (!isAndroid13()) {
            if (equalsPermission(permission, Permission.POST_NOTIFICATIONS)) {
                return false
            }
        }
        return super.isDoNotAskAgainPermission(activity, permission)
    }

    override fun getPermissionIntent(context: Context, permission: String): Intent? {
        if (equalsPermission(permission, Permission.SYSTEM_ALERT_WINDOW)) {
            return WindowPermissionCompat.getPermissionIntent(context)
        }
        if (equalsPermission(permission, Permission.GET_INSTALLED_APPS)) {
            return getPermissionIntent(context)
        }
        if (equalsPermission(permission, Permission.NOTIFICATION_SERVICE)) {
            return getPermissionIntent(context)
        }

        // 向下兼容 Android 13 新权限
        if (!isAndroid13()) {
            if (equalsPermission(permission, Permission.POST_NOTIFICATIONS)) {
                return getPermissionIntent(context)
            }
        }
        return super.getPermissionIntent(context, permission)
    }
}