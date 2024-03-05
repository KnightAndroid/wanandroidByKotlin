package com.knight.kotlin.module_video.repo

import com.knight.kotlin.library_base.repository.BaseRepository
import com.knight.kotlin.library_network.model.responseCodeExceptionHandler
import com.knight.kotlin.module_video.api.VideoApiService
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
}