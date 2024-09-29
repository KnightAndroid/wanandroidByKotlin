package com.knight.kotlin.module_realtime.api

import com.knight.kotlin.library_base.entity.BaiduTopRealTimeBean
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

/**
 * @Description
 * @Author knight
 * @Time 2024/9/28 17:29
 *
 */

interface RealTimeMainApi {

    /**
     * 根据类型获取热搜数据
     */
    @Headers("Domain-Name:baidu")
    @GET("board?platform=pc")
    suspend fun getBaiduRealTimeByTab(@Query("tab") tab : String) : BaiduTopRealTimeBean
}