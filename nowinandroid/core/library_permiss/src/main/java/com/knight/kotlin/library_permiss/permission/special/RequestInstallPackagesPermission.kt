package com.knight.kotlin.library_permiss.permission.special

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
 * @Date 2025/7/11 8:57
 * @descript:安装应用权限类
 */
class RequestInstallPackagesPermission: SpecialPermission {

    companion object {
        const val PERMISSION_NAME = PermissionNames.REQUEST_INSTALL_PACKAGES

        @JvmField
        val CREATOR = object : Parcelable.Creator<RequestInstallPackagesPermission> {
            override fun createFromParcel(source: Parcel): RequestInstallPackagesPermission {
                return RequestInstallPackagesPermission(source)
            }

            override fun newArray(size: Int): Array<RequestInstallPackagesPermission?> {
                return arrayOfNulls(size)
            }
        }
    }
    constructor() : super()
    private constructor(parcel: Parcel) : super(parcel)

    override fun getPermissionName(): String = PERMISSION_NAME

    override fun getFromAndroidVersion(): Int = PermissionVersion.ANDROID_8

    override fun isGrantedPermission(context: Context, skipRequest: Boolean): Boolean {
        if (!PermissionVersion.isAndroid8()) {
            return true
        }
        return context.packageManager.canRequestPackageInstalls()
    }

    override fun getPermissionSettingIntents(context: Context): MutableList<Intent> {
        val intentList = ArrayList<Intent>(6)

        if (PermissionVersion.isAndroid8()) {
            intentList.add(Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES).apply {
                data = getPackageNameUri(context)
            })

            // 如果带包名不能跳转，则不带包名也尝试一下
            intentList.add(Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES))
        }

        intentList.add(getApplicationDetailsSettingIntent(context))
        intentList.add(getManageApplicationSettingIntent())
        intentList.add(getApplicationSettingIntent())
        intentList.add(getAndroidSettingIntent())

        return intentList
    }

    override fun isRegisterPermissionByManifestFile(): Boolean = true
}
