package com.knight.kotlin.module_video.player

import com.google.android.exoplayer2.ExoPlayer

/**
 * Author:Knight
 * Time:2024/3/5 9:47
 * Description:Iplayer
 */
interface Iplayer {
    fun playVideo(url: String)

    fun getplayer(): ExoPlayer

    fun play()

    fun pause()

    fun stop()

    fun release()

    fun isPlaying(): Boolean
}