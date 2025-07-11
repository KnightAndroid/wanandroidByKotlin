package com.knight.kotlin.library_permiss.permission.special

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.VpnService
import android.os.Parcel
import android.os.Parcelable
import com.knight.kotlin.library_permiss.manifest.AndroidManifestInfo
import com.knight.kotlin.library_permiss.manifest.node.PermissionManifestInfo
import com.knight.kotlin.library_permiss.permission.PermissionNames
import com.knight.kotlin.library_permiss.permission.base.IPermission
import com.knight.kotlin.library_permiss.permission.common.SpecialPermission
import com.knight.kotlin.library_permiss.tools.PermissionUtils
import com.knight.kotlin.library_permiss.tools.PermissionVersion


/**
 * @author created by luguian
 * @organize
 * @Date 2025/7/11 8:50
 * @descript: VPN 权限类
 */
class BindVpnServicePermission : SpecialPermission {

    companion object {
        /** 当前权限名称，注意：该常量字段仅供框架内部使用，不提供给外部引用 */
        val PERMISSION_NAME: String = PermissionNames.BIND_VPN_SERVICE

        @JvmField
        val CREATOR: Parcelable.Creator<BindVpnServicePermission> =
            object : Parcelable.Creator<BindVpnServicePermission> {
                override fun createFromParcel(source: Parcel): BindVpnServicePermission {
                    return BindVpnServicePermission(source)
                }

                override fun newArray(size: Int): Array<BindVpnServicePermission?> {
                    return arrayOfNulls(size)
                }
            }
    }
    constructor()
    constructor(source: Parcel) : super(source)

    override fun getPermissionName(): String = PERMISSION_NAME

    override fun getFromAndroidVersion(): Int = PermissionVersion.ANDROID_4_0

    override fun isGrantedPermission(context: Context, skipRequest: Boolean): Boolean {
        return VpnService.prepare(context) == null
    }

    override fun getPermissionSettingIntents(context: Context): MutableList<Intent> {
        return mutableListOf<Intent>().apply {
            VpnService.prepare(context)?.let { add(it) }
            add(getAndroidSettingIntent())
        }
    }

    override fun checkSelfByManifestFile(
        activity: Activity,
        requestPermissions: List<IPermission>,
        androidManifestInfo: AndroidManifestInfo,
        permissionManifestInfoList: List<PermissionManifestInfo>,
        currentPermissionManifestInfo: PermissionManifestInfo
    ) {
        super.checkSelfByManifestFile(
            activity,
            requestPermissions,
            androidManifestInfo,
            permissionManifestInfoList,
            currentPermissionManifestInfo
        )

        val serviceManifestInfoList = androidManifestInfo.serviceManifestInfoList
        for (serviceInfo in serviceManifestInfoList) {
            val permission = serviceInfo.permission
            if (permission != null && PermissionUtils.equalsPermission(this, permission)) {
                return // 找到了正确注册的 Service，跳出检查
            }
        }

        throw IllegalArgumentException(
            "No Service was found to have registered the android:permission=\"${getPermissionName()}\" property, " +
                    "Please register this property to VpnService subclass by AndroidManifest.xml file, " +
                    "otherwise it will lead to can't apply for the permission"
        )
    }
}
