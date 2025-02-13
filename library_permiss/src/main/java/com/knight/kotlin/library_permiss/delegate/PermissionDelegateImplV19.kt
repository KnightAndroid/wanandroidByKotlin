package com.knight.kotlin.library_permiss.delegate

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.annotation.RequiresApi
import com.knight.kotlin.library_permiss.AndroidVersion
import com.knight.kotlin.library_permiss.NotificationPermissionCompat
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

        if (equalsPermission(permission, Permission.NOTIFICATION_SERVICE)) {
            return NotificationPermissionCompat.isGrantedPermission(context);
        }

        return super.isGrantedPermission(context, permission);

    }

    override fun isDoNotAskAgainPermission(
        activity: Activity,
        permission: String
    ): Boolean {
        if (equalsPermission(permission, Permission.NOTIFICATION_SERVICE)) {
            return false;
        }

        return super.isDoNotAskAgainPermission(activity, permission);
    }

    override fun getPermissionSettingIntent(context: Context, permission: String): Intent? {
        if (equalsPermission(permission, Permission.NOTIFICATION_SERVICE)) {
            return NotificationPermissionCompat.getPermissionIntent(context);
        }

        return super.getPermissionSettingIntent(context, permission);
    }
}