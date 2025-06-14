package com.knight.kotlin.module_navigate.api

import com.knight.kotlin.library_network.bean.BaseResponse
import com.knight.kotlin.module_navigate.entity.NavigateListEntity
import retrofit2.http.GET

/**
 * Author:Knight
 * Time:2022/5/5 14:54
 * Description:NavigateApi
 */
interface NavigateApi {

    /**
     * 获取导航数据
     */
    @GET("navi/json")
    suspend fun getNavigateData(): BaseResponse<MutableList<NavigateListEntity>>
}