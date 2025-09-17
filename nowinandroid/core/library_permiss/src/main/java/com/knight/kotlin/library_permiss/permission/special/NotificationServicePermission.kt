package com.knight.kotlin.library_permiss.permission.special

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Parcel
import android.os.Parcelable
import android.provider.Settings
import android.text.TextUtils
import com.knight.kotlin.library_permiss.manifest.AndroidManifestInfo
import com.knight.kotlin.library_permiss.manifest.node.PermissionManifestInfo
import com.knight.kotlin.library_permiss.permission.PermissionNames
import com.knight.kotlin.library_permiss.permission.base.IPermission
import com.knight.kotlin.library_permiss.permission.common.SpecialPermission
import com.knight.kotlin.library_permiss.tools.PermissionVersion
import com.knight.kotlin.library_permiss.tools.PermissionVersion.getTargetVersion
import com.knight.kotlin.library_permiss.tools.PermissionVersion.isAndroid11
import com.knight.kotlin.library_permiss.tools.PermissionVersion.isAndroid13
import com.knight.kotlin.library_permiss.tools.PermissionVersion.isAndroid4_4
import com.knight.kotlin.library_permiss.tools.PermissionVersion.isAndroid5
import com.knight.kotlin.library_permiss.tools.PermissionVersion.isAndroid7
import com.knight.kotlin.library_permiss.tools.PermissionVersion.isAndroid8


/**
 * @author created by luguian
 * @organize
 * @Date 2025/7/11 8:52
 * @descript:通知权限类
 */
class NotificationServicePermission @JvmOverloads constructor(
    private val mChannelId: String? = null
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
        dest.writeString(mChannelId)
    }

    override fun getPermissionName(): String = PERMISSION_NAME

    override fun getFromAndroidVersion( context: Context): Int {
        return PermissionVersion.ANDROID_4_4
    }

    override fun isGrantedPermission( context: Context, skipRequest: Boolean): Boolean {
        if (!isAndroid4_4()) {
            return true
        }
        if (!isAndroid7()) {
            return checkOpPermission(context, OP_POST_NOTIFICATION_FIELD_NAME, OP_POST_NOTIFICATION_DEFAULT_VALUE, true)
        }

        val notificationManager = context.getSystemService(NotificationManager::class.java)
            ?: return checkOpPermission(context, OP_POST_NOTIFICATION_FIELD_NAME, OP_POST_NOTIFICATION_DEFAULT_VALUE, true)
        // 虽然这个 SystemService 永远不为空，但是不怕一万，就怕万一，开展防御性编程
        if (!notificationManager.areNotificationsEnabled()) {
            return false
        }
        if (TextUtils.isEmpty(mChannelId) || !isAndroid8()) {
            return true
        }
        val notificationChannel = notificationManager.getNotificationChannel(mChannelId)
        return notificationChannel != null && notificationChannel.importance != NotificationManager.IMPORTANCE_NONE
    }

    
    override fun getPermissionSettingIntents( context: Context, skipRequest: Boolean): MutableList<Intent> {
        val intentList: MutableList<Intent> = ArrayList(8)
        var intent: Intent

        if (isAndroid8()) {
            intent = Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS)
            // 添加应用的包名参数
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            var notificationChannel: NotificationChannel? = null
            // 虽然这个 SystemService 永远不为空，但是不怕一万，就怕万一，开展防御性编程
            if (notificationManager != null && !TextUtils.isEmpty(mChannelId)) {
                notificationChannel = notificationManager.getNotificationChannel(mChannelId)
            }
            // 设置通知渠道 id 参数的前提条件有两个
            // 1. 这个通知渠道还存在
            // 2. 当前授予了通知权限
            if (notificationChannel != null && notificationManager!!.areNotificationsEnabled()) {
                // 将 Action 修改成具体通知渠道的页面
                intent.setAction(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS)
                // 指定通知渠道 id
                intent.putExtra(Settings.EXTRA_CHANNEL_ID, notificationChannel.id)
                if (isAndroid11()) {
                    // 高版本会优先从会话 id 中找到对应的通知渠道，找不到再从渠道 id 上面找到对应的通知渠道
                    intent.putExtra(Settings.EXTRA_CONVERSATION_ID, notificationChannel.conversationId)
                }
                intentList.add(intent)
            }

            intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
            intentList.add(intent)
        }

        if (isAndroid5()) {
            intent = Intent("android.settings.APP_NOTIFICATION_SETTINGS")
            intent.putExtra("app_package", context.packageName)
            intent.putExtra("app_uid", context.applicationInfo.uid)
            intentList.add(intent)
        }

        if (isAndroid13()) {
            intent = Intent(Settings.ACTION_ALL_APPS_NOTIFICATION_SETTINGS)
            intentList.add(intent)
        }

        intent = getApplicationDetailsSettingIntent(context)
        intentList.add(intent)

        intent = getManageApplicationSettingIntent()
        intentList.add(intent)

        intent = getApplicationSettingIntent()
        intentList.add(intent)

        intent = getAndroidSettingIntent()
        intentList.add(intent)

        return intentList
    }


    fun getChannelId(): String {
        return mChannelId!!
    }

    protected override fun checkSelfByManifestFile(
        activity: Activity,
        requestList: List<IPermission>,
        manifestInfo: AndroidManifestInfo,
        permissionInfoList: List<PermissionManifestInfo>,
        currentPermissionInfo: PermissionManifestInfo
    ) {
        super.checkSelfByManifestFile(activity, requestList, manifestInfo, permissionInfoList, currentPermissionInfo)

        if (getTargetVersion(activity) >= PermissionVersion.ANDROID_13) {
            // 如果当前项目已经适配了 Android 13，则需要在清单文件加入 POST_NOTIFICATIONS 权限，否则会导致无法申请通知栏权限
            val postNotificationsPermission = findPermissionInfoByList(permissionInfoList, PermissionNames.POST_NOTIFICATIONS)
            checkPermissionRegistrationStatus(postNotificationsPermission!!, PermissionNames.POST_NOTIFICATIONS, PermissionManifestInfo.DEFAULT_MAX_SDK_VERSION)
        }
    }
}
