package com.knight.kotlin.library_permiss.permission.special

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Parcel
import android.os.Parcelable
import android.provider.Settings
import com.knight.kotlin.library_permiss.permission.PermissionNames
import com.knight.kotlin.library_permiss.permission.common.SpecialPermission
import com.knight.kotlin.library_permiss.tools.PermissionVersion


/**
 * @author created by luguian
 * @organize
 * @Date 2025/7/11 8:52
 * @descript:通知权限类
 */
class NotificationServicePermission @JvmOverloads constructor(
    private val channelId: String? = null
) : SpecialPermission() {

    companion object {
        /** 当前权限名称，注意：该常量字段仅供框架内部使用，不提供给外部引用 */
        val PERMISSION_NAME: String = PermissionNames.NOTIFICATION_SERVICE

        private const val OP_POST_NOTIFICATION_FIELD_NAME = "OP_POST_NOTIFICATION"
        private const val OP_POST_NOTIFICATION_DEFAULT_VALUE = 11

        @JvmField
        val CREATOR: Parcelable.Creator<NotificationServicePermission> =
            object : Parcelable.Creator<NotificationServicePermission> {
                override fun createFromParcel(source: Parcel): NotificationServicePermission {
                    return NotificationServicePermission(source)
                }

                override fun newArray(size: Int): Array<NotificationServicePermission?> {
                    return arrayOfNulls(size)
                }
            }
    }

    private constructor(parcel: Parcel) : this(parcel.readString())

    override fun writeToParcel(dest: Parcel, flags: Int) {
        super.writeToParcel(dest, flags)
        dest.writeString(channelId)
    }

    override fun getPermissionName(): String = PERMISSION_NAME

    override fun getFromAndroidVersion(): Int = PermissionVersion.ANDROID_4_4

    override fun isGrantedPermission(context: Context, skipRequest: Boolean): Boolean {
        if (!PermissionVersion.isAndroid4_4()) return true

        if (!PermissionVersion.isAndroid7()) {
            return checkOpPermission(
                context,
                OP_POST_NOTIFICATION_FIELD_NAME,
                OP_POST_NOTIFICATION_DEFAULT_VALUE,
                true
            )
        }

        val notificationManager = context.getSystemService(NotificationManager::class.java)
        if (notificationManager == null) {
            return checkOpPermission(
                context,
                OP_POST_NOTIFICATION_FIELD_NAME,
                OP_POST_NOTIFICATION_DEFAULT_VALUE,
                true
            )
        }

        if (!notificationManager.areNotificationsEnabled()) {
            return false
        }

        if (channelId.isNullOrEmpty() || !PermissionVersion.isAndroid8()) {
            return true
        }

        val notificationChannel = notificationManager.getNotificationChannel(channelId)
        return notificationChannel != null &&
                notificationChannel.importance != NotificationManager.IMPORTANCE_NONE
    }

    override fun getPermissionSettingIntents(context: Context):MutableList<Intent> {
        val intentList = ArrayList<Intent>(8)

        if (PermissionVersion.isAndroid8()) {
            var intent = Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS).apply {
                putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
            }

            val notificationManager = context.getSystemService(NotificationManager::class.java)
            val notificationChannel = if (!channelId.isNullOrEmpty() && notificationManager != null)
                notificationManager.getNotificationChannel(channelId)
            else null

            if (notificationChannel != null && notificationManager.areNotificationsEnabled()) {
                intent = Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS).apply {
                    putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                    putExtra(Settings.EXTRA_CHANNEL_ID, notificationChannel.id)
                    if (PermissionVersion.isAndroid11()) {
                        putExtra(Settings.EXTRA_CONVERSATION_ID, notificationChannel.conversationId)
                    }
                }
                intentList.add(intent)
            }

            intentList.add(Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
            })
        }

        if (PermissionVersion.isAndroid5()) {
            intentList.add(Intent("android.settings.APP_NOTIFICATION_SETTINGS").apply {
                putExtra("app_package", context.packageName)
                putExtra("app_uid", context.applicationInfo.uid)
            })
        }

        if (PermissionVersion.isAndroid13()) {
            intentList.add(Intent(Settings.ACTION_ALL_APPS_NOTIFICATION_SETTINGS))
        }

        intentList.add(getApplicationDetailsSettingIntent(context))
        intentList.add(getManageApplicationSettingIntent())
        intentList.add(getApplicationSettingIntent())
        intentList.add(getAndroidSettingIntent())

        return intentList
    }

    fun getChannelId(): String? = channelId
}
