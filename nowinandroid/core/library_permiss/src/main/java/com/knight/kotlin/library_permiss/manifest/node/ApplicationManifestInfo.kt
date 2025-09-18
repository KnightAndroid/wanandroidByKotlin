package com.knight.kotlin.library_permiss.manifest.node

/**
 * @Description Application 清单信息类
 * @Author knight
 * @Time 2025/7/10 20:44
 * 
 */
class ApplicationManifestInfo {
    /** Application 的类名  */

    var name: String = ""

    /** 是否忽略分区存储特性  */
    var requestLegacyExternalStorage: Boolean = false

    /** MetaData 列表  */

    var metaDataInfoList: MutableList<MetaDataManifestInfo> = mutableListOf()
}