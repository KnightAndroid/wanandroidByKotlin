package com.knight.kotlin.library_permiss.permission.special

import android.content.Context
import android.content.Intent
import android.os.Parcel
import android.os.Parcelable
import android.provider.Settings
import com.knight.kotlin.library_permiss.permission.PermissionNames
import com.knight.kotlin.library_permiss.permission.common.SpecialPermission
import com.knight.kotlin.library_permiss.tools.PermissionVersion
import com.knight.kotlin.library_permiss.tools.PermissionVersion.isAndroid8


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

    override fun getFromAndroidVersion( context: Context): Int {
        return PermissionVersion.ANDROID_8
    }

    override fun isGrantedPermission( context: Context, skipRequest: Boolean): Boolean {
        if (!isAndroid8()) {
            return true
        }
        return context.packageManager.canRequestPackageInstalls()
    }

    
    override fun getPermissionSettingIntents( context: Context, skipRequest: Boolean): MutableList<Intent> {
        val intentList: MutableList<Intent> = ArrayList(6)
        var intent: Intent

        if (isAndroid8()) {
            intent = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES)
            intent.setData(getPackageNameUri(context))
            intentList.add(intent)

            // 如果是因为加包名的数据后导致不能跳转，就把包名的数据移除掉
            intent = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES)
            intentList.add(intent)
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
