package com.knight.kotlin.module_eye_daily.api

import com.knight.kotlin.module_eye_daily.entity.EyeDailyListEntity
import retrofit2.http.Headers
import retrofit2.http.POST

/**
 * Author:Knight
 * Time:2024/4/29 14:22
 * Description:EyeDailyListApi
 */
interface EyeDailyListApi {

    @Headers("Domain-Name:eye")
    @POST("v5/index/tab/feed")
    suspend fun getDailyBanner(): EyeDailyListEntity
}