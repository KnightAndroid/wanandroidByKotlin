package com.knight.kotlin.library_permiss.permission.special

import android.content.Context
import android.content.Intent
import android.os.Parcel
import android.os.Parcelable
import android.provider.Settings
import com.knight.kotlin.library_permiss.permission.PermissionNames
import com.knight.kotlin.library_permiss.permission.common.SpecialPermission
import com.knight.kotlin.library_permiss.tools.PermissionSettingPage
import com.knight.kotlin.library_permiss.tools.PermissionVersion
import com.knight.kotlin.library_permiss.tools.PhoneRomUtils


/**
 * @author created by luguian
 * @organize
 * @Date 2025/7/11 8:59
 * @descript:悬浮窗权限类
 */
class SystemAlertWindowPermission : SpecialPermission {

    companion object {
        const val PERMISSION_NAME = PermissionNames.SYSTEM_ALERT_WINDOW

        @JvmField
        val CREATOR = object : Parcelable.Creator<SystemAlertWindowPermission> {
            override fun createFromParcel(source: Parcel): SystemAlertWindowPermission {
                return SystemAlertWindowPermission(source)
            }

            override fun newArray(size: Int): Array<SystemAlertWindowPermission?> {
                return arrayOfNulls(size)
            }
        }

        private const val OP_SYSTEM_ALERT_WINDOW_FIELD_NAME = "OP_SYSTEM_ALERT_WINDOW"
        private const val OP_SYSTEM_ALERT_WINDOW_DEFAULT_VALUE = 24
    }
    constructor() : super()
    private constructor(parcel: Parcel) : super(parcel)

    override fun getPermissionName(): String = PERMISSION_NAME

    override fun getFromAndroidVersion(): Int {
        // 虽然悬浮窗权限是在 Android 6.0 新增的权限，但是有些国产的厂商在 Android 6.0 之前的版本就自己加了，并且框架已经有做兼容了
        // 所以为了兼容更低的 Android 版本，这里需要将悬浮窗权限出现的 Android 版本成 API 17（即框架要求 minSdkVersion 版本）
        return PermissionVersion.ANDROID_4_2
    }

    override fun isGrantedPermission(context: Context, skipRequest: Boolean): Boolean {
        if (PermissionVersion.isAndroid6()) {
            return Settings.canDrawOverlays(context)
        }

        if (!PermissionVersion.isAndroid4_4()) {
            return true
        }

        // 经过测试在 vivo x7 Plus（Android 5.1）和 OPPO A53 （Android 5.1 ColorOs 2.1）的机子上面判断不准确
        // 经过 debug 发现并不是 vivo 和 oppo 修改了 OP_SYSTEM_ALERT_WINDOW 的赋值导致的
        // 估计是 vivo 和 oppo 的机子修改了整个悬浮窗机制，这种就没有办法了
        return checkOpPermission(context, OP_SYSTEM_ALERT_WINDOW_FIELD_NAME, OP_SYSTEM_ALERT_WINDOW_DEFAULT_VALUE, true)
    }

    override fun getPermissionSettingIntents(context: Context): MutableList<Intent> {
        val intentList = ArrayList<Intent>(7)

        if (PermissionVersion.isAndroid6()) {
            // 如果当前系统是 HyperOs，那么就不要跳转到 miui 权限设置页了，因为还要点一下《其他权限》入口才能找到悬浮窗权限设置选项
            // 这样的效果还不如直接跳转到所有应用的悬浮窗权限设置列表，然后再点进去来得更直观
            // 相关 Github issue 地址：https://github.com/getActivity/XXPermissions/issues/342
            if (PermissionVersion.isAndroid11() && !PhoneRomUtils.isHyperOs() &&
                PhoneRomUtils.isMiui() && PhoneRomUtils.isXiaomiSystemOptimization()) {
                // 因为 Android 11 及后面的版本无法直接跳转到具体权限设置页面，只能跳转到悬浮窗权限应用列表，十分地麻烦的，这里做了一下简化
                // miui 做得比较人性化的，不会出现跳转不过去的问题，其他厂商就不一定了，就是不想让你跳转过去
                val intent = PermissionSettingPage.getXiaoMiApplicationPermissionPageIntent(context)
                intentList.add(intent)
            }

            var intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION).apply {
                data = getPackageNameUri(context)
            }
            intentList.add(intent)

            // 在 Android 11 加包名跳转也是没有效果的，官方文档链接：
            // https://developer.android.google.cn/reference/android/provider/Settings#ACTION_MANAGE_OVERLAY_PERMISSION
            intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
            intentList.add(intent)

        } else {
            // 下面是不同厂商的特殊适配逻辑，保持原有注释
            if (PhoneRomUtils.isEmui()) {
                val addViewMonitorActivityIntent = Intent().apply {
                    setClassName("com.huawei.systemmanager", "com.huawei.systemmanager.addviewmonitor.AddViewMonitorActivity")
                }

                val notificationManagementActivityIntent = Intent().apply {
                    setClassName("com.huawei.systemmanager", "com.huawei.systemmanager.notificationmanager.ui.NotificationManagmentActivity")
                }

                var romVersionName = PhoneRomUtils.getRomVersionName() ?: ""

                if (romVersionName.startsWith("3.0")) {
                    intentList.add(notificationManagementActivityIntent)
                    intentList.add(addViewMonitorActivityIntent)
                } else {
                    intentList.add(addViewMonitorActivityIntent)
                    intentList.add(notificationManagementActivityIntent)
                }

                intentList.addAll(PermissionSettingPage.getHuaWeiMobileManagerAppIntent(context))

            } else if (PhoneRomUtils.isMiui()) {
                if (PhoneRomUtils.isXiaomiSystemOptimization()) {
                    val intent = PermissionSettingPage.getXiaoMiApplicationPermissionPageIntent(context)
                    intentList.add(intent)

                    intentList.addAll(PermissionSettingPage.getXiaoMiMobileManagerAppIntent(context))
                }

            } else if (PhoneRomUtils.isColorOs()) {
                val intent = Intent().apply {
                    setClassName("com.oppo.safe", "com.oppo.safe.permission.PermissionTopActivity")
                }
                intentList.add(intent)

                intentList.addAll(PermissionSettingPage.getOppoSafeCenterAppIntent(context))

            } else if (PhoneRomUtils.isOriginOs()) {
                intentList.addAll(PermissionSettingPage.getVivoMobileManagerAppIntent(context))

            } else if (PhoneRomUtils.isOneUi()) {
                val intent = PermissionSettingPage.getOneUiPermissionPageIntent(context)
                intentList.add(intent)

            } else if (PhoneRomUtils.isSmartisanOS() && !PermissionVersion.isAndroid5_1()) {
                intentList.addAll(PermissionSettingPage.getSmartisanPermissionPageIntent())
                intentList.addAll(PermissionSettingPage.getSmartisanSecurityCenterAppIntent(context))
            }
            // 360、魅族等厂商无需特殊跳转处理，直接走应用详情页即可
        }

        intentList.add(getApplicationDetailsSettingIntent(context))
        intentList.add(getManageApplicationSettingIntent())
        intentList.add(getApplicationSettingIntent())
        intentList.add(getAndroidSettingIntent())

        return intentList
    }

    override fun isRegisterPermissionByManifestFile(): Boolean {
        // 表示当前权限需要在 AndroidManifest.xml 文件中进行静态注册
        return true
    }
}
