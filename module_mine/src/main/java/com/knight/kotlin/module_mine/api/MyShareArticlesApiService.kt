package com.knight.kotlin.module_mine.api

import com.knight.kotlin.library_network.bean.BaseResponse
import com.knight.kotlin.module_mine.entity.MyShareArticleEntity
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Author:Knight
 * Time:2022/5/17 10:16
 * Description:MyShareArticlesApiService
 */
interface MyShareArticlesApiService {

    /**
     *
     * 取消收藏
     */
    @POST("user/lg/private_articles/{page}/json?page_size=10")
    suspend fun getMyShareArticles(@Path("page") page:Int): BaseResponse<MyShareArticleEntity>


    /**
     *
     * 删除我的分享文章
     */
    //@FormUrlEncoded
    @POST("lg/user_article/delete/{articleId}/json")
    suspend fun deleteMyShareArticles(@Path("articleId") articleId:Int): BaseResponse<Any>
}