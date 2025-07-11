package com.knight.kotlin.library_permiss.permission.special

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Parcel
import android.os.Parcelable
import android.os.PowerManager
import android.provider.Settings
import com.knight.kotlin.library_permiss.permission.PermissionNames
import com.knight.kotlin.library_permiss.permission.common.SpecialPermission
import com.knight.kotlin.library_permiss.tools.PermissionVersion
import com.knight.kotlin.library_permiss.tools.PhoneRomUtils


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

    override fun getFromAndroidVersion(): Int = PermissionVersion.ANDROID_6

    override fun isGrantedPermission(context: Context, skipRequest: Boolean): Boolean {
        if (!PermissionVersion.isAndroid6()) {
            return true
        }
        val powerManager = context.getSystemService(PowerManager::class.java)
        // 防御性编程，防止 powerManager 为 null
        return powerManager?.isIgnoringBatteryOptimizations(context.packageName) ?: false
    }

    @SuppressLint("BatteryLife")
    override fun getPermissionSettingIntents(context: Context): MutableList<Intent> {
        val intentList = ArrayList<Intent>(7)

        if (PermissionVersion.isAndroid6()) {
            intentList.add(Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply {
                data = getPackageNameUri(context)
            })
        }

        if (PermissionVersion.isAndroid12()) {
            intentList.add(Intent("android.settings.VIEW_ADVANCED_POWER_USAGE_DETAIL").apply {
                data = getPackageNameUri(context)
            })
        }

        if (PermissionVersion.isAndroid6()) {
            intentList.add(Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS))
        }

        if (PhoneRomUtils.isMiui() || PhoneRomUtils.isHyperOs()) {
            intentList.add(getApplicationDetailsSettingIntent(context))
            intentList.add(getManageApplicationSettingIntent())
            intentList.add(getApplicationSettingIntent())
        }

        intentList.add(getAndroidSettingIntent())

        return intentList
    }

    override fun getResultWaitTime(context: Context): Int {
        if (!isSupportRequestPermission(context)) {
            return 0
        }

        val xiaomiPhoneDefaultWaitTime = 1000

        if (PhoneRomUtils.isHyperOs()) {
            if (PermissionVersion.isAndroid15()) {
                return super.getResultWaitTime(context)
            }

            if (PermissionVersion.isAndroid14()) {
                val romBigVersionCode = PhoneRomUtils.getRomBigVersionCode()
                return if (romBigVersionCode < 2) {
                    xiaomiPhoneDefaultWaitTime
                } else {
                    super.getResultWaitTime(context)
                }
            }

            return xiaomiPhoneDefaultWaitTime
        }

        if (PhoneRomUtils.isMiui() &&
            PermissionVersion.isAndroid11() &&
            PermissionVersion.getCurrentVersion() >= getFromAndroidVersion()) {
            return xiaomiPhoneDefaultWaitTime
        }

        return super.getResultWaitTime(context)
    }

    override fun isRegisterPermissionByManifestFile(): Boolean = true
}
