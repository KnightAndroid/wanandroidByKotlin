package com.knight.kotlin.library_permiss.tools

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import com.knight.kotlin.library_permiss.permission.base.IPermission
import com.knight.kotlin.library_permiss.tools.PhoneRomUtils.isColorOs


/**
 * @author created by luguian
 * @organize
 * @Date 2025/7/11 9:44
 * @descript:权限设置页
 */
object PermissionSettingPage {
    /** 华为手机管家 App 包名  */
    private const val HUA_WEI_MOBILE_MANAGER_APP_PACKAGE_NAME = "com.huawei.systemmanager"

    /** 小米手机管家 App 包名  */
    private const val XiAO_MI_MOBILE_MANAGER_APP_PACKAGE_NAME = "com.miui.securitycenter"

    /** OPPO 安全中心 App 包名  */
    private const val OPPO_SAFE_CENTER_APP_PACKAGE_NAME_1 = "com.oppo.safe"
    private const val OPPO_SAFE_CENTER_APP_PACKAGE_NAME_2 = "com.color.safecenter"
    private const val OPPO_SAFE_CENTER_APP_PACKAGE_NAME_3 = "com.oplus.safecenter"

    /** vivo 安全中心 App 包名  */
    private const val VIVO_MOBILE_MANAGER_APP_PACKAGE_NAME = "com.iqoo.secure"

    /** 锤子安全中心包名  */
    private const val SMARTISAN_SECURITY_CENTER_APP_PACKAGE_NAME = "com.smartisanos.securitycenter"

    /** 锤子安全组件包名  */
    private const val SMARTISAN_SECURITY_COMPONENT_APP_PACKAGE_NAME = "com.smartisanos.security"

    /**
     * 获取三星权限设置意图
     */
    
    fun getOneUiPermissionPageIntent(context: Context): Intent {
        val intent = Intent()
        intent.setClassName("com.android.settings", "com.android.settings.Settings\$AppOpsDetailsActivity")
        val extraShowFragmentArguments = Bundle()
        extraShowFragmentArguments.putString("package", context.packageName)
        intent.putExtra(":settings:show_fragment_args", extraShowFragmentArguments)
        intent.setData(PermissionUtils.getPackageNameUri(context))
        return intent
    }

    /* ---------------------------------------------------------------------------------------- */
    /**
     * 返回华为手机管家 App 意图
     */
    
    fun getHuaWeiMobileManagerAppIntent(context: Context): List<Intent> {
        val intentList: MutableList<Intent> = ArrayList(1)

        val intent = context.packageManager.getLaunchIntentForPackage(HUA_WEI_MOBILE_MANAGER_APP_PACKAGE_NAME)
        if (intent != null) {
            intentList.add(intent)
        }
        return intentList
    }

    /**
     * 返回小米手机管家 App 意图
     */
    
    fun getXiaoMiMobileManagerAppIntent(context: Context): List<Intent> {
        val intentList: MutableList<Intent> = ArrayList(2)
        var intent: Intent?

        intent = Intent("miui.intent.action.SECURITY_CENTER")
        intentList.add(intent)

        intent = context.packageManager.getLaunchIntentForPackage(XiAO_MI_MOBILE_MANAGER_APP_PACKAGE_NAME)
        if (intent != null) {
            intentList.add(intent)
        }
        return intentList
    }

    /**
     * 获取 oppo 安全中心 App 意图
     */
    
    fun getOppoSafeCenterAppIntent(context: Context): List<Intent> {
        val intentList: MutableList<Intent> = ArrayList(3)

        var intent = context.packageManager.getLaunchIntentForPackage(OPPO_SAFE_CENTER_APP_PACKAGE_NAME_1)
        if (intent != null) {
            intentList.add(intent)
        }

        intent = context.packageManager.getLaunchIntentForPackage(OPPO_SAFE_CENTER_APP_PACKAGE_NAME_2)
        if (intent != null) {
            intentList.add(intent)
        }

        intent = context.packageManager.getLaunchIntentForPackage(OPPO_SAFE_CENTER_APP_PACKAGE_NAME_3)
        if (intent != null) {
            intentList.add(intent)
        }

        return intentList
    }

    /**
     * 获取 vivo 管家手机意图
     */
    
