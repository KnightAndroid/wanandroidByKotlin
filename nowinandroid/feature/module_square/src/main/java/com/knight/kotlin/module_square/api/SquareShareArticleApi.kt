package com.knight.kotlin.module_square.api

import com.knight.kotlin.library_network.bean.BaseResponse
import com.knight.kotlin.module_square.entity.SquareShareArticleListBean
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Author:Knight
 * Time:2022/4/8 17:37
 * Description:SquareShareArticleApi
 */
interface SquareShareArticleApi {


    @FormUrlEncoded
    @POST("lg/user_article/add/json")
    suspend fun shareArticle(@Field("title") title:String, @Field("link") link:String): BaseResponse<Any>

    /**
     * 获取广场文章数据
     */
    @GET("user_article/list/{page}/json?page_size=10")
    suspend fun getSquareArticles(@Path("page") page:Int) : BaseResponse<SquareShareArticleListBean>

    /**
     *
     * 收藏文章
     */
    @POST("lg/collect/{collectArticleId}/json")
    suspend fun collectArticle(@Path("collectArticleId") collectArticleId:Int):BaseResponse<Any>


    /**
     *
     * 取消收藏
     */
    @POST("lg/uncollect_originId/{unCollectArticleId}/json")
    suspend fun unCollectArticle(@Path("unCollectArticleId") unCollectArticleId:Int):BaseResponse<Any>
}