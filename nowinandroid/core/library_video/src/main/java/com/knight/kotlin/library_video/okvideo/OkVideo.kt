package com.knight.kotlin.library_video.okvideo

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout

/**
 * Author:Knight
 * Time:2024/5/11 15:46
 * Description:OkVideo
 */
open class OkVideo : RelativeLayout {

    enum class State {
        IDLE, NORMAL, PREPARING, PREPARING_CHANGE_URL, PREPARING_PLAYING,
        PREPARED, PLAYING, PAUSE, COMPLETE, ERROR
    }

    enum class Screen {
        NORMAL, FULLSCREEN, TINY
    }

    lateinit var state: State


    constructor(ctx: Context) : super(ctx) {
    }

    constructor(ctx: Context, attrs: AttributeSet) : super(ctx, attrs) {
    }

    companion object {
        @JvmStatic
        fun releaseAllVideos() {

        }
    }

}