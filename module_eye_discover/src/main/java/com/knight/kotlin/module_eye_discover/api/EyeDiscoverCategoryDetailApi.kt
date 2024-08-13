package com.knight.kotlin.module_eye_discover.api

import com.knight.kotlin.module_eye_discover.entity.EyeCategoryDetailEntity
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query


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
    @GET("v4/categories/videoList?udid=fa53872206ed42e3857755c2756ab683f22d64a&deviceModel=Android")
    suspend fun getCategoryDetailData(@Query("id") id:Long) : EyeCategoryDetailEntity
}