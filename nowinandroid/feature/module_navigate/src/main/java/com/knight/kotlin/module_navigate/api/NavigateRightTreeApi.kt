package com.knight.kotlin.module_navigate.api

import com.knight.kotlin.library_network.bean.BaseResponse
import com.knight.kotlin.module_navigate.entity.HierachyListEntity
import com.knight.kotlin.module_navigate.entity.NavigateListEntity
import retrofit2.http.GET

/**
 * Author:Knight
 * Time:2022/5/5 15:30
 * Description:NavigateRightTreeApi
 */
interface NavigateRightTreeApi {

    /**
     * 获取导航数据
     */
    @GET("navi/json")
    suspend fun getTreeNavigateData(): BaseResponse<MutableList<NavigateListEntity>>


    /**
     * 获取体系数据
     */
    @GET("tree/json")
    suspend fun getTreeHierachyData(): BaseResponse<MutableList<HierachyListEntity>>
}