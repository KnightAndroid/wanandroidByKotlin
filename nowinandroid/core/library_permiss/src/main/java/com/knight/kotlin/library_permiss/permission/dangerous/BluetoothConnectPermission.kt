package com.knight.kotlin.library_permiss.permission.dangerous

import android.os.Parcel
import android.os.Parcelable
import com.knight.kotlin.library_permiss.manifest.AndroidManifestInfo
import com.knight.kotlin.library_permiss.manifest.node.PermissionManifestInfo
import com.knight.kotlin.library_permiss.permission.base.IPermission
import com.knight.kotlin.library_permiss.permission.common.DangerousPermission


/**
 * @Description
 * @Author knight
 * @Time 2025/7/10 21:12
 *
 */
class BluetoothConnectPermission : DangerousPermission {
    constructor()

    private constructor(`in`: Parcel) : super(`in`)

    @NonNull
    override fun getPermissionName(): String {
        return PERMISSION_NAME
    }

    override fun getPermissionGroup(): String {
        // 注意：在 Android 12 的时候，蓝牙相关的权限已经归到附近设备的权限组了，但是在 Android 12 之前，蓝牙相关的权限归属定位权限组
        return if (PermissionVersion.isAndroid12()) PermissionGroups.NEARBY_DEVICES else PermissionGroups.LOCATION
    }

    override fun getFromAndroidVersion(): Int {
        return PermissionVersion.ANDROID_12
    }

    override fun getMinTargetSdkVersion(): Int {
        // 部分厂商修改了蓝牙权限机制，在 targetSdk 不满足条件的情况下（小于 31），仍需要让应用申请这个权限，相关的 issue 地址：
        // 1. https://github.com/getActivity/XXPermissions/issues/123
        // 2. https://github.com/getActivity/XXPermissions/issues/302
        return PermissionVersion.ANDROID_6
    }

    protected override fun checkSelfByManifestFile(
        @NonNull activity: Activity?,
        @NonNull requestPermissions: List<IPermission?>?,
        @NonNull androidManifestInfo: AndroidManifestInfo?,
        @NonNull permissionManifestInfoList: List<PermissionManifestInfo?>?,
        @Nullable currentPermissionManifestInfo: PermissionManifestInfo
    ) {
        super.checkSelfByManifestFile(
            activity, requestPermissions, androidManifestInfo, permissionManifestInfoList,
            currentPermissionManifestInfo
        )
        // 如果权限出现的版本小于 minSdkVersion，则证明该权限可能会在旧系统上面申请，需要在 AndroidManifest.xml 文件注册一下旧版权限
        if (getFromAndroidVersion() > getMinSdkVersion(activity, androidManifestInfo)) {
            checkPermissionRegistrationStatus(
                permissionManifestInfoList,
                Manifest.permission.BLUETOOTH,
                PermissionVersion.ANDROID_11
            )
        }
    }




        companion object CREATOR: Parcelable.Creator<BluetoothConnectPermission> {

            /** 当前权限名称，注意：该常量字段仅供框架内部使用，不提供给外部引用，如果需要获取权限名称的字符串，请直接通过 [PermissionNames] 类获取  */

            val PERMISSION_NAME: String = PermissionNames.BLUETOOTH_CONNECT

                override fun createFromParcel(source: Parcel): BluetoothConnectPermission? {
                    return BluetoothConnectPermission(source)
                }

                override fun newArray(size: Int): Array<BluetoothConnectPermission?> {
                    return arrayOfNulls(size)
                }
            }


}