package com.knight.kotlin.library_permiss.permission.dangerous

import android.app.Activity
import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import com.knight.kotlin.library_permiss.manifest.AndroidManifestInfo
import com.knight.kotlin.library_permiss.manifest.node.PermissionManifestInfo
import com.knight.kotlin.library_permiss.permission.PermissionGroups
import com.knight.kotlin.library_permiss.permission.PermissionLists
import com.knight.kotlin.library_permiss.permission.PermissionNames
import com.knight.kotlin.library_permiss.permission.base.IPermission
import com.knight.kotlin.library_permiss.permission.common.DangerousPermission
import com.knight.kotlin.library_permiss.tools.PermissionUtils
import com.knight.kotlin.library_permiss.tools.PermissionVersion


/**
 * @Description  WIFI 权限类
 * @Author knight
 * @Time 2025/7/10 21:37
 *
 */

class NearbyWifiDevicesPermission : DangerousPermission {
    constructor()

    private constructor(`in`: Parcel) : super(`in`)

    
    override fun getPermissionName(): String {
        return PERMISSION_NAME
    }

    override fun getPermissionGroup(): String {
        // 注意：在 Android 13 的时候，WIFI 相关的权限已经归到附近设备的权限组了，但是在 Android 13 之前，WIFI 相关的权限归属定位权限组
        return if (PermissionVersion.isAndroid13()) PermissionGroups.NEARBY_DEVICES else PermissionGroups.LOCATION
    }

    override fun getFromAndroidVersion(): Int {
        return PermissionVersion.ANDROID_13
    }

    
    override fun getOldPermissions(context: Context): List<IPermission> {
        // Android 13 以下使用 WIFI 功能需要用到精确定位的权限
        return PermissionUtils.asArrayList(PermissionLists.getAccessFineLocationPermission())
    }

    override fun isGrantedPermissionByLowVersion(
         context: Context,
        skipRequest: Boolean
    ): Boolean {
        return PermissionLists.getAccessFineLocationPermission()
            .isGrantedPermission(context, skipRequest)
    }

    override fun isDoNotAskAgainPermissionByLowVersion( activity: Activity): Boolean {
        return PermissionLists.getAccessFineLocationPermission().isDoNotAskAgainPermission(activity)
    }

     override fun checkSelfByManifestFile(
         activity: Activity,
         requestPermissions: List<IPermission>,
         androidManifestInfo: AndroidManifestInfo,
         permissionManifestInfoList: List<PermissionManifestInfo>,
         currentPermissionManifestInfo: PermissionManifestInfo
    ) {
        super.checkSelfByManifestFile(
            activity, requestPermissions, androidManifestInfo, permissionManifestInfoList,
            currentPermissionManifestInfo
        )
        // 如果权限出现的版本小于 minSdkVersion，则证明该权限可能会在旧系统上面申请，需要在 AndroidManifest.xml 文件注册一下旧版权限
        if (getFromAndroidVersion() > getMinSdkVersion(activity, androidManifestInfo)) {
            checkPermissionRegistrationStatus(
                permissionManifestInfoList,
                PermissionNames.ACCESS_FINE_LOCATION,
                PermissionVersion.ANDROID_12_L
            )
        }

        // 如果请求的权限已经包含了精确定位权限，就跳过检查
        if (PermissionUtils.containsPermission(
                requestPermissions,
                PermissionNames.ACCESS_FINE_LOCATION
            )
        ) {
            return
        }
        // 如果当前权限没有在清单文件中注册，就跳过检查
        if (currentPermissionManifestInfo == null) {
            return
        }
        // 如果当前权限有在清单文件注册，并且设置了 neverForLocation 标记，就跳过检查
        if (currentPermissionManifestInfo.neverForLocation()) {
            return
        }

        // WIFI 权限：https://developer.android.google.cn/about/versions/13/features/nearby-wifi-devices-permission?hl=zh-cn#assert-never-for-location
        // 在以 Android 13 为目标平台时，请考虑您的应用是否会通过 WIFI API 推导物理位置，如果不会，则应坚定声明此情况。
        // 如需做出此声明，请在应用的清单文件中将 usesPermissionFlags 属性设为 neverForLocation
        val maxSdkVersionString =
            if (currentPermissionManifestInfo.maxSdkVersion != Int.MAX_VALUE) "android:maxSdkVersion=\"" + currentPermissionManifestInfo.maxSdkVersion + "\" " else ""
        // 根据不同的需求场景决定，解决方法分为两种：
        //   1. 不需要使用 WIFI 权限来获取物理位置：只需要在清单文件中注册的权限上面加上 android:usesPermissionFlags="neverForLocation" 即可
        //   2. 需要使用 WIFI 权限来获取物理位置：在申请 WIFI 权限时，还需要动态申请 ACCESS_FINE_LOCATION 权限
        // 通常情况下，我们都不需要使用 WIFI 权限来获取物理位置，所以选择第一种方法即可
        throw IllegalArgumentException(
            "If your app doesn't use " + currentPermissionManifestInfo.name +
                    " to get physical location, " + "please change the <uses-permission android:name=\"" +
                    currentPermissionManifestInfo.name + "\" " + maxSdkVersionString + "/> node in the " +
                    "manifest file to <uses-permission android:name=\"" + currentPermissionManifestInfo.name +
                    "\" android:usesPermissionFlags=\"neverForLocation\" " + maxSdkVersionString + "/> node, " +
                    "if your app need use \"" + currentPermissionManifestInfo.name + "\" to get physical location, " +
                    "also need to add \"" + PermissionNames.ACCESS_FINE_LOCATION + "\" permissions"
        )
    }


    companion object CREATOR : Parcelable.Creator<NearbyWifiDevicesPermission> {

        /** 当前权限名称，注意：该常量字段仅供框架内部使用，不提供给外部引用，如果需要获取权限名称的字符串，请直接通过 [PermissionNames] 类获取  */
        val PERMISSION_NAME: String = PermissionNames.NEARBY_WIFI_DEVICES

        override fun createFromParcel(source: Parcel): NearbyWifiDevicesPermission? {
            return NearbyWifiDevicesPermission(source)
        }

        override fun newArray(size: Int): Array<NearbyWifiDevicesPermission?> {
            return arrayOfNulls(size)
        }


    }
}