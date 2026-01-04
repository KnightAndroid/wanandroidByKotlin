package com.knight.kotlin.module_video.contract

import com.core.library_base.vm.ViewEvent
import com.core.library_base.vm.ViewSideEffect
import com.core.library_base.vm.ViewState
import com.knight.kotlin.module_video.entity.VideoCommentList
import com.knight.kotlin.module_video.entity.VideoPlayEntity

/**
 * @Description
 * @Author knight
 * @Time 2026/1/4 21:23
 *
 */

object VideoMainContract {

    // ========================
    // Event
    // ========================
    sealed class Event : ViewEvent {

        /**
         * 首次进入页面 / 加载视频
         */
        object GetVideos : Event()

        /**
         * 下拉刷新视频
         */
        object RefreshVideos : Event()

        /**
         * 获取视频评论列表
         */
        data class GetVideoCommentList(
            val playerId: Long
        ) : Event()
    }


    // ========================
    // State
    // ========================
    data class State(
        /**
         * 是否正在加载（进入页面 Loading）
         */
        val loading: Boolean = false,

        /**
         * 是否正在刷新（下拉刷新 Loading）
         */
        val refreshing: Boolean = false,

        /**
         * 转换后的 UI 层视频数据
         */
        val videos: List<VideoPlayEntity> = emptyList(),

        /**
         * 评论（给以后用）
         */
        val comments: VideoCommentList? = null,

        /**
         * 错误信息（可空）
         */
        val errorMsg: String? = null
    ) : ViewState


    // ========================
    // Effect
    // ========================
    sealed class Effect : ViewSideEffect {

        /**
         * 只显示一次的 Toast
         */
        data class ShowToast(
            val msg: String
        ) : Effect()
    }
}