package com.knight.kotlin.library_permiss.delegate

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.annotation.RequiresApi
import com.knight.kotlin.library_permiss.AndroidVersionTools
import com.knight.kotlin.library_permiss.NotificationListenerPermissionCompat.getPermissionIntent
import com.knight.kotlin.library_permiss.NotificationListenerPermissionCompat.isGrantedPermission
import com.knight.kotlin.library_permiss.listener.PermissionDelegateImpl
import com.knight.kotlin.library_permiss.permissions.Permission
import com.knight.kotlin.library_permiss.utils.PermissionUtils


/**
 * Author:Knight
 * Time:2023/8/30 15:37
 * Description:PermissionDelegateImplV18
 */
@RequiresApi(api = AndroidVersionTools.ANDROID_4_3)
open class PermissionDelegateImplV18 : PermissionDelegateImpl() {

    override fun isGrantedPermission(
         context: Context,
         permission: String,
        skipRequest: Boolean
    ): Boolean {
        if (PermissionUtils.equalsPermission(
                permission,
                Permission.BIND_NOTIFICATION_LISTENER_SERVICE
            )
        ) {
            return isGrantedPermission(context!!)
        }

        return super.isGrantedPermission(context!!, permission!!, skipRequest)
    }

    override fun isDoNotAskAgainPermission(
         activity: Activity,
         permission: String
    ): Boolean {
        if (PermissionUtils.equalsPermission(
                permission,
                Permission.BIND_NOTIFICATION_LISTENER_SERVICE
            )
        ) {
            return false
        }

        return super.isDoNotAskAgainPermission(activity!!, permission!!)
    }

    override fun getPermissionSettingIntent(
         context: Context,
         permission: String
    ): Intent? {
        if (PermissionUtils.equalsPermission(
                permission,
                Permission.BIND_NOTIFICATION_LISTENER_SERVICE
            )
        ) {
            return getPermissionIntent(context)
        }

        return super.getPermissionSettingIntent(context!!, permission!!)
    }
}