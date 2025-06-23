package com.knight.kotlin.library_util

import android.content.Context
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


/**
 * @author created by luguian
 * @organize
 * @Date 2025/6/23 16:18
 * @descript:apk安装帮助类
 */
object PluginApkInstallerUtils {

    /**
     * 将 assets 中的插件 APK 拷贝到 App 私有目录中（/data/data/xxx/files/plugins）
     * @return 返回拷贝后的 apk 绝对路径
     */
    fun installFromAssets(context: Context, assetApkName: String): String? {
        val pluginDir = File(context.filesDir, "plugins")
        if (!pluginDir.exists()) pluginDir.mkdirs()

        val pluginApkFile = File(pluginDir, assetApkName)
        if (pluginApkFile.exists()) pluginApkFile.delete() // 每次都覆盖

        return try {
            context.assets.open(assetApkName).use { inputStream ->
                FileOutputStream(pluginApkFile).use { output ->
                    inputStream.copyTo(output)
                }
            }
            pluginApkFile.absolutePath
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}