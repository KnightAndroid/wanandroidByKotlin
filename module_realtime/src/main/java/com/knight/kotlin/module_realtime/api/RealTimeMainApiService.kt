package com.knight.kotlin.module_realtime.api

import com.knight.kotlin.library_base.entity.BaiduTopRealTimeBean
import retrofit2.http.GET
import retrofit2.http.Headers

/**
 * @Description
 * @Author knight
 * @Time 2024/9/28 17:29
 *
 */

interface RealTimeMainApiService {




    /**
     * 获取主页热搜新闻
     */
    @Headers("Domain-Name:baidu")
    @GET("board?platform=wise&tab=homepage")
    suspend fun getMainBaiduRealTime(): BaiduTopRealTimeBean
}