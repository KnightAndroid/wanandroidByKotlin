package com.knight.kotlin.module_video.utils

import android.content.Context
import com.google.android.exoplayer2.database.StandaloneDatabaseProvider
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import com.knight.kotlin.module_video.player.VideoPlayer


/**
 * Author:Knight
 * Time:2024/4/7 15:54
 * Description:VideoCache
 */
class VideoCache {

    companion object {
//        /**
//         * 预加载的大小，每个视频预加载512KB，这个参数可根据实际情况调整
//         */
//        val PRELOAD_LENGTH = 1024 * 1024 * 2
//
//
//        @Volatile
//        private var instance: PreloadManager? = null
//
//        fun getInstance(context: Context) =
//            instance ?: synchronized(this) {
//                instance ?: PreloadManager(context).also { instance = it }
//            }

        @Volatile
        private var sDownloadCache: SimpleCache? = null

        fun getInstance(context: Context) =
            sDownloadCache ?: synchronized(this) {
                val cacheFile = context.cacheDir.resolve("tiktok_cache_file$this.hashCode()")
                sDownloadCache ?: SimpleCache(
                    cacheFile, LeastRecentlyUsedCacheEvictor(
                        VideoPlayer.MAX_CACHE_BYTE
                    ), StandaloneDatabaseProvider(context)
                ).also { sDownloadCache = it }
            }

    }
}