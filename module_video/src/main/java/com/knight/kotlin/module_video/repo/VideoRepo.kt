package com.knight.kotlin.module_video.repo

import com.knight.kotlin.library_base.repository.BaseRepository
import com.knight.kotlin.library_network.model.responseCodeExceptionHandler
import com.knight.kotlin.module_video.api.VideoApiService
import com.knight.kotlin.module_video.entity.VideoCommentList
import com.knight.kotlin.module_video.entity.VideoListEntity
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2024/2/26 11:31
 * Description:VideoRepo
 */
class VideoRepo @Inject constructor() : BaseRepository() {

    @Inject
    lateinit var mVideoService: VideoApiService

    suspend fun getDouyinVideos() = request<MutableList<VideoListEntity>>{
        mVideoService.getDouyinVideos().run {
            responseCodeExceptionHandler(code, msg)
            emit(data)
        }
    }

    /**
     * 获取视频评论列表
     *
     *
     * @param jokeId
     * @param page
     */
    suspend fun getVideoCommentList(jokeId:Long,page:Long) = request<VideoCommentList> {
        mVideoService.getVideoCommentList(jokeId, page).run {
            responseCodeExceptionHandler(code, msg)
            emit(data)
        }
    }
}