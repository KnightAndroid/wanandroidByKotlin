package com.knight.kotlin.library_permiss.delegate

import android.app.Activity
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.PowerManager
import android.provider.Settings
import androidx.annotation.RequiresApi
import com.knight.kotlin.library_permiss.AndroidVersionTools
import com.knight.kotlin.library_permiss.AndroidVersionTools.isAndroid10
import com.knight.kotlin.library_permiss.AndroidVersionTools.isAndroid6
import com.knight.kotlin.library_permiss.GetInstalledAppsPermissionCompat
import com.knight.kotlin.library_permiss.GetInstalledAppsPermissionCompat.isDoNotAskAgainPermission
import com.knight.kotlin.library_permiss.PermissionHelper
import com.knight.kotlin.library_permiss.WindowPermissionCompat
import com.knight.kotlin.library_permiss.permissions.Permission
import com.knight.kotlin.library_permiss.utils.PermissionUtils
import com.knight.kotlin.library_permiss.utils.PermissionUtils.areActivityIntent
import com.knight.kotlin.library_permiss.utils.PermissionUtils.equalsPermission
import com.knight.kotlin.library_permiss.utils.PermissionUtils.getPackageNameUri
import com.knight.kotlin.library_permiss.utils.PhoneRomUtils.isHarmonyOs
import com.knight.kotlin.library_permiss.utils.PhoneRomUtils.isMagicOs


/**
 * Author:Knight
 * Time:2023/8/30 15:47
 * Description:PermissionDelegateImplV23
 */
@RequiresApi(api = AndroidVersionTools.ANDROID_6)
open class PermissionDelegateImplV23 : PermissionDelegateImplV21(){
    override fun isGrantedPermission(
         context: Context,
         permission: String,
        skipRequest: Boolean
    ): Boolean {
        if (!PermissionHelper.isSpecialPermission(permission!!)) {
            // 读取应用列表权限是比较特殊的危险权限，它和其他危险权限的判断方式不太一样，所以需要放在这里来判断
            if (equalsPermission(permission, Permission.GET_INSTALLED_APPS)) {
                return GetInstalledAppsPermissionCompat.isGrantedPermission(context)
            }
            return PermissionUtils.isGrantedPermission(context, permission)
        }

        if (equalsPermission(permission, Permission.SYSTEM_ALERT_WINDOW)) {
            return WindowPermissionCompat.isGrantedPermission(context)
        }

        if (equalsPermission(permission, Permission.WRITE_SETTINGS)) {
            return isGrantedSettingPermission(context)
        }

        if (equalsPermission(permission, Permission.ACCESS_NOTIFICATION_POLICY)) {
            return isGrantedNotDisturbPermission(context)
        }

        if (equalsPermission(permission, Permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)) {
            return isGrantedIgnoreBatteryPermission(context)
        }

        // Android 6.0 及以下还有一些特殊权限需要判断
        return super.isGrantedPermission(context, permission, skipRequest)
    }

