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
import com.knight.kotlin.library_permiss.tools.PermissionVersion.isAndroid6


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

    override fun getFromAndroidVersion(context: Context): Int {
        return PermissionVersion.ANDROID_6
    }

    override fun isGrantedPermission(context: Context, skipRequest: Boolean): Boolean {
        if (!isAndroid6()) {
            return true
        }
        return Settings.System.canWrite(context)
    }


    override fun getPermissionSettingIntents(context: Context, skipRequest: Boolean): MutableList<Intent> {
        val intentList: MutableList<Intent> = ArrayList(6)
        var intent: Intent

        if (isAndroid6()) {
            intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
            intent.setData(getPackageNameUri(context))
            intentList.add(intent)

            // 如果是因为加包名的数据后导致不能跳转，就把包名的数据移除掉
            intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
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
