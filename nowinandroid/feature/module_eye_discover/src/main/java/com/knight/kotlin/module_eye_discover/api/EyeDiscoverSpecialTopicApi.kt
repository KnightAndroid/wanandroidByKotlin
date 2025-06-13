package com.knight.kotlin.module_eye_discover.api

import com.knight.kotlin.module_eye_discover.entity.EyeSpecialTopicDetailEntity
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path


/**
 * @author created by luguian
 * @organize
 * @Date 2024/8/19 11:04
 * @descript:专题详细API
 */
interface EyeDiscoverSpecialTopicApi {

    /**
     *
     * 获取详细分类数据
     *
     */
    @Headers("Domain-Name:eye")
    @GET("v3/lightTopics/internal/{id}")
    suspend fun getSpecialTopicDetailData(@Path("id") id: Long) : EyeSpecialTopicDetailEntity
}