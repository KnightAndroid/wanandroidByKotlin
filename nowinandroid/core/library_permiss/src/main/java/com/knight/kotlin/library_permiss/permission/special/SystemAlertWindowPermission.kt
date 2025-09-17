package com.knight.kotlin.library_permiss.permission.special

import android.content.Context
import android.content.Intent
import android.os.Parcel
import android.os.Parcelable
import android.provider.Settings
import com.knight.kotlin.library_permiss.permission.PermissionNames
import com.knight.kotlin.library_permiss.permission.common.SpecialPermission
import com.knight.kotlin.library_permiss.tools.PermissionSettingPage.getHuaWeiMobileManagerAppIntent
import com.knight.kotlin.library_permiss.tools.PermissionSettingPage.getOneUiPermissionPageIntent
import com.knight.kotlin.library_permiss.tools.PermissionSettingPage.getOppoSafeCenterAppIntent
import com.knight.kotlin.library_permiss.tools.PermissionSettingPage.getSmartisanPermissionPageIntent
import com.knight.kotlin.library_permiss.tools.PermissionSettingPage.getSmartisanSecurityCenterAppIntent
import com.knight.kotlin.library_permiss.tools.PermissionSettingPage.getVivoMobileManagerAppIntent
import com.knight.kotlin.library_permiss.tools.PermissionSettingPage.getXiaoMiApplicationPermissionPageIntent
import com.knight.kotlin.library_permiss.tools.PermissionSettingPage.getXiaoMiMobileManagerAppIntent
import com.knight.kotlin.library_permiss.tools.PermissionVersion
import com.knight.kotlin.library_permiss.tools.PermissionVersion.isAndroid11
import com.knight.kotlin.library_permiss.tools.PermissionVersion.isAndroid4_4
import com.knight.kotlin.library_permiss.tools.PermissionVersion.isAndroid5_1
import com.knight.kotlin.library_permiss.tools.PermissionVersion.isAndroid6


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

    override fun getFromAndroidVersion( context: Context): Int {
        // 虽然悬浮窗权限是在 Android 6.0 新增的权限，但是有些国产的厂商在 Android 6.0 之前的版本就自己加了，并且框架已经有做兼容了
        // 所以为了兼容更低的 Android 版本，这里需要将悬浮窗权限出现的 Android 版本成 API 17（即框架要求 minSdkVersion 版本）
        return PermissionVersion.ANDROID_4_2
    }

    override fun isGrantedPermission( context: Context, skipRequest: Boolean): Boolean {
        if (isAndroid6()) {
            return Settings.canDrawOverlays(context)
        }

        if (!isAndroid4_4()) {
            return true
        }

        // 经过测试在 vivo x7 Plus（Android 5.1）和 OPPO A53 （Android 5.1 ColorOS 2.1）的机子上面判断不准确
        // 经过 debug 发现并不是 vivo 和 oppo 修改了 OP_SYSTEM_ALERT_WINDOW 的赋值导致的
        // 估计是 vivo 和 oppo 的机子修改了整个悬浮窗机制，这种就没有办法了
        return checkOpPermission(context!!, OP_SYSTEM_ALERT_WINDOW_FIELD_NAME, OP_SYSTEM_ALERT_WINDOW_DEFAULT_VALUE, true)
    }

    
    override fun getPermissionSettingIntents( context: Context, skipRequest: Boolean): MutableList<Intent> {
        val intentList: MutableList<Intent> = ArrayList(7)
        var intent: Intent

        if (isAndroid6()) {
            // 如果当前系统是 HyperOS，那么就不要跳转到 MIUI 权限设置页了，因为还要点一下《其他权限》入口才能找到悬浮窗权限设置选项
            // 这样的效果还不如直接跳转到所有应用的悬浮窗权限设置列表，然后再点进去来得更直观
            // 相关 Github issue 地址：https://github.com/getActivity/XXPermissions/issues/342
            if (isAndroid11() && !DeviceOs.isHyperOs() &&
                (DeviceOs.isMiui() && DeviceOs.isMiuiOptimization())
            ) {
                // 因为 Android 11 及后面的版本无法直接跳转到具体权限设置页面，只能跳转到悬浮窗权限应用列表，十分地麻烦的，这里做了一下简化
                // MIUI 做得比较人性化的，不会出现跳转不过去的问题，其他厂商就不一定了，就是不想让你跳转过去
                intent = getXiaoMiApplicationPermissionPageIntent(context)
                intentList.add(intent)
            }

            intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
            intent.setData(getPackageNameUri(context))
            intentList.add(intent)

            // 在 Android 11 加包名跳转也是没有效果的，官方文档链接：
            // https://developer.android.google.cn/reference/android/provider/Settings#ACTION_MANAGE_OVERLAY_PERMISSION
            intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
            intentList.add(intent)
        } else {
            // 需要注意的是，这里不需要判断 HarmonyOS，因为 HarmonyOS 2.0 用代码判断是 API 等级是 29（Android 10）会直接走上面的逻辑，而不会走到下面来

            if (DeviceOs.isEmui()) {
                // EMUI 发展史：http://www.360doc.com/content/19/1017/10/9113704_867381705.shtml
                // android 华为版本历史,一文看完华为EMUI发展史：https://blog.csdn.net/weixin_39959369/article/details/117351161

                val addViewMonitorActivityIntent = Intent()
                // EMUI 3.1 的适配（华为荣耀 7 Android 5.0、华为揽阅 M2 青春版 Android 5.1、华为畅享 5S Android 5.1）
                addViewMonitorActivityIntent.setClassName("com.huawei.systemmanager", "com.huawei.systemmanager.addviewmonitor.AddViewMonitorActivity")

                val notificationManagementActivityIntent = Intent()
                // EMUI 3.0 的适配（华为麦芒 3S Android 4.4）
                notificationManagementActivityIntent.setClassName("com.huawei.systemmanager", "com.huawei.notificationmanager.ui.NotificationManagmentActivity")

                // 获取厂商版本号
                val osVersionName: String = DeviceOs.getOsVersionName()

                if (osVersionName.startsWith("3.0")) {
                    // 3.0、3.0.1
                    intentList.add(notificationManagementActivityIntent)
                    intentList.add(addViewMonitorActivityIntent)
                } else {
                    // 3.1、其他的
                    intentList.add(addViewMonitorActivityIntent)
                    intentList.add(notificationManagementActivityIntent)
                }

                // 华为手机管家主页
                intentList.addAll(getHuaWeiMobileManagerAppIntent(context))
            } else if (DeviceOs.isMiui()) {
                // 假设关闭了 MIUI 优化，就不走这里的逻辑
                // 小米手机也可以通过应用详情页开启悬浮窗权限（只不过会多一步操作）

                if (DeviceOs.isMiuiOptimization()) {
                    intent = getXiaoMiApplicationPermissionPageIntent(context)
                    intentList.add(intent)
                }

                // 小米手机管家主页
                intentList.addAll(getXiaoMiMobileManagerAppIntent(context))
            } else if (DeviceOs.isColorOs()) {
                // com.color.safecenter 是之前 oppo 安全中心的包名，而 com.oppo.safe 是 oppo 后面改的安全中心的包名
                // 经过测试发现是在 ColorOs 2.1 的时候改的，Android 4.4 还是 com.color.safecenter，到了 Android 5.0 变成了 com.oppo.safe

                // java.lang.SecurityException: Permission Denial: starting Intent
                // { cmp=com.oppo.safe/.permission.floatwindow.FloatWindowListActivity (has extras) } from
                // ProcessRecord{839a7c5 10595:com.hjq.permissions.demo/u0a3781} (pid=10595, uid=13781) not exported from uid 1000
                // intent.setClassName("com.oppo.safe", "com.oppo.safe.permission.floatwindow.FloatWindowListActivity");

                // java.lang.SecurityException: Permission Denial: starting Intent
                // { cmp=com.color.safecenter/.permission.floatwindow.FloatWindowListActivity (has extras) } from
                // ProcessRecord{42b660b0 31279:com.hjq.permissions.demo/u0a204} (pid=31279, uid=10204) not exported from uid 1000
                // intent.setClassName("com.color.safecenter", "com.color.safecenter.permission.floatwindow.FloatWindowListActivity");

                // java.lang.SecurityException: Permission Denial: starting Intent
                // { cmp=com.color.safecenter/.permission.PermissionAppAllPermissionActivity (has extras) } from
                // ProcessRecord{42c49dd8 1791:com.hjq.permissions.demo/u0a204} (pid=1791, uid=10204) not exported from uid 1000
                // intent.setClassName("com.color.safecenter", "com.color.safecenter.permission.PermissionAppAllPermissionActivity");

                // 虽然不能直接到达悬浮窗界面，但是到达它的上一级页面（权限隐私页面）还是可以的，所以做了简单的取舍
                // 测试机是 OPPO R7 Plus（Android 5.0，ColorOs 2.1）、OPPO R7s（Android 4.4，ColorOs 2.1）
                // com.oppo.safe.permission.PermissionTopActivity
                // com.oppo.safe..permission.PermissionAppListActivity
                // com.color.safecenter.permission.PermissionTopActivity

                intent = Intent()
                intent.setClassName("com.oppo.safe", "com.oppo.safe.permission.PermissionTopActivity")
                intentList.add(intent)

                intentList.addAll(getOppoSafeCenterAppIntent(context))
            } else if (DeviceOs.isFuntouchOs()) {
                // java.lang.SecurityException: Permission Denial: starting Intent
                // { cmp=com.iqoo.secure/.ui.phoneoptimize.FloatWindowManager (has extras) } from
                // ProcessRecord{2c3023cf 21847:com.hjq.permissions.demo/u0a4633} (pid=21847, uid=14633) not exported from uid 10055
                // intent.setClassName("com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.FloatWindowManager");

                // java.lang.SecurityException: Permission Denial: starting Intent
                // { cmp=com.iqoo.secure/.safeguard.PurviewTabActivity (has extras) } from
                // ProcessRecord{2c3023cf 21847:com.hjq.permissions.demo/u0a4633} (pid=21847, uid=14633) not exported from uid 10055
                // intent.setClassName("com.iqoo.secure", "com.iqoo.secure.safeguard.PurviewTabActivity");

                // 经过测试在 vivo x7 Plus（Android 5.1）上面能跳转过去，但是显示却是一个空白页面
                // intent.setClassName("com.iqoo.secure", "com.iqoo.secure.safeguard.SoftPermissionDetailActivity");

                // Vivo 手机管家主页

                intentList.addAll(getVivoMobileManagerAppIntent(context))
            } else if (DeviceOs.isOneUi()) {
                intent = getOneUiPermissionPageIntent(context)
                intentList.add(intent)
            } else if (DeviceOs.isSmartisanOs() && !isAndroid5_1()) {
                // 经过测试，锤子手机 5.1 及以上的手机的可以直接通过直接跳转到应用详情开启悬浮窗权限，但是 4.4 以下的手机就不行，需要跳转到安全中心
                intentList.addAll(getSmartisanPermissionPageIntent())
                intentList.addAll(getSmartisanSecurityCenterAppIntent(context))
            }

            // 360 第一部发布的手机是 360 N4，Android 版本是 6.0 了，所以根本不需要跳转到指定的页面开启悬浮窗权限
            // 经过测试，魅族手机 6.0 可以直接通过直接跳转到应用详情开启悬浮窗权限
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
}
