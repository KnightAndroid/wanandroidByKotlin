package com.knight.kotlin.module_video.player

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.BaseMediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import androidx.media3.exoplayer.trackselection.TrackSelector
import com.knight.kotlin.module_video.databinding.VideoPlayviewBinding
import com.knight.kotlin.module_video.utils.VideoCache

/**
 * Author:Knight
 * Time:2024/3/5 9:48
 * Description:VideoPlayer 视频播放器类
 */
@UnstableApi
class VideoPlayer @JvmOverloads constructor(context: Context, attrs : AttributeSet? = null) : FrameLayout(context,attrs),Iplayer,DefaultLifecycleObserver{

    private val trackSelector : TrackSelector = DefaultTrackSelector(context)


    private val mPlayer : ExoPlayer by lazy {
        ExoPlayer.Builder(context).
         setTrackSelector(trackSelector).build()
    }

    //自定义 DefaultLoadControl 参数
    val MIN_BUFFER_MS = 5_000 //最小缓冲时间
    val MAX_BUFFER_MS = 7_000 //最大缓冲时间
    val PLAYBACK_BUFFER_MS = 700 //最小播放缓冲时间，只有缓冲到达这个时间后才是可播放状态
    val REBUFFER_MS = 1_000 // 当缓冲用完，再次缓冲的时间
    val loadControl = DefaultLoadControl.Builder()
        .setPrioritizeTimeOverSizeThresholds(true) //缓冲时间优先级高于大小
        .setBufferDurationsMs(MIN_BUFFER_MS, MAX_BUFFER_MS, PLAYBACK_BUFFER_MS, REBUFFER_MS)


    private val binding : VideoPlayviewBinding = VideoPlayviewBinding.inflate(LayoutInflater.from(context),this,true)

    companion object {
        const val MAX_CACHE_BYTE: Long = 1024 * 1024 * 200 //200MB
    }

    init {
        initPlayer()
    }

    override fun onResume(owner:LifecycleOwner) {
        super.onResume(owner)
        //返回时，推荐页面可见，则继续播放视频

    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        pause()
    }


    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        stop()
    }


    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        release()
    }

    private fun initPlayer() {
        binding.playerview.player = mPlayer
        binding.playerview.useController = false
        mPlayer.playWhenReady = true
        mPlayer.repeatMode = Player.REPEAT_MODE_ALL

    }


    fun playVideo(mediaSource: BaseMediaSource) {
        mPlayer.setMediaSource(mediaSource)
        mPlayer.prepare()

    }

//    val cache : SimpleCache by lazy {
//        val cacheFile = context.cacheDir.resolve("video_cache_file")
//        SimpleCache(cacheFile,LeastRecentlyUsedCacheEvictor(MAX_CACHE_BYTE),StandaloneDatabaseProvider(context))
//    }

    /**
     *
     *
     * 根据url生成缓存
     * @param url
     */
    override fun playVideo(url: String) {
        if (TextUtils.isEmpty(url)) {
            return
        }

        val mediaItem = MediaItem.fromUri(url)
        val dataSourceFactory = CacheDataSource.Factory().setCache(VideoCache.getInstance(context)).setUpstreamDataSourceFactory(DefaultDataSource.Factory(context))
        val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(mediaItem)
        mPlayer.setMediaSource(mediaSource)
        mPlayer.prepare()
        mPlayer.play()

    }

    override fun getplayer(): ExoPlayer {
        return mPlayer
    }

    override fun play() {
        mPlayer.play()
    }

    override fun pause() {
        mPlayer.pause()
    }

    override fun stop() {
        mPlayer.stop()
    }

    override fun release() {
        mPlayer?.let {
            it.release()
        }
    }

    override fun isPlaying(): Boolean  = mPlayer.isPlaying

}