package com.knight.kotlin.library_permiss.manifest

import com.knight.kotlin.library_permiss.manifest.node.ActivityManifestInfo
import com.knight.kotlin.library_permiss.manifest.node.ApplicationManifestInfo
import com.knight.kotlin.library_permiss.manifest.node.BroadcastReceiverManifestInfo
import com.knight.kotlin.library_permiss.manifest.node.PermissionManifestInfo
import com.knight.kotlin.library_permiss.manifest.node.ServiceManifestInfo
import com.knight.kotlin.library_permiss.manifest.node.UsesSdkManifestInfo


/**
 * Author:Knight
 * Time:2023/8/29 16:35
 * Description:AndroidManifestInfo
 */
class AndroidManifestInfo {
    /** 应用包名  */
    
    var packageName: String = ""

    /** 使用 sdk 信息  */

    var usesSdkInfo: UsesSdkManifestInfo? = null

    /** 权限节点信息  */
    
    val permissionInfoList: MutableList<PermissionManifestInfo> = mutableListOf()

    /** 查询包名列表  */
    
    val queriesPackageList: MutableList<String> = mutableListOf()

    /** Application 节点信息  */

    var applicationInfo: ApplicationManifestInfo? = null

    /** Activity 节点信息  */
    
    val activityInfoList: MutableList<ActivityManifestInfo> = mutableListOf()

    /** Service 节点信息  */
    
    val serviceInfoList: MutableList<ServiceManifestInfo> = mutableListOf()

    /** BroadcastReceiver 节点信息  */
    
    val receiverInfoList: MutableList<BroadcastReceiverManifestInfo> = mutableListOf()
}