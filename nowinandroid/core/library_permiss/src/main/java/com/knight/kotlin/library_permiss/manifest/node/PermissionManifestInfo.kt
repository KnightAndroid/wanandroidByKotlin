package com.knight.kotlin.library_permiss.manifest.node

import android.content.pm.PackageInfo



/**
 * @Description
 * @Author knight
 * @Time 2025/7/10 20:45
 *
 */

class PermissionManifestInfo {
    /**
     * 权限名称
     */
    var name: String? = null

    /**
     * 最大生效 sdk 版本
     */
    var maxSdkVersion: Int = 0

    /**
     * 权限使用标志
     */
    var usesPermissionFlags: Int = 0

    /**
     * 是否不会用当前权限需要推导地理位置
     */
    fun neverForLocation(): Boolean {
        return (usesPermissionFlags and REQUESTED_PERMISSION_NEVER_FOR_LOCATION) != 0
    }

    companion object {
        /**
         * 不需要请求地理位置标志
         */
        private var REQUESTED_PERMISSION_NEVER_FOR_LOCATION = 0

        init {
            if (PermissionVersion.isAndroid12()) {
                REQUESTED_PERMISSION_NEVER_FOR_LOCATION =
                    PackageInfo.REQUESTED_PERMISSION_NEVER_FOR_LOCATION
            } else {
                REQUESTED_PERMISSION_NEVER_FOR_LOCATION = 0x00010000
            }
        }
    }
}