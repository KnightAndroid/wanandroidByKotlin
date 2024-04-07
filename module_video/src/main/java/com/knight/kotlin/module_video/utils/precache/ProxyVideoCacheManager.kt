package com.knight.kotlin.module_video.utils.precache

import android.content.Context
import com.knight.kotlin.module_video.utils.videocache.HttpProxyCacheServer
import com.knight.kotlin.module_video.utils.videocache.StorageUtils
import java.io.File


/**
 * Author:Knight
 * Time:2024/4/7 10:37
 * Description:ProxyVideoCacheManager
 */
object ProxyVideoCacheManager {

    private var sharedProxy: HttpProxyCacheServer?=null

    fun getProxy(context: Context): HttpProxyCacheServer? {
        return if (sharedProxy == null) newProxy(context).also { sharedProxy = it } else sharedProxy
    }

    private fun newProxy(context: Context): HttpProxyCacheServer {
        return HttpProxyCacheServer.Builder(context)
            .maxCacheSize((512 * 1024 * 1024).toLong()) // 512MB for cache
            //缓存路径，不设置默认在sd_card/Android/data/[app_package_name]/cache中
            //                .cacheDirectory()
            //                .cacheDirectory(new File(Environment.getExternalStorageDirectory().getPath() + "/uu/video-cache/"))
            .build()
    }


    /**
     * 删除所有缓存文件
     *
     * @return 返回缓存是否删除成功
     */
    fun clearAllCache(context: Context): Boolean {
        getProxy(context)
        return StorageUtils.deleteFiles(sharedProxy?.getCacheRoot())
    }

    /**
     * 删除url对应默认缓存文件
     *
     * @return 返回缓存是否删除成功
     */
    fun clearDefaultCache(context: Context, url: String?): Boolean {
        getProxy(context)
        val pathTmp: File = sharedProxy?.getTempCacheFile(url)!!
        val path = sharedProxy!!.getCacheFile(url)
        return StorageUtils.deleteFile(pathTmp.absolutePath) &&
                StorageUtils.deleteFile(path.absolutePath)
    }
}