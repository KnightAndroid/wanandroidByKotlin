package com.knight.kotlin.module_mine.api

import com.knight.kotlin.library_base.entity.UserInfoEntity
import com.knight.kotlin.library_network.bean.BaseResponse
import com.knight.kotlin.module_mine.entity.UserInfoCoinEntity
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Author:Knight
 * Time:2022/1/18 17:18
 * Description:MineApiService
 */
interface MineApiService {
    @GET("lg/coin/userinfo/json")
    suspend fun getUserInfoCoin(): BaseResponse<UserInfoCoinEntity>

    @FormUrlEncoded
    @POST("user/login")
    suspend fun login(@Field("username") userName:String, @Field("password") passWord:String): BaseResponse<UserInfoEntity>
}