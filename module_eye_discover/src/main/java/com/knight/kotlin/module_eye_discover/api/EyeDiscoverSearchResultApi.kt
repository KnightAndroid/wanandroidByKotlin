package com.knight.kotlin.module_eye_discover.api

import com.knight.kotlin.library_network.bean.EyeApiResponse
import com.knight.kotlin.module_eye_discover.entity.EyeSearchResultEntity
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

/**
 * @Description 搜索结果Api
 * @Author knight
 * @Time 2025/1/2 20:51
 *
 */

interface EyeDiscoverSearchResultApi {


    /**
     *
     * 根据关键字搜索
     */
    @Headers("Domain-Name:eye_sub")
    @POST("v1/search/search/get_search_result_v2")
    @FormUrlEncoded
    suspend fun getSearchResultByQuery(@Field("query") query:String): EyeApiResponse<EyeSearchResultEntity>
}