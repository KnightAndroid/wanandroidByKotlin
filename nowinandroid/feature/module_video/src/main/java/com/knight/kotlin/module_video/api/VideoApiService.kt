package com.knight.kotlin.module_video.api

import com.core.library_base.entity.EyeDailyListEntity
import com.knight.kotlin.module_video.entity.VideoCommentList
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * Author:Knight
 * Time:2024/2/26 11:29
 * Description:VideoApiService
 */
interface VideoApiService {



    /**
     * 获取视频(为了模仿抖音)
     *
     *
     * @return
     */
    @Headers("Domain-Name:eye")
    @POST("v5/index/tab/feed")
    suspend fun getVideos(): EyeDailyListEntity


    @Headers("Domain-Name:eye")
    @GET("v2/replies/video")
    suspend fun getVideoCommentList(@Query("videoId") videoId:Long):VideoCommentList

}