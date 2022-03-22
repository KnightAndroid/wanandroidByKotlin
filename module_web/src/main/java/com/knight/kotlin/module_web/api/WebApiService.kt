package com.knight.kotlin.module_web.api

import com.knight.kotlin.library_network.bean.BaseResponse
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Author:Knight
 * Time:2022/2/22 11:12
 * Description:WebApiService
 * WebService接口代理类
 */
interface  WebApiService {

    /**
     * 文章点赞/收藏
     *
     */
    @POST("lg/collect/{collectArticleId}/json")
    suspend fun collectArticle(@Path("collectArticleId") collectArticleId:Int): BaseResponse<Any>
}