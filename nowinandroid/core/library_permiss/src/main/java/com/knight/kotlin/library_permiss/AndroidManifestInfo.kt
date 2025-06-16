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

    /** 查询包名列表  */
    val queriesPackageList = mutableListOf<String>()

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
        /** 权限名称 **/
        var name: String? = null
        /** 最大生效 sdk 版本 **/
        var maxSdkVersion = 0
        /** 权限使用标志 **/
        var usesPermissionFlags = 0
        fun neverForLocation(): Boolean {
            return usesPermissionFlags and REQUESTED_PERMISSION_NEVER_FOR_LOCATION != 0
        }

        companion object {
            /** [PackageInfo.REQUESTED_PERMISSION_NEVER_FOR_LOCATION]  */
            private val REQUESTED_PERMISSION_NEVER_FOR_LOCATION = if(AndroidVersionTools.isAndroid12()) PackageInfo.REQUESTED_PERMISSION_NEVER_FOR_LOCATION else 0x00010000


        }
    }

    class ApplicationInfo {
        /** 应用的类名 **/
        var name: String? = null
        /** 是否忽略分区存储特性 **/
        var requestLegacyExternalStorage = false
    }

    class ActivityInfo {
        /** 活动的类名**/
        var name: String? = null
        /** 窗口是否支持画中画 */
        var supportsPictureInPicture = false
    }

    class ServiceInfo {
        /** 服务的类名 */
        var name: String? = null
        /** 服务所使用到的权限 */
        var permission: String? = null
    }
}