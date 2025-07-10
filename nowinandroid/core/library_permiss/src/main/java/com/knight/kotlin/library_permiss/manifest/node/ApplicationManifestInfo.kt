package com.knight.kotlin.library_permiss.manifest.node

/**
 * @Description
 * @Author knight
 * @Time 2025/7/10 20:44
 * 
 */
class ApplicationManifestInfo {
    /**
     * 应用的类名
     */
    var name: String = ""

    /**
     * 是否忽略分区存储特性
     */
    var requestLegacyExternalStorage: Boolean = false
}