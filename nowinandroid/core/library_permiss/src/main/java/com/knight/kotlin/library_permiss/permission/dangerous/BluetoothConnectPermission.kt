package com.knight.kotlin.library_permiss.permission.dangerous

import android.Manifest
import android.app.Activity
import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import com.knight.kotlin.library_permiss.manifest.AndroidManifestInfo
import com.knight.kotlin.library_permiss.manifest.node.PermissionManifestInfo
import com.knight.kotlin.library_permiss.permission.PermissionGroups
import com.knight.kotlin.library_permiss.permission.PermissionNames
import com.knight.kotlin.library_permiss.permission.base.IPermission
import com.knight.kotlin.library_permiss.permission.common.DangerousPermission
import com.knight.kotlin.library_permiss.tools.PermissionVersion
import com.knight.kotlin.library_permiss.tools.PermissionVersion.isAndroid12


/**
 * @Description 蓝牙连接权限类
 * @Author knight
 * @Time 2025/7/10 21:12
 *
 */
class BluetoothConnectPermission : DangerousPermission {
    constructor()

    private constructor(`in`: Parcel) : super(`in`)


    override fun getPermissionName(): String {
        return PERMISSION_NAME
    }

    override fun getPermissionGroup( context: Context): String {
        // 注意：在 Android 12 的时候，蓝牙相关的权限已经归到附近设备的权限组了，但是在 Android 12 之前，蓝牙相关的权限归属定位权限组
        return if (isAndroid12()) PermissionGroups.NEARBY_DEVICES else PermissionGroups.LOCATION
    }

    override fun getFromAndroidVersion( context: Context): Int {
        return PermissionVersion.ANDROID_12
    }

    override fun getMinTargetSdkVersion( context: Context): Int {
        // 部分厂商修改了蓝牙权限机制，在 targetSdk 不满足条件的情况下（小于 31），仍需要让应用申请这个权限，相关的 issue 地址：
        // 1. https://github.com/getActivity/XXPermissions/issues/123
        // 2. https://github.com/getActivity/XXPermissions/issues/302
        return PermissionVersion.ANDROID_6
    }

    override fun checkSelfByManifestFile(
         activity: Activity,
         requestList: List<IPermission>,
         manifestInfo: AndroidManifestInfo,
         permissionInfoList: List<PermissionManifestInfo>,
         currentPermissionInfo: PermissionManifestInfo
    ) {
        super.checkSelfByManifestFile(activity, requestList, manifestInfo, permissionInfoList, currentPermissionInfo)
        // 如果权限出现的版本小于 minSdkVersion，则证明该权限可能会在旧系统上面申请，需要在 AndroidManifest.xml 文件注册一下旧版权限
        if (getFromAndroidVersion(activity) > getMinSdkVersion(activity, manifestInfo)) {
            checkPermissionRegistrationStatus(permissionInfoList, Manifest.permission.BLUETOOTH, PermissionVersion.ANDROID_11)
        }
    }



    companion object {
        val PERMISSION_NAME: String = PermissionNames.BLUETOOTH_CONNECT
        @JvmField
        val CREATOR : Parcelable.Creator<BluetoothConnectPermission> =


            object : Parcelable.Creator<BluetoothConnectPermission> {
                /** 当前权限名称，注意：该常量字段仅供框架内部使用，不提供给外部引用，
                 * 如果需要获取权限名称的字符串，请直接通过 [PermissionNames] 类获取
                 */

                override fun createFromParcel(source: Parcel): BluetoothConnectPermission {
                    return BluetoothConnectPermission(source)
                }

                override fun newArray(size: Int): Array<BluetoothConnectPermission?> {
                    return arrayOfNulls(size)
                }
            }
    }


}