package com.knight.kotlin.module_mine.api

import com.knight.kotlin.library_common.entity.UserInfoEntity
import com.knight.kotlin.library_network.bean.BaseResponse
import com.knight.kotlin.module_mine.entity.UserInfoMessageEntity
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
    @GET("user/lg/userinfo/json")
    suspend fun getUserInfoCoin(): BaseResponse<UserInfoMessageEntity>

    @FormUrlEncoded
    @POST("user/login")
    suspend fun login(@Field("username") userName:String, @Field("password") passWord:String): BaseResponse<UserInfoEntity>
}