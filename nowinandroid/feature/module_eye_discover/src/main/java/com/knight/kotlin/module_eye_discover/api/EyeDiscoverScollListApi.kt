package com.knight.kotlin.module_eye_discover.api

import com.core.library_base.config.Appconfig
import com.google.gson.JsonObject
import com.knight.kotlin.library_network.bean.EyeApiResponse
import com.knight.kotlin.module_eye_discover.entity.EyeDiscoverNavEntity
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST


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



    @Headers("Domain-Name:eye_sub")
    @FormUrlEncoded
    @POST("v1/card/page/get_nav")
    suspend fun getNav(@Field("tab_label") tabLabel: String) : EyeApiResponse<EyeDiscoverNavEntity>
}