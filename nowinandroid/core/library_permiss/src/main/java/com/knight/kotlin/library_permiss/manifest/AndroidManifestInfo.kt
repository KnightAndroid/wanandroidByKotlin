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
    var usesSdkManifestInfo: UsesSdkManifestInfo? = null

    /** 权限节点信息  */
    
    val permissionManifestInfoList: List<PermissionManifestInfo> = ArrayList()

    /** 查询包名列表  */
    
    val queriesPackageList: List<String> = ArrayList()

    /** Application 节点信息  */
    var applicationManifestInfo: ApplicationManifestInfo? = null

    /** Activity 节点信息  */
    
    val activityManifestInfoList: List<ActivityManifestInfo> = ArrayList()

    /** Service 节点信息  */
    
    val serviceManifestInfoList: List<ServiceManifestInfo> = ArrayList()

    /** BroadcastReceiver 节点信息  */
    
    val broadcastReceiverManifestInfoList: List<BroadcastReceiverManifestInfo> = ArrayList()
}