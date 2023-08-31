package com.knight.kotlin.library_permiss

import android.content.pm.PackageInfo


/**
 * Author:Knight
 * Time:2023/8/29 16:35
 * Description:AndroidManifestInfo
 */
class AndroidManifestInfo {
    /** 应用包名  */
    var packageName: String? = null

    /** 使用 sdk 信息  */

    var usesSdkInfo: UsesSdkInfo? = null

    /** 权限节点信息  */

    val permissionInfoList = mutableListOf<PermissionInfo>()

    /** Application 节点信息  */
    var applicationInfo: ApplicationInfo? = null

    /** Activity 节点信息  */

    val activityInfoList = mutableListOf<ActivityInfo>()

    /** Service 节点信息  */
    val serviceInfoList = mutableListOf<ServiceInfo>()

    class UsesSdkInfo {
        var minSdkVersion = 0
    }

    class PermissionInfo {
        var name: String? = null
        var maxSdkVersion = 0
        var usesPermissionFlags = 0
        fun neverForLocation(): Boolean {
            return usesPermissionFlags and REQUESTED_PERMISSION_NEVER_FOR_LOCATION != 0
        }

        companion object {
            /** [PackageInfo.REQUESTED_PERMISSION_NEVER_FOR_LOCATION]  */
            private const val REQUESTED_PERMISSION_NEVER_FOR_LOCATION = 0x00010000
        }
    }

    class ApplicationInfo {
        var name: String? = null
        var requestLegacyExternalStorage = false
    }

    class ActivityInfo {
        var name: String? = null
        var supportsPictureInPicture = false
    }

    class ServiceInfo {
        var name: String? = null
        var permission: String? = null
    }
}