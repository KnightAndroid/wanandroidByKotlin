package com.knight.kotlin.module_square.api

import com.core.library_base.entity.SearchHotKeyEntity
import com.knight.kotlin.library_network.bean.BaseResponse
import com.knight.kotlin.module_square.entity.SquareQuestionListBean
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Author:Knight
 * Time:2022/4/27 16:49
 * Description:SquareApi
 */
interface SquareApi {


    /**
     * 获取问答文章数据
     */
    @GET("wenda/list/{page}/json?page_size=10")
    suspend fun getQuestions(@Path("page") page:Int): BaseResponse<SquareQuestionListBean>

    /**
     *
     * 请求热词
     */
    @GET("hotkey/json")
    suspend fun getHotKey(): BaseResponse<MutableList<SearchHotKeyEntity>>


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