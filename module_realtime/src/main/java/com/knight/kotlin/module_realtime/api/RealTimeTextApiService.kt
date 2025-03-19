package com.knight.kotlin.module_realtime.api

import com.knight.kotlin.library_base.entity.BaiduTopRealTimeBean
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query


/**
 * @author created by luguian
 * @organize
 * @Date 2025/3/19 16:15
 * @descript:民生 财经 热梗
 */
interface RealTimeTextApiService {





    /**
     * 获取主页热搜新闻
     */
    @Headers("Domain-Name:baidu")
    @GET("board")
    suspend fun getDataByTab(@Query("platform") platform:String,@Query("tab") tab:String): BaiduTopRealTimeBean
}