package com.knight.kotlin.library_scan.utils

import android.app.Activity
import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import com.knight.kotlin.library_scan.R
import java.io.Closeable
import java.io.IOException

/**
 * Author:Knight
 * Time:2022/2/14 14:02
 * Description:AudioUtil
 */
class AudioUtil(activity:Activity,audioId:Int) : MediaPlayer.OnCompletionListener,MediaPlayer.OnErrorListener,Closeable {

    private val BEEP_VOLUME = 0.10f
    private val VIBRATE_DURATION = 200L

    private var activity: Activity = activity
    private var mediaPlayer: MediaPlayer? = null

    private var audioId  = audioId

    init {
        mediaPlayer = null
        updatePrefs()
    }

    @Synchronized
    private fun updatePrefs() {
        if (mediaPlayer == null) {
            // 设置activity音量控制键控制的音频流
            activity?.let {
                it.volumeControlStream = AudioManager.STREAM_MUSIC
                mediaPlayer = buildMediaPlayer(it)
            }


        }
    }

    /**
     * 开启响铃和震动
     */
    @Synchronized
    fun playBeepSoundAndVibrate() {
        mediaPlayer?.let {
            it.start()
        }
    }

    /**
     * 创建MediaPlayer
     * @param activity
     * @return
     */
    private fun buildMediaPlayer(activity: Context): MediaPlayer? {
        val mediaPlayer = MediaPlayer()
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
        // 监听是否播放完成
        mediaPlayer.setOnCompletionListener(this)
        mediaPlayer.setOnErrorListener(this)
        // 配置播放资源
        return try {
            val file = activity.resources
                .openRawResourceFd(if (audioId == 0) R.raw.beep else audioId)
            try {
                mediaPlayer.setDataSource(
                    file.fileDescriptor,
                    file.startOffset, file.length
                )
            } finally {
                file.close()
            }
            // 设置音量
            mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME)
            mediaPlayer.prepare()
            mediaPlayer
        } catch (ioe: IOException) {
            mediaPlayer.release()
            null
        }
    }

    override fun onCompletion(mp: MediaPlayer?) {
        // When the beep has finished playing, rewind to queue up another one.
        mp?.seekTo(0)
    }

    override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        if (what == MediaPlayer.MEDIA_ERROR_SERVER_DIED) {
            // we are finished, so put up an appropriate error toast if required
            // and finish
            activity?.finish()
        } else {
            // possibly media player error, so release and recreate
            mp?.release()
            mediaPlayer = null
            updatePrefs()
        }
        return true
    }

    override fun close() {
        mediaPlayer?.let {
            it.release()
            mediaPlayer = null
        }
    }
}