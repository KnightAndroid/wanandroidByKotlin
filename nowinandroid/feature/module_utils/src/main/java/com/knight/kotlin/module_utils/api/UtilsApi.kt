package com.knight.kotlin.module_utils.api

import com.knight.kotlin.library_network.bean.BaseResponse
import com.knight.kotlin.module_utils.entity.UtilsEntity
import retrofit2.http.GET

/**
 * Author:Knight
 * Time:2022/6/2 14:49
 * Description:UtilsApi
 */
interface UtilsApi {


    /**
     * 工具集合
     */
    @GET("tools/list/json")
    suspend fun getUtils(): BaseResponse<MutableList<UtilsEntity>>
}