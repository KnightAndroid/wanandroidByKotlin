package com.knight.kotlin.library_permiss.manifest.node

/**
 * @Description Activity 清单信息类
 * @Author knight
 * @Time 2025/7/10 20:43
 *
 */

class ActivityManifestInfo {
    /** Activity 的类名 */
    var name: String = ""

    /** 是否支持画中画 */
    var supportsPictureInPicture: Boolean = false

    /** 意图过滤器列表 */
    var intentFilterInfoList: MutableList<IntentFilterManifestInfo> = mutableListOf()

    /** MetaData 列表 */
    var metaDataInfoList: MutableList<MetaDataManifestInfo> = mutableListOf()
}