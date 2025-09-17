package com.knight.kotlin.library_permiss.permission.special

import android.app.Activity
import android.app.NotificationManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Parcel
import android.os.Parcelable
import android.provider.Settings
import android.service.notification.NotificationListenerService
import android.text.TextUtils
import com.knight.kotlin.library_permiss.manifest.AndroidManifestInfo
import com.knight.kotlin.library_permiss.manifest.node.IntentFilterManifestInfo
import com.knight.kotlin.library_permiss.manifest.node.PermissionManifestInfo
import com.knight.kotlin.library_permiss.manifest.node.ServiceManifestInfo
import com.knight.kotlin.library_permiss.permission.PermissionNames
import com.knight.kotlin.library_permiss.permission.base.IPermission
import com.knight.kotlin.library_permiss.permission.common.SpecialPermission
import com.knight.kotlin.library_permiss.tools.PermissionUtils
import com.knight.kotlin.library_permiss.tools.PermissionVersion
import com.knight.kotlin.library_permiss.tools.PermissionVersion.isAndroid11
import com.knight.kotlin.library_permiss.tools.PermissionVersion.isAndroid4_3
import com.knight.kotlin.library_permiss.tools.PermissionVersion.isAndroid5_1
import com.knight.kotlin.library_permiss.tools.PermissionVersion.isAndroid6
import com.knight.kotlin.library_permiss.tools.PermissionVersion.isAndroid8_1


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
    val mNotificationListenerServiceClassName: String


    constructor(notificationListenerServiceClassName: String) {
        this.mNotificationListenerServiceClassName = notificationListenerServiceClassName
    }

    private constructor(parcel: Parcel) : this(
        requireNotNull(parcel.readString()) {
            "NotificationListenerServiceClassName must not be null"
        }
    )

    override fun writeToParcel(dest: Parcel, flags: Int) {
        super.writeToParcel(dest, flags)
        dest.writeString(mNotificationListenerServiceClassName)
    }

    override fun getPermissionName(): String {
        return PERMISSION_NAME
    }

    override fun getFromAndroidVersion( context: Context): Int {
        return PermissionVersion.ANDROID_4_3
    }

    override fun isGrantedPermission( context: Context, skipRequest: Boolean): Boolean {
        // 经过实践得出，通知监听权限是在 Android 4.3 才出现的，所以前面的版本统一返回 true
        if (!isAndroid4_3()) {
            return true
        }
        val notificationManager = if (isAndroid6()) {
            context.getSystemService(NotificationManager::class.java)
        } else {
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        }
        val serviceClassName = if (PermissionUtils.isClassExist(mNotificationListenerServiceClassName)) mNotificationListenerServiceClassName else null
        // 虽然这个 SystemService 永远不为空，但是不怕一万，就怕万一，开展防御性编程
        if (isAndroid8_1() && notificationManager != null && serviceClassName != null) {
            return notificationManager.isNotificationListenerAccessGranted(ComponentName(context, serviceClassName))
        }
        val enabledNotificationListeners = Settings.Secure.getString(context.contentResolver, SETTING_ENABLED_NOTIFICATION_LISTENERS)
        if (TextUtils.isEmpty(enabledNotificationListeners)) {
            return false
        }
        // com.hjq.permissions.demo/com.hjq.permissions.demo.NotificationMonitorService:com.huawei.health/com.huawei.bone.ui.setting.NotificationPushListener
        val allComponentNameArray = enabledNotificationListeners.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        for (component in allComponentNameArray) {
            val componentName = ComponentName.unflattenFromString(component) ?: continue
            if (serviceClassName != null) {
                // 精准匹配：匹配应用包名及 Service 类名
                if (context.packageName == componentName.packageName &&
                    serviceClassName == componentName.className
                ) {
                    return true
                }
            } else {
                // 模糊匹配：仅匹配应用包名
                if (context.packageName == componentName.packageName) {
                    return true
                }
            }
        }
        return false
    }

    
    override fun getPermissionSettingIntents( context: Context, skipRequest: Boolean): MutableList<Intent> {
        val intentList: MutableList<Intent> = ArrayList(3)
        var intent: Intent

        if (isAndroid11() && PermissionUtils.isClassExist(mNotificationListenerServiceClassName)) {
            intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_DETAIL_SETTINGS)
            intent.putExtra(
                Settings.EXTRA_NOTIFICATION_LISTENER_COMPONENT_NAME,
                ComponentName(context, mNotificationListenerServiceClassName).flattenToString()
            )
            intentList.add(intent)
        }
        val action = if (isAndroid5_1()) {
            Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS
        } else {
            // android.provider.Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS
            "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"
        }
        intent = Intent(action)
        intentList.add(intent)

        intent = getAndroidSettingIntent()
        intentList.add(intent)

        return intentList
    }

    override fun checkCompliance( activity: Activity,  requestList: List<IPermission>,  manifestInfo: AndroidManifestInfo) {
        super.checkCompliance(activity, requestList, manifestInfo)
        require(!TextUtils.isEmpty(mNotificationListenerServiceClassName)) { "Pass the ServiceClass parameter as empty" }
        require(PermissionUtils.isClassExist(mNotificationListenerServiceClassName)) { "The passed-in $mNotificationListenerServiceClassName is an invalid class" }
    }

    protected override fun checkSelfByManifestFile(
         activity: Activity,
         requestList: List<IPermission>,
         manifestInfo: AndroidManifestInfo,
         permissionInfoList: List<PermissionManifestInfo>,
         currentPermissionInfo: PermissionManifestInfo
    ) {
        super.checkSelfByManifestFile(activity, requestList, manifestInfo, permissionInfoList, currentPermissionInfo)

        val serviceInfoList: List<ServiceManifestInfo> = manifestInfo.serviceInfoList
        for (serviceInfo in serviceInfoList) {
            if (serviceInfo == null) {
                continue
            }

            if (!PermissionUtils.reverseEqualsString(mNotificationListenerServiceClassName, serviceInfo.name)) {
                // 不是目标的 Service，继续循环
                continue
            }

            require(!(serviceInfo.permission == null || !PermissionUtils.equalsPermission(this, serviceInfo.permission))) {
                ("Please register permission node in the AndroidManifest.xml file, for example: "
                        + "<service android:name=\"" + mNotificationListenerServiceClassName + "\" android:permission=\"" + getPermissionName() + "\" />")
            }
            val action = if (isAndroid4_3()) {
                NotificationListenerService.SERVICE_INTERFACE
            } else {
                "android.service.notification.NotificationListenerService"
            }
            // 当前是否注册了通知栏监听服务的意图
            var registeredNotificationListenerServiceAction = false
            val intentFilterInfoList: List<IntentFilterManifestInfo> = serviceInfo.intentFilterInfoList
            if (intentFilterInfoList != null) {
                for (intentFilterInfo in intentFilterInfoList) {
                    if (intentFilterInfo.actionList.contains(action)) {
                        registeredNotificationListenerServiceAction = true
                        break
                    }
                }
            }

            if (registeredNotificationListenerServiceAction) {
                // 符合要求，中断所有的循环并返回，避免走到后面的抛异常代码
                return
            }

            val xmlCode = ("\t\t<intent-filter>\n"
                    + "\t\t    <action android:name=\"" + action + "\" />\n"
                    + "\t\t</intent-filter>")
            throw IllegalArgumentException(
                """Please add an intent filter for "$mNotificationListenerServiceClassName" in the AndroidManifest.xml file.
$xmlCode"""
            )
        }

        // 这个 Service 组件没有在清单文件中注册
        throw IllegalArgumentException("The \"$mNotificationListenerServiceClassName\" component is not registered in the AndroidManifest.xml file")
    }

    
    fun getNotificationListenerServiceClassName(): String {
        return mNotificationListenerServiceClassName
    }
}
