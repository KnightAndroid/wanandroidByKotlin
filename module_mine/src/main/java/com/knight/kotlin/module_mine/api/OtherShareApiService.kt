package com.knight.kotlin.module_mine.api

import com.knight.kotlin.library_network.bean.BaseResponse
import com.knight.kotlin.module_mine.entity.OtherShareArticleListEntity
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Author:Knight
 * Time:2022/5/7 16:20
 * Description:OtherShareApiService
 */
interface OtherShareApiService {

    /**
     * 获取他人分享文章
     *
     */
    @GET("user/{uid}/share_articles/{page}/json?page_size=10")
    suspend fun getOtherShareArticle(@Path("uid") uid:Int,@Path("page") page:Int): BaseResponse<OtherShareArticleListEntity>

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