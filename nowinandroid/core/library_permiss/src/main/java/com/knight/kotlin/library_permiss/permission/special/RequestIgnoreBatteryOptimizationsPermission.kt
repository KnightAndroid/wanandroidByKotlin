package com.knight.kotlin.library_permiss.permission.special

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Parcel
import android.os.Parcelable
import android.os.PowerManager
import android.provider.Settings
import com.core.library_devicecompat.DeviceOs
import com.knight.kotlin.library_permiss.permission.PermissionNames
import com.knight.kotlin.library_permiss.permission.PermissionPageType
import com.knight.kotlin.library_permiss.permission.common.SpecialPermission
import com.knight.kotlin.library_permiss.tools.PermissionUtils
import com.knight.kotlin.library_permiss.tools.PermissionVersion
import com.knight.kotlin.library_permiss.tools.PermissionVersion.isAndroid11
import com.knight.kotlin.library_permiss.tools.PermissionVersion.isAndroid12
import com.knight.kotlin.library_permiss.tools.PermissionVersion.isAndroid14
import com.knight.kotlin.library_permiss.tools.PermissionVersion.isAndroid15
import com.knight.kotlin.library_permiss.tools.PermissionVersion.isAndroid6


/**
 * @author created by luguian
 * @organize
 * @Date 2025/7/11 8:56
 * @descript:请求忽略电池优化选项权限类
 */
class RequestIgnoreBatteryOptimizationsPermission : SpecialPermission {

    companion object {
        const val PERMISSION_NAME = PermissionNames.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS

        @JvmField
        val CREATOR = object : Parcelable.Creator<RequestIgnoreBatteryOptimizationsPermission> {
            override fun createFromParcel(source: Parcel): RequestIgnoreBatteryOptimizationsPermission {
                return RequestIgnoreBatteryOptimizationsPermission(source)
            }

            override fun newArray(size: Int): Array<RequestIgnoreBatteryOptimizationsPermission?> {
                return arrayOfNulls(size)
            }
        }
    }
    constructor() : super()
    private constructor(parcel: Parcel) : super(parcel)

    override fun getPermissionName(): String = PERMISSION_NAME

    @SuppressLint("BatteryLife")
    
    override fun getPermissionPageType( context: Context): PermissionPageType {
        // 因为在 Android 10 的时候，这个特殊权限弹出的页面小米还是用谷歌原生的
        // 然而在 Android 11 之后的，这个权限页面被小米改成了自己定制化的页面
        if (isAndroid11() && (DeviceOs.isHyperOs() || DeviceOs.isMiui())) {
            return PermissionPageType.OPAQUE_ACTIVITY
        }
        // 请求忽略电池优化选项权限在 Android 15 及以上版本的 OPPO 系统上面是一个不透明的 Activity 页面
        if (DeviceOs.isColorOs() && isAndroid15()) {
            return PermissionPageType.OPAQUE_ACTIVITY
        }
        if (isAndroid6() && !isGrantedPermission(context)) {
            val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
            intent.setData(getPackageNameUri(context))
            if (PermissionUtils.areActivityIntent(context, intent)) {
                return PermissionPageType.TRANSPARENT_ACTIVITY
            }
        }
        return PermissionPageType.OPAQUE_ACTIVITY
    }

    override fun getFromAndroidVersion( context: Context): Int {
        return PermissionVersion.ANDROID_6
    }

    override fun isGrantedPermission( context: Context, skipRequest: Boolean): Boolean {
        if (!isAndroid6()) {
            return true
        }
        val powerManager = context.getSystemService(PowerManager::class.java) ?: return false
        // 虽然这个 SystemService 永远不为空，但是不怕一万，就怕万一，开展防御性编程
        return powerManager.isIgnoringBatteryOptimizations(context.packageName)
    }

    @SuppressLint("BatteryLife")

    override fun getPermissionSettingIntents( context: Context, skipRequest: Boolean): MutableList<Intent> {
        val intentList: MutableList<Intent> = ArrayList(7)

        var requestIgnoreBatteryOptimizationsIntent: Intent? = null
        if (isAndroid6()) {
            requestIgnoreBatteryOptimizationsIntent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
            requestIgnoreBatteryOptimizationsIntent.setData(getPackageNameUri(context))
            // 经过测试，如果是已经授权的情况下，是不能再跳转到这个 Intent 的，否则就会导致存在这个 Intent，也可以跳转过去，
            // 但是这个权限设置页就会立马 finish，就会导致代码实际跳转了但是用户没有感觉到有跳转权限设置页的问题
            // 经过测试，发现有 HyperOS 就算授权了也可以跳转过去，但 MIUI 就不行，Android 原生也不行，所以这里要排除一下 HyperOS
            if (isGrantedPermission(context, skipRequest) && !DeviceOs.isHyperOs()) {
                requestIgnoreBatteryOptimizationsIntent = null
            }
        }

        var advancedPowerUsageDetailIntent: Intent? = null
        if (isAndroid12()) {
            // 应用的电池使用情况详情页：Settings.ACTION_VIEW_ADVANCED_POWER_USAGE_DETAIL
            // 虽然 ACTION_VIEW_ADVANCED_POWER_USAGE_DETAIL 是 Android 10 的源码才出现的
            // 但是经过测试，在 Android 10 上面是无法跳转的，只有到了 Android 12 才能跳转
            advancedPowerUsageDetailIntent = Intent("android.settings.VIEW_ADVANCED_POWER_USAGE_DETAIL")
            advancedPowerUsageDetailIntent.setData(getPackageNameUri(context))
        }

        var ignoreBatteryOptimizationSettingsIntent: Intent? = null
        if (isAndroid6()) {
            ignoreBatteryOptimizationSettingsIntent = Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS)
        }

