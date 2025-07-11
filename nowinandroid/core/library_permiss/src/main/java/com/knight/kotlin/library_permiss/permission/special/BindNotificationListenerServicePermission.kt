package com.knight.kotlin.library_permiss.permission.special

import android.app.Activity
import android.app.NotificationManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Parcel
import android.os.Parcelable
import android.provider.Settings
import com.knight.kotlin.library_permiss.manifest.AndroidManifestInfo
import com.knight.kotlin.library_permiss.manifest.node.PermissionManifestInfo
import com.knight.kotlin.library_permiss.permission.PermissionNames
import com.knight.kotlin.library_permiss.permission.base.IPermission
import com.knight.kotlin.library_permiss.permission.common.SpecialPermission
import com.knight.kotlin.library_permiss.tools.PermissionUtils
import com.knight.kotlin.library_permiss.tools.PermissionVersion


/**
 * @author created by luguian
 * @organize
 * @Date 2025/7/11 8:49
 * @descript:通知栏监听权限类
 */
class BindNotificationListenerServicePermission : SpecialPermission {

    companion object {
        const val PERMISSION_NAME = PermissionNames.BIND_NOTIFICATION_LISTENER_SERVICE

        private const val SETTING_ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners"

        @JvmField
        val CREATOR: Parcelable.Creator<BindNotificationListenerServicePermission> =
            object : Parcelable.Creator<BindNotificationListenerServicePermission> {
                override fun createFromParcel(source: Parcel): BindNotificationListenerServicePermission {
                    return BindNotificationListenerServicePermission(source)
                }

                override fun newArray(size: Int): Array<BindNotificationListenerServicePermission?> {
                    return arrayOfNulls(size)
                }
            }
    }

    /** 通知监听器的 Service 类名 */
    val notificationListenerServiceClassName: String


    constructor(notificationListenerServiceClassName: String) {
        this.notificationListenerServiceClassName = notificationListenerServiceClassName
    }

    private constructor(parcel: Parcel) : this(
        requireNotNull(parcel.readString()) {
            "NotificationListenerServiceClassName must not be null"
        }
    )

    override fun writeToParcel(dest: Parcel, flags: Int) {
        super.writeToParcel(dest, flags)
        dest.writeString(notificationListenerServiceClassName)
    }

    override fun getPermissionName(): String {
        return PERMISSION_NAME
    }

    override fun getFromAndroidVersion(): Int {
        return PermissionVersion.ANDROID_4_3
    }

    override fun isGrantedPermission(context: Context, skipRequest: Boolean): Boolean {
        // Android 4.3 以前版本不支持通知监听器，直接返回 true
        if (!PermissionVersion.isAndroid4_3()) return true

        val notificationManager: NotificationManager? = if (PermissionVersion.isAndroid6()) {
            context.getSystemService(NotificationManager::class.java)
        } else {
            context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
        }

        val serviceClassName = if (PermissionUtils.isClassExist(notificationListenerServiceClassName)) {
            notificationListenerServiceClassName
        } else null

        if (PermissionVersion.isAndroid8_1() && notificationManager != null && serviceClassName != null) {
            return notificationManager.isNotificationListenerAccessGranted(
                ComponentName(context, serviceClassName)
            )
        }

        val enabledListeners = Settings.Secure.getString(
            context.contentResolver,
            SETTING_ENABLED_NOTIFICATION_LISTENERS
        ) ?: return false

        val componentArray = enabledListeners.split(":")
        for (component in componentArray) {
            val componentName = ComponentName.unflattenFromString(component) ?: continue
            if (serviceClassName != null) {
                if (serviceClassName == componentName.className) {
                    return true
                }
            } else {
                if (context.packageName == componentName.packageName) {
                    return true
                }
            }
        }

        return false
    }

    override fun getPermissionSettingIntents(context: Context): MutableList<Intent> {
        val intentList = mutableListOf<Intent>()

        if (PermissionVersion.isAndroid11() &&
            PermissionUtils.isClassExist(notificationListenerServiceClassName)
        ) {
            val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_DETAIL_SETTINGS).apply {
                putExtra(
                    Settings.EXTRA_NOTIFICATION_LISTENER_COMPONENT_NAME,
                    ComponentName(context, notificationListenerServiceClassName).flattenToString()
                )
            }
            intentList.add(intent)
        }

        val action = if (PermissionVersion.isAndroid5_1()) {
            Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS
        } else {
            "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"
        }

        intentList.add(Intent(action))
        intentList.add(getAndroidSettingIntent())

        return intentList
    }

    override fun checkCompliance(
        activity: Activity,
        requestPermissions: List<IPermission>,
        androidManifestInfo: AndroidManifestInfo
    ) {
        super.checkCompliance(activity, requestPermissions, androidManifestInfo)
        require(notificationListenerServiceClassName.isNotEmpty()) {
            "Pass the ServiceClass parameter as empty"
        }
        require(PermissionUtils.isClassExist(notificationListenerServiceClassName)) {
            "The passed-in $notificationListenerServiceClassName is an invalid class"
        }
    }

    override fun checkSelfByManifestFile(
        activity: Activity,
        requestPermissions: List<IPermission>,
        androidManifestInfo: AndroidManifestInfo,
        permissionManifestInfoList: List<PermissionManifestInfo>,
        currentPermissionManifestInfo: PermissionManifestInfo
    ) {
        super.checkSelfByManifestFile(
            activity,
            requestPermissions,
            androidManifestInfo,
            permissionManifestInfoList,
            currentPermissionManifestInfo
        )

        for (info in androidManifestInfo.serviceManifestInfoList) {
            if (info == null) continue
            if (!PermissionUtils.reverseEqualsString(notificationListenerServiceClassName, info.name)) continue

            if (info.permission == null || !PermissionUtils.equalsPermission(this, info.permission)) {
                throw IllegalArgumentException(
                    "Please register permission node in the AndroidManifest.xml file, for example: " +
                            "<service android:name=\"$notificationListenerServiceClassName\" android:permission=\"${getPermissionName()}\" />"
                )
            }
            return
        }

        throw IllegalArgumentException("The \"$notificationListenerServiceClassName\" component is not registered in the AndroidManifest.xml file")
    }

    fun getNotificationListenerServiceClassName(): String {
        return notificationListenerServiceClassName
    }
}
