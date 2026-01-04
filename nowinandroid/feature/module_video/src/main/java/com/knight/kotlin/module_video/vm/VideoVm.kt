package com.knight.kotlin.module_video.vm

import com.core.library_base.vm.BaseMviViewModel
import com.knight.kotlin.library_base.config.EyeTypeConstants
import com.knight.kotlin.module_video.contract.VideoMainContract
import com.knight.kotlin.module_video.entity.VideoPlayEntity
import com.knight.kotlin.module_video.repo.VideoRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2024/2/26 14:18
 * Description:VideoVm
 */
@HiltViewModel
class VideoVm @Inject constructor(
    private val repo: VideoRepo
) : BaseMviViewModel<
        VideoMainContract.Event,
        VideoMainContract.State,
        VideoMainContract.Effect>() {

    override fun initialState() = VideoMainContract.State()

    override fun handleEvent(event: VideoMainContract.Event) {
        when (event) {

            // 首次进入页面 / 普通加载
            VideoMainContract.Event.GetVideos -> {
                getVideos(isRefresh = false)
            }

            // 下拉刷新
            VideoMainContract.Event.RefreshVideos -> {
                getVideos(isRefresh = true)
            }

            is VideoMainContract.Event.GetVideoCommentList -> {
                getVideoCommentList(event.playerId)
            }
        }
    }

    /**
     * 获取视频列表
     */
    private fun getVideos(isRefresh: Boolean) {

        // 设置加载状态
        setState {
            copy(
                loading = !isRefresh,
                refreshing = isRefresh
            )
        }

        requestFlowMvi(
            block = {
                repo.getVideos()
            },
            onEach = { data ->

                // =========================
                // 还原你原来 Activity 里的逻辑：
                // 1. 过滤 TEXT_HEAD
                // 2. 转成 VideoPlayEntity
                // =========================
                val (_, followCardList) = data.itemList.partition {
                    it.type == EyeTypeConstants.TEXT_HEAD_TYPE
                }

                val resultList = mutableListOf<VideoPlayEntity>()

                for (i in followCardList.size - 1 downTo 0) {
                    val item = followCardList[i].data.content.data

                    resultList.add(
                        VideoPlayEntity(
                            item.id,
                            item.author!!.id,
                            item.playUrl,
                            item.cover!!.feed,
                            "1080,1920",
                            item.author!!.name,
                            item.author!!.icon,
                            item.description,
                            item.consumption.collectionCount,
                            item.consumption.shareCount,
                            item.consumption.replyCount,
                            0,
                            false,
                            false,
                            false
                        )
                    )
                }

                setState {
                    copy(
                        videos = resultList,
                        loading = false,
                        refreshing = false,
                        errorMsg = null
                    )
                }
            },
            onError = { e ->
                setState {
                    copy(
                        loading = false,
                        refreshing = false,
                        errorMsg = e.message
                    )
                }

                setEffect {
                    VideoMainContract.Effect.ShowToast(
                        e.message ?: "获取视频失败"
                    )
                }
            }
        )
    }


    /**
     * 获取评论
     * 现在只是放在 State 里留以后用
     */
    private fun getVideoCommentList(playerId: Long) {
        requestFlowMvi(
            block = {
                repo.getVideoCommentList(playerId)
            },
            onEach = { data ->
                setState {
                    copy(comments = data)
                }
            },
            onError = { e ->
                setEffect {
                    VideoMainContract.Effect.ShowToast(
                        e.message ?: "获取评论失败"
                    )
                }
            }
        )
    }
}
