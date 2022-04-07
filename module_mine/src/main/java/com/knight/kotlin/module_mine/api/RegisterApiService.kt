package com.knight.kotlin.module_mine.api

import com.knight.kotlin.library_base.entity.UserInfoEntity
import com.knight.kotlin.library_network.bean.BaseResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * Author:Knight
 * Time:2022/4/6 14:51
 * Description:RegisterApiService
 */
interface RegisterApiService {

    @FormUrlEncoded
    @POST("user/register")
    suspend fun register(@Field("username") userName:String, @Field("password") passWord:String,@Field("repassword") repassword:String): BaseResponse<UserInfoEntity>
}