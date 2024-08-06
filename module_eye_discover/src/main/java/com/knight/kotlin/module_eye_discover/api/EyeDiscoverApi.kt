package com.knight.kotlin.module_eye_discover.api

import com.google.gson.JsonObject
import retrofit2.http.GET
import retrofit2.http.Headers


/**
 * @author created by luguian
 * @organize
 * @Date 2024/8/6 9:10
 * @descript:发现主页接口请求
 */
interface EyeDiscoverApi {


    /**
     *
     * 获取发现数据
     *
     */
    @Headers("Domain-Name:eye")
    @GET("v7/index/tab/discovery")
    suspend fun getDiscoverData() : JsonObject
}