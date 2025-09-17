package com.knight.kotlin.library_permiss.permission.special

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.VpnService
import android.os.Parcel
import android.os.Parcelable
import com.knight.kotlin.library_permiss.manifest.AndroidManifestInfo
import com.knight.kotlin.library_permiss.manifest.node.IntentFilterManifestInfo
import com.knight.kotlin.library_permiss.manifest.node.PermissionManifestInfo
import com.knight.kotlin.library_permiss.manifest.node.ServiceManifestInfo
import com.knight.kotlin.library_permiss.permission.PermissionNames
import com.knight.kotlin.library_permiss.permission.base.IPermission
import com.knight.kotlin.library_permiss.permission.common.SpecialPermission
import com.knight.kotlin.library_permiss.tools.PermissionUtils
import com.knight.kotlin.library_permiss.tools.PermissionVersion
import com.knight.kotlin.library_permiss.tools.PermissionVersion.isAndroid15


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

    
    override fun getPermissionPageType(context: Context): PermissionPageType {
        // VPN 权限在 Android 15 及以上版本的 OPPO 系统上面是一个不透明的 Activity 页面
        if (DeviceOs.isColorOs() && isAndroid15()) {
            return PermissionPageType.OPAQUE_ACTIVITY
        }
        return if (VpnService.prepare(context) != null) PermissionPageType.TRANSPARENT_ACTIVITY else PermissionPageType.OPAQUE_ACTIVITY
    }

    override fun getFromAndroidVersion( context: Context): Int {
        return PermissionVersion.ANDROID_4_0
    }

    override fun isGrantedPermission( context: Context, skipRequest: Boolean): Boolean {
        return VpnService.prepare(context) == null
    }

    
    override fun getPermissionSettingIntents( context: Context, skipRequest: Boolean): List<Intent> {
        val intentList: MutableList<Intent> = ArrayList(2)
        intentList.add(VpnService.prepare(context))
        intentList.add(getAndroidSettingIntent())
        return intentList
    }

    protected override fun checkSelfByManifestFile(
         activity: Activity,
         requestList: List<IPermission>,
         manifestInfo: AndroidManifestInfo,
         permissionInfoList: List<PermissionManifestInfo>,
        currentPermissionInfo: PermissionManifestInfo
    ) {
        super.checkSelfByManifestFile(activity, requestList, manifestInfo, permissionInfoList, currentPermissionInfo)
        // 判断有没有 Service 类注册了 android:permission="android.permission.BIND_VPN_SERVICE" 属性
        val serviceInfoList: List<ServiceManifestInfo> = manifestInfo.serviceInfoList
        for (i in serviceInfoList.indices) {
            val serviceInfo = serviceInfoList[i]
            val permission = serviceInfo.permission ?: continue

            if (!PermissionUtils.equalsPermission(this, permission)) {
                continue
            }

            val action = "android.net.VpnService"
            // 当前是否注册了 VPN 服务的意图
            var registeredVpnServiceAction = false
            val intentFilterInfoList: List<IntentFilterManifestInfo> = serviceInfo.intentFilterInfoList
            if (intentFilterInfoList != null) {
                for (intentFilterInfo in intentFilterInfoList) {
                    if (intentFilterInfo.actionList.contains(action)) {
                        registeredVpnServiceAction = true
                        break
                    }
                }
            }
            if (registeredVpnServiceAction) {
                // 符合要求，中断所有的循环并返回，避免走到后面的抛异常代码
                return
            }

            val xmlCode = ("\t\t<intent-filter>\n"
                    + "\t\t    <action android:name=\"" + action + "\" />\n"
                    + "\t\t</intent-filter>")
            throw IllegalArgumentException(
                """Please add an intent filter for "${serviceInfo.name}" in the AndroidManifest.xml file.
$xmlCode"""
            )
        }

        /*
         没有找到有任何 Service 注册过 android:permission="android.permission.BIND_VPN_SERVICE" 属性，
         请注册该属性给 VpnService 的子类到 AndroidManifest.xml 文件中
         */
        throw IllegalArgumentException(
            ("No Service was found to have registered the android:permission=\"" + getPermissionName() +
                    "\" property, Please register this property to VpnService subclass by AndroidManifest.xml file, "
                    + "otherwise it will lead to can't apply for the permission")
        )
    }
}
