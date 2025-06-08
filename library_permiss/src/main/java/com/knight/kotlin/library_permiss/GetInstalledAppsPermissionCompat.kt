package com.knight.kotlin.library_permiss

import android.app.Activity
import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.PermissionInfo
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import com.knight.kotlin.library_permiss.AndroidVersionTools.isAndroid4_4
import com.knight.kotlin.library_permiss.AndroidVersionTools.isAndroid6
import com.knight.kotlin.library_permiss.AndroidVersionTools.isAndroid9
import com.knight.kotlin.library_permiss.permissions.Permission
import com.knight.kotlin.library_permiss.utils.PermissionUtils
import com.knight.kotlin.library_permiss.utils.PermissionUtils.checkOpNoThrow
import com.knight.kotlin.library_permiss.utils.PhoneRomUtils


/**
 * Author:Knight
 * Time:2023/8/29 16:44
 * Description:GetInstalledAppsPermissionCompat
 */
object GetInstalledAppsPermissionCompat {

    private const val MIUI_OP_GET_INSTALLED_APPS_FIELD_NAME = "OP_GET_INSTALLED_APPS"
    private const val MIUI_OP_GET_INSTALLED_APPS_DEFAULT_VALUE = 10022

    @RequiresApi(Build.VERSION_CODES.M)
    fun isGrantedPermission(context: Context): Boolean {
        if (isSupportGetInstalledAppsPermission(context)) {
            return PermissionUtils.isGrantedPermission(context, Permission.GET_INSTALLED_APPS);
        }

        if (PhoneRomUtils.isMiui() && isMiuiSupportGetInstalledAppsPermission()) {
            if (!PhoneRomUtils.isMiuiOptimization()) {
                // 如果当前没有开启 miui 优化，则直接返回 true，表示已经授权，因为在这种情况下
                // 就算跳转 miui 权限设置页，用户也授权了，用代码判断权限还是没有授予的状态
                // 所以在没有开启 miui 优化的情况下，就告诉外层已经授予了，避免外层去引导用户跳转到权限设置页
                return true;
            }
            // 经过测试发现，OP_GET_INSTALLED_APPS 是小米在 Android 6.0 才加上的，看了 Android 5.0 的 miui 并没有出现读取应用列表的权限
            return PermissionUtils.checkOpNoThrow(context, MIUI_OP_GET_INSTALLED_APPS_FIELD_NAME, MIUI_OP_GET_INSTALLED_APPS_DEFAULT_VALUE);
        }

        // 如果不支持申请，则直接返回 true（代表有这个权限），反正也不会崩溃，顶多就是获取不到第三方应用列表
        return true
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun isDoNotAskAgainPermission(activity: Activity): Boolean {
        if (isSupportGetInstalledAppsPermission(activity)) {
            // 如果支持申请，那么再去判断权限是否永久拒绝
            return PermissionUtils.isDoNotAskAgainPermission(activity, Permission.GET_INSTALLED_APPS);
        }

        if (PhoneRomUtils.isMiui() && isMiuiSupportGetInstalledAppsPermission()) {
            if (!PhoneRomUtils.isMiuiOptimization()) {
                return false;
            }
            // 如果在没有授权的情况下返回 true 表示永久拒绝，这样就能走后面的判断，让外层调用者跳转到小米定制的权限设置页面
            return !isGrantedPermission(activity);
        }

        // 如果不支持申请，则直接返回 false（代表没有永久拒绝）
        return false
    }

    fun getPermissionIntent(context: Context): Intent? {
        if (PhoneRomUtils.isMiui()) {
            var intent: Intent? = null
            if (PhoneRomUtils.isMiuiOptimization()) {
                intent = PermissionIntentManager.getMiuiPermissionPageIntent(context)
            }
            // 另外跳转到应用详情页也可以开启读取应用列表权限
//            intent = PermissionActivityIntentHandler.addSubIntentToMainIntent(
//                intent,
//                PermissionIntentManager.getApplicationDetailsIntent(context)
//            )
//
//            return intent

            // 另外跳转到应用详情页也可以开启读取应用列表权限
            intent = PermissionActivityIntentHandler.addSubIntentForMainIntent(intent, PermissionIntentManager.getApplicationDetailsIntent(context));
            return intent
        }
        return PermissionIntentManager.getApplicationDetailsIntent(context)
    }

    /**
     * 判断是否支持获取应用列表权限
     */
    @RequiresApi(api = AndroidVersionTools.ANDROID_6)
    @SuppressWarnings("deprecation")
    private fun isSupportGetInstalledAppsPermission(context: Context): Boolean {
        if (!isAndroid6()) {
            // 如果是 Android 6.0 以下，判定它是不支持的
            return false
        }
        try {
            val permissionInfo =
                context.packageManager.getPermissionInfo(Permission.GET_INSTALLED_APPS, 0)
            if (permissionInfo != null) {
                return if (isAndroid9()) {
                    permissionInfo.protection == PermissionInfo.PROTECTION_DANGEROUS
                } else {
                    (permissionInfo.protectionLevel and PermissionInfo.PROTECTION_MASK_BASE) == PermissionInfo.PROTECTION_DANGEROUS
                }
            }
        } catch (e: PackageManager.NameNotFoundException) {
            // 没有这个权限时会抛出：android.content.pm.PackageManager$NameNotFoundException: com.android.permission.GET_INSTALLED_APPS
            e.printStackTrace()
        }
        // 如果不支持申请，则直接返回 false（代表没有永久拒绝）
        return false;
        try {
            // 移动终端应用软件列表权限实施指南：http://www.taf.org.cn/upload/AssociationStandard/TTAF%20108-2022%20%E7%A7%BB%E5%8A%A8%E7%BB%88%E7%AB%AF%E5%BA%94%E7%94%A8%E8%BD%AF%E4%BB%B6%E5%88%97%E8%A1%A8%E6%9D%83%E9%99%90%E5%AE%9E%E6%96%BD%E6%8C%87%E5%8D%97.pdf
            // 这是兜底方案，因为测试了大量的机型，除了荣耀的 Magic UI 有按照这个规范去做，其他厂商（包括华为的 HarmonyOS）都没有按照这个规范去做
            // 虽然可以只用上面那种判断权限是不是危险权限的方式，但是避免不了有的手机厂商用下面的这种，所以两种都写比较好，小孩子才做选择，大人我全都要
            return Settings.Secure.getInt(
                context.contentResolver,
                "oem_installed_apps_runtime_permission_enable"
            ) === 1
        } catch (e: Settings.SettingNotFoundException) {
            // 没有这个系统属性时会抛出：android.provider.Settings$SettingNotFoundException: oem_installed_apps_runtime_permission_enable
            e.printStackTrace()
        }
        return false
    }

    /**
     * 判断当前 miui 版本是否支持申请读取应用列表权限
     */
    private fun isMiuiSupportGetInstalledAppsPermission(): Boolean {
        if (!isAndroid4_4()) {
            return true
        }
        try {
            val appOpsClass = Class.forName(AppOpsManager::class.java.name)
            appOpsClass.getDeclaredField(MIUI_OP_GET_INSTALLED_APPS_FIELD_NAME)
            // 证明有这个字段，返回 true
            return true
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        }
        return true
    }
}