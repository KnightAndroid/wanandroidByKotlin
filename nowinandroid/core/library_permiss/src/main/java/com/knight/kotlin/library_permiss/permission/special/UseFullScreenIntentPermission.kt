package com.knight.kotlin.library_permiss.permission.special


/**
 * @author created by luguian
 * @organize
 * @Date 2025/7/11 9:00
 * @descript:全屏通知权限类
 */
import android.app.Activity
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Parcel
import android.os.Parcelable
import android.provider.Settings
import com.knight.kotlin.library_permiss.permission.PermissionNames
import com.knight.kotlin.library_permiss.permission.base.IPermission
import com.knight.kotlin.library_permiss.permission.common.SpecialPermission
import com.knight.kotlin.library_permiss.tools.PermissionUtils
import com.knight.kotlin.library_permiss.tools.PermissionVersion
import com.knight.kotlin.library_permiss.tools.PhoneRomUtils

class UseFullScreenIntentPermission : SpecialPermission {

    companion object {
        /** 当前权限名称，注意：该常量字段仅供框架内部使用，不提供给外部引用，如果需要获取权限名称的字符串，请直接通过 [PermissionNames] 类获取 */
        @JvmField
        val PERMISSION_NAME: String = PermissionNames.USE_FULL_SCREEN_INTENT

        @JvmField
        val CREATOR: Parcelable.Creator<UseFullScreenIntentPermission> = object : Parcelable.Creator<UseFullScreenIntentPermission> {
            override fun createFromParcel(source: Parcel): UseFullScreenIntentPermission {
                return UseFullScreenIntentPermission(source)
            }

            override fun newArray(size: Int): Array<UseFullScreenIntentPermission?> {
                return arrayOfNulls(size)
            }
        }
    }
    constructor() : super()
    private constructor(parcel: Parcel) : super(parcel)

    
    override fun getPermissionName(): String = PERMISSION_NAME

    override fun getFromAndroidVersion(): Int = PermissionVersion.ANDROID_14

    override fun isGrantedPermission(context: Context, skipRequest: Boolean): Boolean {
        if (!PermissionVersion.isAndroid14()) {
            return true
        }
        val notificationManager = context.getSystemService(NotificationManager::class.java)
        // 虽然这个 SystemService 永远不为空，但是不怕一万，就怕万一，开展防御性编程
        return notificationManager?.canUseFullScreenIntent() ?: false
    }

    
    override fun getPermissionSettingIntents( context: Context): MutableList<Intent> {
        val intentList = ArrayList<Intent>(6)

        if (PermissionVersion.isAndroid14()) {
            Intent(Settings.ACTION_MANAGE_APP_USE_FULL_SCREEN_INTENT).apply {
                data = getPackageNameUri(context)
                intentList.add(this)
            }
        }

        // 经过测试，miui 和 Hyper 不支持在通知界面设置全屏通知权限的，但是 Android 原生是可以的
        if (PhoneRomUtils.isHyperOs() || PhoneRomUtils.isMiui()) {
            intentList.add(getAndroidSettingIntent())
            return intentList
        }

        if (PermissionVersion.isAndroid8()) {
            Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                intentList.add(this)
            }
        }

        intentList.add(getApplicationDetailsSettingIntent(context))
        intentList.add(getManageApplicationSettingIntent())
        intentList.add(getApplicationSettingIntent())
        intentList.add(getAndroidSettingIntent())

        return intentList
    }

    override fun isRegisterPermissionByManifestFile(): Boolean = true

    override fun checkSelfByRequestPermissions(
         activity: Activity,
         requestPermissions: List<IPermission>
    ) {
        super.checkSelfByRequestPermissions(activity, requestPermissions)

        if (!PermissionUtils.containsPermission(requestPermissions, PermissionNames.NOTIFICATION_SERVICE) &&
            !PermissionUtils.containsPermission(requestPermissions, PermissionNames.POST_NOTIFICATIONS)
        ) {
            throw IllegalArgumentException(
                "The \"${getPermissionName()}\" needs to be used together with the notification permission. " +
                        "(\"${PermissionNames.NOTIFICATION_SERVICE}\" or \"${PermissionNames.POST_NOTIFICATIONS}\")"
            )
        }

        var thisPermissionIndex = -1
        var notificationServicePermissionIndex = -1
        var postNotificationsPermissionIndex = -1

        requestPermissions.forEachIndexed { i, permission ->
            when {
                PermissionUtils.equalsPermission(permission, getPermissionName()) -> thisPermissionIndex = i
                PermissionUtils.equalsPermission(permission, PermissionNames.NOTIFICATION_SERVICE) -> notificationServicePermissionIndex = i
                PermissionUtils.equalsPermission(permission, PermissionNames.POST_NOTIFICATIONS) -> postNotificationsPermissionIndex = i
            }
        }

        if (notificationServicePermissionIndex != -1 && notificationServicePermissionIndex > thisPermissionIndex) {
            throw IllegalArgumentException(
                "Please place the \"${getPermissionName()}\" permission after the \"${PermissionNames.NOTIFICATION_SERVICE}\" permission"
            )
        }

        if (postNotificationsPermissionIndex != -1 && postNotificationsPermissionIndex > thisPermissionIndex) {
            throw IllegalArgumentException(
                "Please place the \"${getPermissionName()}\" permission after the \"${PermissionNames.POST_NOTIFICATIONS}\" permission"
            )
        }
    }
}
