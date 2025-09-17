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
import com.knight.kotlin.library_permiss.tools.PermissionVersion.isAndroid10
import com.knight.kotlin.library_permiss.tools.PermissionVersion.isAndroid5


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


    override fun getFromAndroidVersion( context: Context): Int {
        return PermissionVersion.ANDROID_5
    }

    override fun isGrantedPermission( context: Context, skipRequest: Boolean): Boolean {
        if (!isAndroid5()) {
            return true
        }
        return checkOpPermission(context, AppOpsManager.OPSTR_GET_USAGE_STATS, false)
    }

    
    override fun getPermissionSettingIntents( context: Context, skipRequest: Boolean): MutableList<Intent> {
        val intentList: MutableList<Intent> = ArrayList(3)
        var intent: Intent

        if (isAndroid10()) {
            intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
            // 经过测试，只有在 Android 10 及以上加包名才有效果
            // 如果在 Android 10 以下加包名会导致无法跳转
            intent.setData(getPackageNameUri(context))
            intentList.add(intent)
        }

        if (isAndroid5()) {
            intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
            intentList.add(intent)
        }

        intent = getAndroidSettingIntent()
        intentList.add(intent)

        return intentList
    }

    override fun isRegisterPermissionByManifestFile(): Boolean {
        // 表示当前权限需要在 AndroidManifest.xml 文件中进行静态注册
        return true
    }
}
