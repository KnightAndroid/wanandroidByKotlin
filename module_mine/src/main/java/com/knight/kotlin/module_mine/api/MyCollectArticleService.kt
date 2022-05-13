package com.knight.kotlin.module_mine.api

import com.knight.kotlin.library_network.bean.BaseResponse
import com.knight.kotlin.module_mine.entity.MyCollectArticleListEntity
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Author:Knight
 * Time:2022/5/13 10:22
 * Description:MyCollectArticleService
 */
interface MyCollectArticleService {

    /**
     * 获取自己收藏的文章
     *
     */
    @GET("lg/collect/list/{page}/json?page_size=10")
    suspend fun getMyCollectArticles(@Path("page") page:Int): BaseResponse<MyCollectArticleListEntity>

    /**
     *
     * 取消收藏
     */
    @POST("lg/uncollect_originId/{unCollectArticleId}/json")
    suspend fun unCollectArticle(@Path("unCollectArticleId") unCollectArticleId:Int):BaseResponse<Any>
}