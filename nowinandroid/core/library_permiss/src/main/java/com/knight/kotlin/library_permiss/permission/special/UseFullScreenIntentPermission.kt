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
import com.core.library_devicecompat.DeviceOs
import com.knight.kotlin.library_permiss.permission.PermissionNames
import com.knight.kotlin.library_permiss.permission.base.IPermission
import com.knight.kotlin.library_permiss.permission.common.SpecialPermission
import com.knight.kotlin.library_permiss.tools.PermissionUtils
import com.knight.kotlin.library_permiss.tools.PermissionVersion
import com.knight.kotlin.library_permiss.tools.PermissionVersion.isAndroid14
import com.knight.kotlin.library_permiss.tools.PermissionVersion.isAndroid8


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

    override fun getFromAndroidVersion( context: Context): Int {
        return PermissionVersion.ANDROID_14
    }

    override fun isGrantedPermission( context: Context, skipRequest: Boolean): Boolean {
        if (!isAndroid14()) {
            return true
        }
        val notificationManager = context.getSystemService(NotificationManager::class.java) ?: return false
        // 虽然这个 SystemService 永远不为空，但是不怕一万，就怕万一，开展防御性编程
        return notificationManager.canUseFullScreenIntent()
    }

    
    override fun getPermissionSettingIntents( context: Context, skipRequest: Boolean): MutableList<Intent> {
        val intentList: MutableList<Intent> = ArrayList(6)
        var intent: Intent

        if (isAndroid14()) {
            intent = Intent(Settings.ACTION_MANAGE_APP_USE_FULL_SCREEN_INTENT)
            intent.setData(getPackageNameUri(context))
            intentList.add(intent)
        }

        // 经过测试，MIUI 和 HyperOS 不支持在通知界面设置全屏通知权限的，但是 Android 原生是可以的
        if (DeviceOs.isHyperOs() || DeviceOs.isMiui()) {
            intent = getAndroidSettingIntent()
            intentList.add(intent)
            return intentList
        }

        if (isAndroid8()) {
            intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
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

    override fun isRegisterPermissionByManifestFile(): Boolean {
        // 表示当前权限需要在 AndroidManifest.xml 文件中进行静态注册
        return true
    }

    protected override fun checkSelfByRequestPermissions( activity: Activity,  requestList: List<IPermission>) {
        super.checkSelfByRequestPermissions(activity, requestList)
        // 全屏通知权限需要通知权限一起使用（NOTIFICATION_SERVICE 或者 POST_NOTIFICATIONS）
        require(
            !(!PermissionUtils.containsPermission(requestList, PermissionNames.NOTIFICATION_SERVICE) &&
                    !PermissionUtils.containsPermission(requestList, PermissionNames.POST_NOTIFICATIONS))
        ) {
            ("The \"" + getPermissionName() + "\" needs to be used together with the notification permission. "
                    + "(\"" + PermissionNames.NOTIFICATION_SERVICE + "\" or \"" + PermissionNames.POST_NOTIFICATIONS + "\")")
        }

        var thisPermissionIndex = -1
        var notificationServicePermissionIndex = -1
        var postNotificationsPermissionIndex = -1
        for (i in requestList.indices) {
            val permission = requestList[i]
            if (PermissionUtils.equalsPermission(permission!!, getPermissionName())) {
                thisPermissionIndex = i
            } else if (PermissionUtils.equalsPermission(permission, PermissionNames.NOTIFICATION_SERVICE)) {
                notificationServicePermissionIndex = i
            } else if (PermissionUtils.equalsPermission(permission, PermissionNames.POST_NOTIFICATIONS)) {
                postNotificationsPermissionIndex = i
            }
        }

        require(!(notificationServicePermissionIndex != -1 && notificationServicePermissionIndex > thisPermissionIndex)) {
            "Please place the " + getPermissionName() +
                    "\" permission after the \"" + PermissionNames.NOTIFICATION_SERVICE + "\" permission"
        }

        require(!(postNotificationsPermissionIndex != -1 && postNotificationsPermissionIndex > thisPermissionIndex)) {
            "Please place the \"" + getPermissionName() +
                    "\" permission after the \"" + PermissionNames.POST_NOTIFICATIONS + "\" permission"
        }
    }
}
