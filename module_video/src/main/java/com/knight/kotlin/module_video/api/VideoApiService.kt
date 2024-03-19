package com.knight.kotlin.module_video.api

import com.knight.kotlin.library_network.bean.BaseResponse
import com.knight.kotlin.module_video.entity.VideoCommentList
import com.knight.kotlin.module_video.entity.VideoListEntity
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * Author:Knight
 * Time:2024/2/26 11:29
 * Description:VideoApiService
 */
interface VideoApiService {

    @Headers("BaseUrlName:Video","project_token:AD9D5C1ADEF444DAAB430A3DB71B94CC")
    @POST("douyin/list")
    suspend fun getDouyinVideos(): BaseResponse<MutableList<VideoListEntity>>




    /**
     *
     * 获取视频评论
     */
    @Headers("BaseUrlName:Video","project_token:AD9D5C1ADEF444DAAB430A3DB71B94CC")
    @POST("/jokes/comment/list")
    suspend fun getVideoCommentList(@Query("jokeId") jokeId:Long,@Query("page") page:Long):BaseResponse<VideoCommentList>
}