    override fun isDoNotAskAgainPermission(
         activity: Activity,
         permission: String
    ): Boolean {
        if (!PermissionHelper.isSpecialPermission(permission!!)) {
            // 读取应用列表权限是比较特殊的危险权限，它和其他危险权限的判断方式不太一样，所以需要放在这里来判断
            if (equalsPermission(permission, Permission.GET_INSTALLED_APPS)) {
                return isDoNotAskAgainPermission(activity!!)
            }
            return PermissionUtils.isDoNotAskAgainPermission(activity, permission)
        }

        if (equalsPermission(permission, Permission.SYSTEM_ALERT_WINDOW) ||
            equalsPermission(permission, Permission.WRITE_SETTINGS) ||
            equalsPermission(permission, Permission.ACCESS_NOTIFICATION_POLICY) ||
            equalsPermission(permission, Permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
        ) {
            return false
        }

        return super.isDoNotAskAgainPermission(activity!!, permission)
    }

    override fun getPermissionSettingIntent(
         context: Context,
         permission: String
    ): Intent? {
        if (equalsPermission(permission!!, Permission.GET_INSTALLED_APPS)) {
            return GetInstalledAppsPermissionCompat.getPermissionIntent(context)
        }

        if (equalsPermission(permission, Permission.SYSTEM_ALERT_WINDOW)) {
            return WindowPermissionCompat.getPermissionIntent(context)
        }

        if (equalsPermission(permission, Permission.WRITE_SETTINGS)) {
            return getSettingPermissionIntent(context)
        }

        if (equalsPermission(permission, Permission.ACCESS_NOTIFICATION_POLICY)) {
            return getNotDisturbPermissionIntent(context)
        }

        if (equalsPermission(permission, Permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)) {
            return getIgnoreBatteryPermissionIntent(context)
        }

        return super.getPermissionSettingIntent(context, permission)
    }

    /**
     * 是否有系统设置权限
     */
    private fun isGrantedSettingPermission( context: Context): Boolean {
        if (!isAndroid6()) {
            return true
        }
        return Settings.System.canWrite(context)
    }

    /**
     * 获取系统设置权限界面意图
     */
    private fun getSettingPermissionIntent( context: Context): Intent {
        if (!isAndroid6()) {
            return getApplicationDetailsIntent(context)
        }
        var intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
        intent.setData(getPackageNameUri(context))
        if (!areActivityIntent(context, intent)) {
            intent = getApplicationDetailsIntent(context)
        }
        return intent
    }

    /**
     * 是否有勿扰模式权限
     */
    private fun isGrantedNotDisturbPermission( context: Context): Boolean {
        if (!isAndroid6()) {
            return true
        }
        return context.getSystemService(NotificationManager::class.java).isNotificationPolicyAccessGranted
    }

    /**
     * 获取勿扰模式设置界面意图
     */
    private fun getNotDisturbPermissionIntent( context: Context): Intent {
        if (!isAndroid6()) {
            return getApplicationDetailsIntent(context)
        }
        var intent: Intent
        if (isAndroid10()) {
            // android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_DETAIL_SETTINGS
            intent = Intent("android.settings.NOTIFICATION_POLICY_ACCESS_DETAIL_SETTINGS")
            intent.setData(getPackageNameUri(context))

            // issue 地址：https://github.com/getActivity/XXPermissions/issues/190
            // 这里解释一下，为什么要排除鸿蒙系统，因为用代码能检测到有这个 Intent，也能跳转过去，但是会被马上拒绝
            // 测试过了其他厂商系统及 Android 原生系统都没有这个问题，就只有鸿蒙有这个问题
            // 只因为这个 Intent 是隐藏的意图，所以就不让用，鸿蒙 2.0 和 3.0 都有这个问题
            // 别问鸿蒙 1.0 有没有问题，问就是鸿蒙一发布就 2.0 了，1.0 版本都没有问世过
            // ------------------------ 我是一条华丽的分割线 ----------------------------
            // issue 地址：https://github.com/getActivity/XXPermissions/issues/233
            // 经过测试，荣耀下面这些机子都会出现加包名跳转不过去的问题
            // 荣耀 magic4 Android 13  MagicOs 7.0
            // 荣耀 80 Pro Android 12  MagicOs 7.0
            // 荣耀 X20 SE Android 11  MagicOs 4.1
            // 荣耀 Play5 Android 10  MagicOs 4.0
            if (isHarmonyOs() || isMagicOs()) {
                intent = Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
            }
        } else {
            intent = Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
        }

        if (!areActivityIntent(context, intent)) {
            intent = getApplicationDetailsIntent(context)
        }
        return intent
    }

    /**
     * 是否忽略电池优化选项
     */
    private fun isGrantedIgnoreBatteryPermission( context: Context): Boolean {
        if (!isAndroid6()) {
            return true
        }
        return context.getSystemService(PowerManager::class.java)
            .isIgnoringBatteryOptimizations(context.packageName)
    }

    /**
     * 获取电池优化选项设置界面意图
     */
    private fun getIgnoreBatteryPermissionIntent(context: Context): Intent {
        if (!isAndroid6()) {
            return getApplicationDetailsIntent(context)
        }
        var intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
        intent.setData(getPackageNameUri(context))

        if (!areActivityIntent(context, intent)) {
            intent = Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS)
        }

        if (!areActivityIntent(context, intent)) {
            intent = getApplicationDetailsIntent(context)
        }
        return intent
    }
}