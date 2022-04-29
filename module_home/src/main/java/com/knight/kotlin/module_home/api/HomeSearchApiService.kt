package com.knight.kotlin.module_home.api

import com.knight.kotlin.library_base.entity.SearchHotKeyEntity
import com.knight.kotlin.library_network.bean.BaseResponse
import retrofit2.http.GET

/**
 * Author:Knight
 * Time:2022/4/12 16:32
 * Description:HomeSearchApiService
 */
interface HomeSearchApiService {

    /**
     * 热词
     */
    @GET("hotkey/json")
    suspend fun getHotKey(): BaseResponse<MutableList<SearchHotKeyEntity>>




}