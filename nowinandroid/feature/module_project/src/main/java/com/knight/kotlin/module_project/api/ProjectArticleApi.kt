package com.knight.kotlin.module_project.api

import com.knight.kotlin.library_network.bean.BaseResponse
import com.knight.kotlin.module_project.entity.ProjectArticleListBean
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Author:Knight
 * Time:2022/4/28 18:38
 * Description:ProjectArticleApi
 */
interface ProjectArticleApi {

    /**
     * 获取项目列表数据
     */
    @GET("project/list/{page}/json?page_size=10")
    suspend fun getProjectArticle(@Path("page") page:Int,@Query("cid") cid:Int):BaseResponse<ProjectArticleListBean>

    /**
     * 获取最新的项目列表文章数据
     *
     */
    @GET("article/listproject/{page}/json?page_size=10")
    suspend fun getNewProjectArticle(@Path("page") page:Int):BaseResponse<ProjectArticleListBean>

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