package com.knight.kotlin.module_set.api

import com.knight.kotlin.library_network.bean.BaseResponse
import retrofit2.http.GET

/**
 * Author:Knight
 * Time:2022/3/30 15:20
 * Description:SetApiService
 */
interface SetApiService {

    /**
     * 退出账号
     */
    @GET("user/logout/json")
    suspend fun logout(): BaseResponse<Any>
}