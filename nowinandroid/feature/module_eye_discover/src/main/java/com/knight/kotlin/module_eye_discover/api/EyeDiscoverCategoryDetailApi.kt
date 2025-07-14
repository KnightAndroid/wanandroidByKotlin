package com.knight.kotlin.module_eye_discover.api

import com.core.library_common.config.Appconfig
import com.knight.kotlin.module_eye_discover.entity.EyeCategoryDetailEntity
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import retrofit2.http.Url


/**
 * @author created by luguian
 * @organize
 * @Date 2024/8/13 16:33
 * @descript:开眼分类详细页面api
 */
interface EyeDiscoverCategoryDetailApi {

    /**
     *
     * 获取详细分类数据
     *
     */
    @Headers("Domain-Name:eye")
    @GET("v4/categories/videoList?udid=${Appconfig.EYE_UUID}&deviceModel=Android")
    suspend fun getCategoryDetailData(@Query("id") id:Long) : EyeCategoryDetailEntity


    /**
     *
     * 加载分类详细数据
     */
    @Headers("Domain-Name:eye")
    @GET
    suspend fun getLoadMoreCategoryDetailData(@Url url: String): EyeCategoryDetailEntity
}