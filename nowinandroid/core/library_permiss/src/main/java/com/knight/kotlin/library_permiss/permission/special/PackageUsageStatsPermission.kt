package com.knight.kotlin.library_permiss.permission.special

import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.os.Parcel
import android.os.Parcelable
import android.provider.Settings
import com.knight.kotlin.library_permiss.permission.PermissionNames
import com.knight.kotlin.library_permiss.permission.common.SpecialPermission
import com.knight.kotlin.library_permiss.tools.PermissionVersion


/**
 * @author created by luguian
 * @organize
 * @Date 2025/7/11 8:54
 * @descript:查看应用使用情况权限类
 */
class PackageUsageStatsPermission : SpecialPermission {

    companion object {
        /** 当前权限名称，仅供框架内部使用 */
        val PERMISSION_NAME: String = PermissionNames.PACKAGE_USAGE_STATS

        @JvmField
        val CREATOR: Parcelable.Creator<PackageUsageStatsPermission> =
            object : Parcelable.Creator<PackageUsageStatsPermission> {
                override fun createFromParcel(source: Parcel): PackageUsageStatsPermission {
                    return PackageUsageStatsPermission(source)
                }

                override fun newArray(size: Int): Array<PackageUsageStatsPermission?> {
                    return arrayOfNulls(size)
                }
            }
    }

    constructor() : super()

    private constructor(source: Parcel) : super(source)

    override fun getPermissionName(): String = PERMISSION_NAME

    override fun getFromAndroidVersion(): Int = PermissionVersion.ANDROID_5

    override fun isGrantedPermission(context: Context, skipRequest: Boolean): Boolean {
        // Android 5 以下系统默认授予权限
        return if (!PermissionVersion.isAndroid5()) {
            true
        } else {
            checkOpPermission(context, AppOpsManager.OPSTR_GET_USAGE_STATS, false)
        }
    }

    override fun getPermissionSettingIntents(context: Context): MutableList<Intent> {
        val intentList = ArrayList<Intent>(3)

        if (PermissionVersion.isAndroid10()) {
            // Android 10+ 加包名有效
            intentList.add(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS).apply {
                data = getPackageNameUri(context)
            })
        }

        if (PermissionVersion.isAndroid5()) {
            intentList.add(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
        }

        intentList.add(getAndroidSettingIntent())
        return intentList
    }

    override fun isRegisterPermissionByManifestFile(): Boolean = true
}
