package com.knight.kotlin.module_eye_discover.api

import com.google.gson.JsonObject
import com.knight.kotlin.library_base.config.Appconfig
import retrofit2.http.GET
import retrofit2.http.Headers


/**
 * @author created by luguian
 * @organize
 * @Date 2024/8/6 9:10
 * @descript:发现主页接口请求
 */
interface EyeDiscoverScollListApi {


    /**
     *
     * 获取发现数据
     *
     */
    @Headers("Domain-Name:eye")
    @GET("v7/index/tab/discovery?udid=${Appconfig.EYE_UUID}&vc=591&vn=6.2.1&size=720X1280&deviceModel=Che1-CL20")
    suspend fun getDiscoverData() : JsonObject
}