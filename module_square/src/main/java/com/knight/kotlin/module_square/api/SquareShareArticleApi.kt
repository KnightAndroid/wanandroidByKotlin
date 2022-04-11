package com.knight.kotlin.module_square.api

import com.knight.kotlin.library_network.bean.BaseResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * Author:Knight
 * Time:2022/4/8 17:37
 * Description:SquareShareArticleApi
 */
interface SquareShareArticleApi {


    @FormUrlEncoded
    @POST("lg/user_article/add/json")
    suspend fun shareArticle(@Field("title") title:String, @Field("link") link:String): BaseResponse<Any>

}