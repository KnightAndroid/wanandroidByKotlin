package com.knight.kotlin.module_eye_video_detail.api

import com.knight.kotlin.library_base.entity.EyeDailyListEntity
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * Author:Knight
 * Time:2024/5/13 15:43
 * Description:EyeVideoDetailApi
 */
interface EyeVideoDetailApi {

    /**
     * 获取视频详情
     *
     *
     * @return
     */
    @Headers("Domain-Name:eye")
    @POST("v4/video/related")
    suspend fun getVideoDetail(@Query("id") id: Long): EyeDailyListEntity
}