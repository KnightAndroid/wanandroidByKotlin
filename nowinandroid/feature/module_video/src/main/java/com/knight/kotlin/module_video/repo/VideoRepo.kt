package com.knight.kotlin.module_video.repo

import com.core.library_base.ktx.dimissLoadingDialog
import com.knight.kotlin.library_base.entity.EyeDailyListEntity
import com.knight.kotlin.library_base.repository.BaseRepository
import com.knight.kotlin.library_network.model.responseCodeExceptionHandler
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.module_video.api.VideoApiService
import com.knight.kotlin.module_video.entity.VideoCommentList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2024/2/26 11:31
 * Description:VideoRepo
 */
class VideoRepo @Inject constructor(
    private val videoService: VideoApiService
) : BaseRepository() {

    /**
     * 获取视频列表
     */
    fun getVideos(): Flow<EyeDailyListEntity> = flow {
        val response = videoService.getVideos()
        responseCodeExceptionHandler(response.errorCode, response.errorMessage)
        emit(response)
    }

    /**
     * 获取视频评论列表
     */
    fun getVideoCommentList(playerId: Long): Flow<VideoCommentList> = flow {
        val response = videoService.getVideoCommentList(playerId)
        responseCodeExceptionHandler(response.errorCode, response.errorMessage)
        emit(response)
    }
}