    fun getVivoMobileManagerAppIntent(context: Context): List<Intent> {
        val intentList: MutableList<Intent> = ArrayList(1)

        val intent = context.packageManager.getLaunchIntentForPackage(VIVO_MOBILE_MANAGER_APP_PACKAGE_NAME)
        if (intent != null) {
            intentList.add(intent)
        }
        return intentList
    }

    /**
     * 获取锤子手机安全中心 App
     */
    
    fun getSmartisanSecurityCenterAppIntent(context: Context): List<Intent> {
        val intentList: MutableList<Intent> = ArrayList(2)

        var intent = context.packageManager.getLaunchIntentForPackage(SMARTISAN_SECURITY_COMPONENT_APP_PACKAGE_NAME)
        if (intent != null) {
            intentList.add(intent)
        }

        intent = context.packageManager.getLaunchIntentForPackage(SMARTISAN_SECURITY_CENTER_APP_PACKAGE_NAME)
        if (intent != null) {
            intentList.add(intent)
        }

        return intentList
    }

    /* ---------------------------------------------------------------------------------------- */
    /**
     * 获取小米应用具体的权限设置页意图
     */
    
    fun getXiaoMiApplicationPermissionPageIntent(context: Context): Intent {
        return Intent("miui.intent.action.APP_PERM_EDITOR")
            .putExtra("extra_pkgname", context.packageName)
    }

    /**
     * 获取锤子安全中心权限设置页意图
     */
    
    fun getSmartisanPermissionPageIntent(): List<Intent> {
        val intentList: MutableList<Intent> = ArrayList(2)

        var intent = Intent(SMARTISAN_SECURITY_COMPONENT_APP_PACKAGE_NAME + ".action.PACKAGE_OVERVIEW")
        intentList.add(intent)

        intent = Intent()
        intent.setClassName(SMARTISAN_SECURITY_COMPONENT_APP_PACKAGE_NAME, SMARTISAN_SECURITY_COMPONENT_APP_PACKAGE_NAME + ".PackagesOverview")
        intentList.add(intent)

        return intentList
    }

    /* ---------------------------------------------------------------------------------------- */
    /**
     * 获取通用的权限设置页
     */

    fun getCommonPermissionSettingIntent(context: Context): MutableList<Intent> {
        return getCommonPermissionSettingIntent(context, *emptyArray())
    }

    fun getCommonPermissionSettingIntent(context: Context, vararg permissions: IPermission): MutableList<Intent> {
        val intentList = mutableListOf<Intent>()
        intentList.add(getApplicationDetailsSettingsIntent(context, *permissions))
        intentList.add(getManageApplicationSettingsIntent())
        intentList.add(getApplicationSettingsIntent())
        intentList.add(getAndroidSettingsIntent())
        return intentList
    }

    /**
     * 获取应用详情界面意图
     */
    
    fun getApplicationDetailsSettingsIntent( context: Context,  vararg permissions: IPermission?): Intent {
        val intent: Intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.setData(PermissionUtils.getPackageNameUri(context))
        if (permissions != null && permissions.size > 0 && isColorOs()) {
            // OPPO 应用权限受阻跳转优化适配：https://open.oppomobile.com/new/developmentDoc/info?id=12983
            val bundle = Bundle()
            val permissionList: List<String?> = PermissionUtils.convertPermissionList(permissions.filterNotNull())
            // 元素为受阻权限的原生权限名字符串常量
            bundle.putStringArrayList("permissionList", if (permissionList is ArrayList<*>) permissionList as ArrayList<String?> else ArrayList(permissionList))
            intent.putExtras(bundle)
            // 传入跳转优化标识
            intent.putExtra("isGetPermission", true)
        }
        return intent
    }

    /**
     * 获取管理所有应用意图
     */
    
    fun getManageApplicationSettingsIntent(): Intent {
        return Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS)
    }

    /**
     * 获取所有应用详情页意图
     */
    
    fun getApplicationSettingsIntent(): Intent {
        return Intent(Settings.ACTION_APPLICATION_SETTINGS)
    }

    /**
     * 获取系统设置意图
     */
    
    fun getAndroidSettingsIntent(): Intent {
        return Intent(Settings.ACTION_SETTINGS)
    }
}