        // 因为在 Android 10 的时候，这个特殊权限弹出的页面小米还是用谷歌原生的
        // 然而在 Android 11 之后的，这个权限页面被小米改成了自己定制化的页面
        if (skipRequest && !(isAndroid11() && (DeviceOs.isHyperOs() || DeviceOs.isMiui()))) {
            if (advancedPowerUsageDetailIntent != null) {
                intentList.add(advancedPowerUsageDetailIntent)
            }
            if (ignoreBatteryOptimizationSettingsIntent != null) {
                intentList.add(ignoreBatteryOptimizationSettingsIntent)
            }
            if (requestIgnoreBatteryOptimizationsIntent != null) {
                intentList.add(requestIgnoreBatteryOptimizationsIntent)
            }
        } else {
            if (requestIgnoreBatteryOptimizationsIntent != null) {
                intentList.add(requestIgnoreBatteryOptimizationsIntent)
            }
            if (advancedPowerUsageDetailIntent != null) {
                intentList.add(advancedPowerUsageDetailIntent)
            }
            if (ignoreBatteryOptimizationSettingsIntent != null) {
                intentList.add(ignoreBatteryOptimizationSettingsIntent)
            }
        }

        var intent: Intent
        // 经过测试，得出结论，MIUI 和 HyperOS 支持在应用详情页设置该权限：
        // 1. MIUI 应用详情页 -> 省电策略
        // 2. HyperOS 应用详情页 -> 电量消耗
        if (DeviceOs.isHyperOs() || DeviceOs.isMiui()) {
            intent = getApplicationDetailsSettingIntent(context)
            intentList.add(intent)

            intent = getManageApplicationSettingIntent()
            intentList.add(intent)

            intent = getApplicationSettingIntent()
            intentList.add(intent)
        }

        intent = getAndroidSettingIntent()
        intentList.add(intent)

        return intentList
    }

    override fun getResultWaitTime( context: Context): Int {
        if (!isSupportRequestPermission(context)) {
            return 0
        }

        // 小米手机默认等待时长
        val xiaomiPhoneDefaultWaitTime = 1000
        if (DeviceOs.isHyperOs()) {
            // 1. HyperOS 2.0.112.0，Android 15，小米 14，200 毫秒没有问题
            // 2. HyperOS 2.0.8.0，Android 15，小米 12S Pro，200 毫秒没有问题
            // 3. HyperOS 2.0.5.0，Android 15，红米 K60，200 毫秒没有问题
            // 4. HyperOS 2.0.1.0，Android 15，红米 14R，200 毫秒没有问题
            // 5. HyperOS 2.0.4.0，Android 14，小米平板 5，200 毫秒没有问题
            // 6. HyperOS 2.0.1.0，Android 14，小米 12 Pro 天玑版，200 毫秒没有问题
            // 7. HyperOS 1.0.7.0，Android 14，红米 Note 14，需要 1000 毫秒
            // 大致结论：HyperOS 2.0 及以上的系统没有问题，HyperOS 2.0 的 Android 版本有 Android 15 和 Android 14 的，
            //         Android 14 的 HyperOS 只有 1.0 的，没有找到 Android 14 HyperOS 2.0 的版本，
            //         所以这个问题应该是在 HyperOS 2.0 上面修复了，大概率 HyperOS 2.0  UI 大改版改动到了（看到 UI 有明显变化）
            //         结果测试人员发现了，开发人员不得不修，否则会影响自己的绩效，至此这个历史遗留 Bug 终于被发现并修复
            //         Android 15 的 HyperOS 2.0 版本 200 毫秒没有问题，但是 Android 14 的版本 HyperOS 1.0 还有有问题
            if (isAndroid15()) {
                return super.getResultWaitTime(context)
            }

            if (isAndroid14()) {
                val osBigVersionCode: Int = DeviceOs.getOsBigVersionCode()
                // 如果获取不到的大版本号又或者获取到的大版本号小于 2，就返回小米机型默认的等待时间
                if (osBigVersionCode < 2) {
                    return xiaomiPhoneDefaultWaitTime
                }
                return super.getResultWaitTime(context)
            }

            return xiaomiPhoneDefaultWaitTime
        }

        if (DeviceOs.isMiui() && isAndroid11()) {
            // 经过测试，发现小米 Android 11 及以上的版本，申请这个权限需要 1000 毫秒才能判断到（测试了 800 毫秒还不行）
            // 因为在 Android 10 的时候，这个特殊权限弹出的页面小米还是用谷歌原生的
            // 然而在 Android 11 之后的，这个权限页面被小米改成了自己定制化的页面
            // 测试了原生的模拟器和 vivo 云测并发现没有这个问题，所以断定这个 Bug 就是小米特有的
            return xiaomiPhoneDefaultWaitTime
        }

        return super.getResultWaitTime(context)
    }

    override fun isRegisterPermissionByManifestFile(): Boolean {
        // 表示当前权限需要在 AndroidManifest.xml 文件中进行静态注册
        return true
    }
}
