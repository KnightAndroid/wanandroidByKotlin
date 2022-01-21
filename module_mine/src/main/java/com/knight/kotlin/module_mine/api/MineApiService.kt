package com.knight.kotlin.module_mine.api

import com.knight.kotlin.library_network.bean.BaseResponse
import com.knight.kotlin.module_mine.entity.UserInfoCoinEntity
import retrofit2.http.GET

/**
 * Author:Knight
 * Time:2022/1/18 17:18
 * Description:MineApiService
 */
interface MineApiService {
    @GET("lg/coin/userinfo/json")
    suspend fun getUserInfoCoin(): BaseResponse<UserInfoCoinEntity>
}