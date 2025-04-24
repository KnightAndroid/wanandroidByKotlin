package com.knight.kotlin.library_util

import android.os.Handler
import android.os.Looper
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.datasource.cache.LeastRecentlyUsedCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import com.knight.kotlin.library_base.BaseApp
import java.io.File


/**
 * @author created by luguian
 * @organize
 * @Date 2025/4/24 14:45
 * @descript:
 */

@UnstableApi
class Mp3PlayerUtils {


    companion object {
        @Volatile
        private var instance: Mp3PlayerUtils? = null

        fun getInstance(): Mp3PlayerUtils {
            return instance ?: synchronized(this) {
                instance ?: Mp3PlayerUtils().also {
                    instance = it
                }
            }
        }
    }


    private val appContext = BaseApp.context

    private val cacheDir = File(appContext.cacheDir, "media_cache")
    private val cache = SimpleCache(
        cacheDir,
        LeastRecentlyUsedCacheEvictor(100L * 1024L * 1024L),
        StandaloneDatabaseProvider(appContext)
    )

    private val cacheDataSourceFactory = CacheDataSource.Factory()
        .setCache(cache)
        .setUpstreamDataSourceFactory(DefaultDataSource.Factory(appContext))
        .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)

    private val player: ExoPlayer = ExoPlayer.Builder(appContext)
        .setMediaSourceFactory(DefaultMediaSourceFactory(cacheDataSourceFactory))
        .build()

    private val handler = Handler(Looper.getMainLooper())
    private var progressCallback: ((positionMs: Long, durationMs: Long) -> Unit)? = null
    private var progressRunnable: Runnable? = null

    // 回调：播放状态、错误、播放结束
    var onPlayStateChanged: ((isPlaying: Boolean) -> Unit)? = null
    var onError: ((errorMessage: String) -> Unit)? = null
    var onPlaybackEnded: (() -> Unit)? = null

    init {
        player.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                onPlayStateChanged?.invoke(isPlaying)
            }

            override fun onPlayerError(error: PlaybackException) {
                lastProgressPosition = -1L
                onError?.invoke(error.localizedMessage ?: "Unknown Error")
            }

            override fun onPlaybackStateChanged(state: Int) {
                if (state == Player.STATE_ENDED) {
                    lastProgressPosition = -1L
                    onPlaybackEnded?.invoke()
                }
            }
        })
    }

    fun play(url: String, onProgress: ((positionMs: Long, durationMs: Long) -> Unit)? = null) {
        stopProgressUpdates()

        val mediaItem = MediaItem.fromUri(url)
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()

        progressCallback = onProgress
        startProgressUpdates()
    }

    fun pause() = player.pause()
    fun resume() = player.play()
    fun stop() {
        player.stop()
        stopProgressUpdates()
    }

    fun release() {
        player.release()
        cache.release()
        stopProgressUpdates()
    }

    fun isPlaying() = player.isPlaying
    private var lastProgressPosition: Long = -1L
    private fun startProgressUpdates() {
        if (progressCallback == null) return

        progressRunnable = object : Runnable {
            override fun run() {
                val position = player.currentPosition
                val duration = player.duration
                // 只有当进度变化时才回调
                if (position != lastProgressPosition) {
                    lastProgressPosition = position
                    progressCallback?.invoke(position, duration)
                }
                handler.postDelayed(this, 500L)
            }
        }
        handler.post(progressRunnable!!)
    }

    private fun stopProgressUpdates() {
        progressRunnable?.let { handler.removeCallbacks(it) }
        progressRunnable = null
    }


    fun smartTogglePlay(url: String, onProgress: ((position: Long, duration: Long) -> Unit)? = null,onStateChanged: ((isPlaying: Boolean) -> Unit)? = null) {
        when (player.playbackState) {
            Player.STATE_IDLE, Player.STATE_ENDED -> {
                // 第一次播放 或 播放已结束
                play(url, onProgress)
                onStateChanged?.invoke(true) // 默认播放了
            }
            Player.STATE_READY -> {
                if (isPlaying()) {
                    pause()
                    onStateChanged?.invoke(false)
                } else {
                    resume()
                    onStateChanged?.invoke(true)
                }
            }
            else -> {
                // 其他状态不做处理（如 BUFFERING）
            }
        }
    }
}