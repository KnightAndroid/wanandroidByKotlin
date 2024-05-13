package com.knight.kotlin.module_eye_daily.api

import com.knight.kotlin.library_base.entity.EyeDailyListEntity
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Url

/**
 * Author:Knight
 * Time:2024/4/29 14:22
 * Description:EyeDailyListApi
 */
interface EyeDailyListApi {

    /**
     * 获取日报广告
     *
     *
     * @return
     */
    @Headers("Domain-Name:eye")
    @POST("v5/index/tab/feed")
    suspend fun getDailyBanner(): EyeDailyListEntity

    /**
     * 日报数据
     *
     *
     * @param url
     * @return
     */
    @GET
    suspend fun getDailyList(@Url url: String): EyeDailyListEntity

}