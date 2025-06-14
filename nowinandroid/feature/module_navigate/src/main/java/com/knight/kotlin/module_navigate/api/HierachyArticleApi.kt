package com.knight.kotlin.module_navigate.api

import com.knight.kotlin.library_network.bean.BaseResponse
import com.knight.kotlin.module_navigate.entity.HierachyTabArticleListEntity
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Author:Knight
 * Time:2022/5/6 16:00
 * Description:HierachyArticleApi
 */
interface HierachyArticleApi {

    /**
     * 获取体系文章列表
     *
     */
    @GET("article/list/{page}/json?page_size=10")
    suspend fun getHierachyArticle(@Path("page") page:Int,@Query("cid") k:Int): BaseResponse<HierachyTabArticleListEntity>


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