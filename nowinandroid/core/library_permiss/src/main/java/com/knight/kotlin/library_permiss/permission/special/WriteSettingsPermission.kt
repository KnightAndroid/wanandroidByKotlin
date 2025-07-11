package com.knight.kotlin.library_permiss.permission.special


/**
 * @author created by luguian
 * @organize
 * @Date 2025/7/11 9:02
 * @descript:写入系统设置权限类
 */
import android.content.Context
import android.content.Intent
import android.os.Parcel
import android.os.Parcelable
import android.provider.Settings
import com.knight.kotlin.library_permiss.permission.PermissionNames
import com.knight.kotlin.library_permiss.permission.common.SpecialPermission
import com.knight.kotlin.library_permiss.tools.PermissionVersion

class WriteSettingsPermission : SpecialPermission {

    companion object {
        /** 当前权限名称，注意：该常量字段仅供框架内部使用，不提供给外部引用，如果需要获取权限名称的字符串，请直接通过 [PermissionNames] 类获取 */
        @JvmField
        val PERMISSION_NAME: String = PermissionNames.WRITE_SETTINGS

        @JvmField
        val CREATOR: Parcelable.Creator<WriteSettingsPermission> = object : Parcelable.Creator<WriteSettingsPermission> {
            override fun createFromParcel(source: Parcel): WriteSettingsPermission {
                return WriteSettingsPermission(source)
            }

            override fun newArray(size: Int): Array<WriteSettingsPermission?> {
                return arrayOfNulls(size)
            }
        }
    }

    constructor() : super()
    private constructor(parcel: Parcel) : super(parcel)


    
    override fun getPermissionName(): String = PERMISSION_NAME

    override fun getFromAndroidVersion(): Int = PermissionVersion.ANDROID_6

    override fun isGrantedPermission( context: Context, skipRequest: Boolean): Boolean {
        if (!PermissionVersion.isAndroid6()) {
            return true
        }
        return Settings.System.canWrite(context)
    }

    
    override fun getPermissionSettingIntents( context: Context): MutableList<Intent> {
        val intentList = ArrayList<Intent>(6)

        if (PermissionVersion.isAndroid6()) {
            Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS).apply {
                data = getPackageNameUri(context)
                intentList.add(this)
            }
            // 如果是因为加包名的数据后导致不能跳转，就把包名的数据移除掉
            intentList.add(Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS))
        }

        intentList.add(getApplicationDetailsSettingIntent(context))
        intentList.add(getManageApplicationSettingIntent())
        intentList.add(getApplicationSettingIntent())
        intentList.add(getAndroidSettingIntent())

        return intentList
    }

    override fun isRegisterPermissionByManifestFile(): Boolean = true
}
