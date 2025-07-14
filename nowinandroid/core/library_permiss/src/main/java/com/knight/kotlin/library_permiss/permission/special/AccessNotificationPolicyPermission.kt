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
import com.knight.kotlin.library_permiss.tools.PhoneRomUtils.isHarmonyOs
import com.knight.kotlin.library_permiss.tools.PhoneRomUtils.isMagicOs


/**
 * @author created by luguian
 * @organize
 * @Date 2025/7/11 8:44
 * @descript:勿扰权限类
 */
class AccessNotificationPolicyPermission : SpecialPermission {
    constructor()

    private constructor(`in`: Parcel) : super(`in`)

    
    override fun getPermissionName(): String {
        return PERMISSION_NAME
    }

    override fun getFromAndroidVersion(): Int {
        return PermissionVersion.ANDROID_6
    }

    override fun isGrantedPermission( context: Context, skipRequest: Boolean): Boolean {
        if (!PermissionVersion.isAndroid6()) {
            return true
        }
        val notificationManager = context.getSystemService(NotificationManager::class.java) ?: return false
        // 虽然这个 SystemService 永远不为空，但是不怕一万，就怕万一，开展防御性编程
        return notificationManager.isNotificationPolicyAccessGranted
    }

    
    override fun getPermissionSettingIntents( context: Context): MutableList<Intent> {
        val intentList: MutableList<Intent> = ArrayList(6)
        var intent: Intent

        // 这里解释一下，为什么要排除 HarmonyOs 和 Magic，因为用代码能检测到有这个 Intent，也能跳转过去，但是会被马上拒绝
        // 测试过了其他厂商系统及 Android 原生系统都没有这个问题，就只有鸿蒙有这个问题
        // 只因为这个 Intent 是隐藏的意图，所以就不让用，鸿蒙 2.0 和 3.0 都有这个问题
        // 别问鸿蒙 1.0 有没有问题，问就是鸿蒙一发布就 2.0 了，1.0 版本都没有问世过
        // ------------------------ 我是一条华丽的分割线 ----------------------------
        // 相关的 issue 地址：
        // 1. https://github.com/getActivity/XXPermissions/issues/190
        // 2. https://github.com/getActivity/XXPermissions/issues/233
        // 经过测试，荣耀下面这些机子都会出现加包名跳转不过去的问题
        // 荣耀 magic4 Android 13  MagicOs 7.0
        // 荣耀 80 Pro Android 12  MagicOs 7.0
        // 荣耀 X20 SE Android 11  MagicOs 4.1
        // 荣耀 Play5 Android 10  MagicOs 4.0
        if (PermissionVersion.isAndroid10() && !isHarmonyOs() && !isMagicOs()) {
            // android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_DETAIL_SETTINGS
            intent = Intent("android.settings.NOTIFICATION_POLICY_ACCESS_DETAIL_SETTINGS")
            intent.setData(getPackageNameUri(context))
            intentList.add(intent)
        }

        if (PermissionVersion.isAndroid6()) {
            intent = Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
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



    companion object {
        val PERMISSION_NAME: String = PermissionNames.ACCESS_NOTIFICATION_POLICY
        @JvmField
        val CREATOR : Parcelable.Creator<AccessNotificationPolicyPermission> =


            object : Parcelable.Creator<AccessNotificationPolicyPermission> {
                /** 当前权限名称，注意：该常量字段仅供框架内部使用，不提供给外部引用，
                 * 如果需要获取权限名称的字符串，请直接通过 [PermissionNames] 类获取
                 */

                override fun createFromParcel(source: Parcel): AccessNotificationPolicyPermission {
                    return AccessNotificationPolicyPermission(source)
                }

                override fun newArray(size: Int): Array<AccessNotificationPolicyPermission?> {
                    return arrayOfNulls(size)
                }
            }
    }

}