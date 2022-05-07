package com.knight.kotlin.module_navigate.api

import com.knight.kotlin.library_network.bean.BaseResponse
import com.knight.kotlin.module_navigate.entity.HierachyListEntity
import retrofit2.http.GET

/**
 * Author:Knight
 * Time:2022/5/6 13:53
 * Description:HierachyApi
 */
interface HierachyApi {
    /**
     * 获取体系数据
     */
    @GET("tree/json")
    suspend fun getHierachyData(): BaseResponse<MutableList<HierachyListEntity>>
}