package com.knight.kotlin.module_square.api

import com.knight.kotlin.library_network.bean.BaseResponse
import com.knight.kotlin.module_square.entity.SquareArticleListBean
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Author:Knight
 * Time:2022/4/11 11:21
 * Description:SquareArticleApi
 */
interface SquareArticleApi {


    /**
     * 获取标签类文章数据
     */
    @FormUrlEncoded
    @POST("article/query/{page}/json?page_size=10")
    suspend fun getArticlebyTag(@Path("page") page:Int,@Field("k") keyword:String): BaseResponse<SquareArticleListBean>


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