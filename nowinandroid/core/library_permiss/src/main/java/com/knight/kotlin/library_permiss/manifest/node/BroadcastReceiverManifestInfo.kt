package com.knight.kotlin.library_permiss.manifest.node

/**
 * @Description BroadcastReceiver 清单信息类
 * @Author knight
 * @Time 2025/7/10 20:44
 *
 */

class BroadcastReceiverManifestInfo {
    /** BroadcastReceiver 的类名  */

    var name: String = ""

    /** 所使用到的权限  */

    var permission: String? = null

    /** 意图过滤器列表  */

    var intentFilterInfoList: MutableList<IntentFilterManifestInfo> = mutableListOf()

    /** MetaData 列表  */

    var metaDataInfoList: MutableList<MetaDataManifestInfo> = mutableListOf()
}