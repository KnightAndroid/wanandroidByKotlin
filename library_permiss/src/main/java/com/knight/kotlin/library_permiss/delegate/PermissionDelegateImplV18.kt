package com.knight.kotlin.library_permiss.delegate

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.annotation.RequiresApi
import com.knight.kotlin.library_permiss.AndroidVersion
import com.knight.kotlin.library_permiss.NotificationListenerPermissionCompat.getPermissionIntent
import com.knight.kotlin.library_permiss.NotificationListenerPermissionCompat.isGrantedPermission
import com.knight.kotlin.library_permiss.permissions.Permission
import com.knight.kotlin.library_permiss.utils.PermissionUtils.equalsPermission


/**
 * Author:Knight
 * Time:2023/8/30 15:37
 * Description:PermissionDelegateImplV18
 */
@RequiresApi(api = AndroidVersion.ANDROID_4_3)
open class PermissionDelegateImplV18 : PermissionDelegateImplV14() {

    override fun isGrantedPermission(context: Context,  permission: String): Boolean {
        // 检测通知栏监听权限
        return if (equalsPermission(
                permission, Permission.BIND_NOTIFICATION_LISTENER_SERVICE
            )
        ) {
            isGrantedPermission(context)
        } else super.isGrantedPermission(context, permission)
    }

    override fun isPermissionPermanentDenied(
        activity: Activity,
         permission: String
    ): Boolean {
        return if (equalsPermission(
                permission, Permission.BIND_NOTIFICATION_LISTENER_SERVICE
            )
        ) {
            false
        } else super.isPermissionPermanentDenied(activity, permission)
    }

    override  fun getPermissionIntent(context: Context, permission: String): Intent? {
        return if (equalsPermission(
                permission, Permission.BIND_NOTIFICATION_LISTENER_SERVICE
            )
        ) {
            getPermissionIntent(context)
        } else super.getPermissionIntent(context, permission)
    }
}