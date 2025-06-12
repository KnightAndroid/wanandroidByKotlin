package com.knight.kotlin.module_home.api

import com.knight.kotlin.library_network.bean.BaseResponse
import com.knight.kotlin.module_home.entity.HomeArticleListBean
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Author:Knight
 * Time:2022/4/20 10:48
 * Description:HomeSearchResultApiService
 */
interface HomeSearchResultApiService {

    /**
     * 根据关键字获取搜索结果
     */
    @FormUrlEncoded
    @POST("article/query/{page}/json?page_size=10")
    suspend fun searchArticleByKeyword(@Path("page") page:Int, @Field("k") keyword:String): BaseResponse<HomeArticleListBean>


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