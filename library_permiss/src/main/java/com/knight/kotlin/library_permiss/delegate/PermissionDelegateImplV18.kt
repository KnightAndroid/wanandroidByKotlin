package com.knight.kotlin.library_permiss.delegate

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.annotation.RequiresApi
import com.knight.kotlin.library_permiss.AndroidVersion
import com.knight.kotlin.library_permiss.NotificationListenerPermissionCompat
import com.knight.kotlin.library_permiss.NotificationListenerPermissionCompat.isGrantedPermission
import com.knight.kotlin.library_permiss.listener.PermissionDelegateImplBase
import com.knight.kotlin.library_permiss.permissions.Permission
import com.knight.kotlin.library_permiss.utils.PermissionUtils.equalsPermission


/**
 * Author:Knight
 * Time:2023/8/30 15:37
 * Description:PermissionDelegateImplV18
 */
@RequiresApi(api = AndroidVersion.ANDROID_4_3)
open class PermissionDelegateImplV18 : PermissionDelegateImplBase() {

    override fun isGrantedPermission(context: Context,  permission: String): Boolean {
        // 检测通知栏监听权限
        return if (equalsPermission(
                permission, Permission.BIND_NOTIFICATION_LISTENER_SERVICE
            )
        ) {
            isGrantedPermission(context)
        } else super.isGrantedPermission(context, permission)
    }

    override fun isDoNotAskAgainPermission(
        activity: Activity,
         permission: String
    ): Boolean {
        return if (equalsPermission(
                permission, Permission.BIND_NOTIFICATION_LISTENER_SERVICE
            )
        ) {
            false
        } else super.isDoNotAskAgainPermission(activity, permission)
    }

    override  fun getPermissionSettingIntent(context: Context, permission: String): Intent? {
        return if (equalsPermission(
                permission, Permission.BIND_NOTIFICATION_LISTENER_SERVICE
            )
        ) {
            NotificationListenerPermissionCompat.getPermissionIntent(context)
        } else super.getPermissionSettingIntent(context, permission)
    }
}