package com.knight.kotlin.library_permiss

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.provider.Settings
import com.knight.kotlin.library_permiss.AndroidVersion.isAndroid4_4
import com.knight.kotlin.library_permiss.AndroidVersion.isAndroid5
import com.knight.kotlin.library_permiss.AndroidVersion.isAndroid7
import com.knight.kotlin.library_permiss.AndroidVersion.isAndroid8
import com.knight.kotlin.library_permiss.utils.PermissionUtils.areActivityIntent
import com.knight.kotlin.library_permiss.utils.PermissionUtils.checkOpNoThrow


/**
 * Author:Knight
 * Time:2023/8/29 16:51
 * Description:NotificationPermissionCompat
 */
object NotificationPermissionCompat {

    private const val OP_POST_NOTIFICATION_FIELD_NAME = "OP_POST_NOTIFICATION"
    private const val OP_POST_NOTIFICATION_DEFAULT_VALUE = 11

    fun isGrantedPermission(context: Context): Boolean {
        if (isAndroid7()) {
            return context.getSystemService(NotificationManager::class.java)
                .areNotificationsEnabled()
        }
        return if (isAndroid4_4()) {
            checkOpNoThrow(
                context,
                OP_POST_NOTIFICATION_FIELD_NAME,
                OP_POST_NOTIFICATION_DEFAULT_VALUE
            )
        } else true
    }

    fun getPermissionIntent(context: Context): Intent{
        lateinit var intent: Intent
        if (isAndroid8()) {
            intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
            //intent.putExtra(Settings.EXTRA_CHANNEL_ID, context.getApplicationInfo().uid);
        } else if (isAndroid5()) {
            intent = Intent()
            intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
            intent.putExtra("app_package", context.packageName)
            intent.putExtra("app_uid", context.applicationInfo.uid)
        }
        if (!areActivityIntent(context, intent)) {
            intent = PermissionIntentManager.getApplicationDetailsIntent(context)
        }
        return intent
    }
}