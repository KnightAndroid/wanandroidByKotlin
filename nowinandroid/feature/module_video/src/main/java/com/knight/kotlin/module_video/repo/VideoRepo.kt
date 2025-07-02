package com.knight.kotlin.module_video.repo

import com.core.library_base.ktx.dimissLoadingDialog
import com.knight.kotlin.library_base.entity.EyeDailyListEntity
import com.knight.kotlin.library_base.repository.BaseRepository
import com.knight.kotlin.library_network.model.responseCodeExceptionHandler
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.module_video.api.VideoApiService
import com.knight.kotlin.module_video.entity.VideoCommentList
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2024/2/26 11:31
 * Description:VideoRepo
 */
class VideoRepo @Inject constructor() : BaseRepository() {

    @Inject
    lateinit var mVideoService: VideoApiService



    /**
     *
     *
     * 得到广告图
     * @param failureCallBack 失败回调
     * @return
     */
    fun getVideos(failureCallBack:((String?) ->Unit) ?= null): Flow<EyeDailyListEntity> = request<EyeDailyListEntity> ({
        mVideoService.getVideos().run {
            responseCodeExceptionHandler(errorCode, errorMessage)
            dimissLoadingDialog()
            emit(this)
        }
    }) {
        dimissLoadingDialog()
        failureCallBack?.run {
            this(it)
        }
    }

    /**
     * 获取视频评论列表
     *
     *
     * @param jokeId
     * @param page
     */
    fun getVideoCommentList(playerId:Long) = request<VideoCommentList>({
        mVideoService.getVideoCommentList(playerId).run {
            responseCodeExceptionHandler(errorCode, errorMessage)
            emit(this)
        }
    }) {
        it?.run {
            toast(it)
        }
    }
}