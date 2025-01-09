package com.knight.kotlin.module_eye_discover.api

import com.knight.kotlin.library_network.bean.EyeApiResponse
import com.knight.kotlin.module_eye_discover.entity.EyeDiscoverMetro
import com.knight.kotlin.module_eye_discover.entity.EyeDiscoverMetroCard
import com.knight.kotlin.module_eye_discover.entity.EyeSearchResultEntity
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Url

/**
 * @Description 搜索Item项api
 * @Author knight
 * @Time 2025/1/5 17:36
 *
 */

interface EyeDiscoverSearchResultItemApi {



    @FormUrlEncoded
    @POST
    suspend fun getMoreDataSearchByQuery(@Url url :String,@FieldMap params:MutableMap<String,Any>) : EyeApiResponse<EyeSearchResultEntity>

}