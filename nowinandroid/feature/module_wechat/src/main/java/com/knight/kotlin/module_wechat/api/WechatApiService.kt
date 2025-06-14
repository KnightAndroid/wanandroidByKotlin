package com.knight.kotlin.module_wechat.api

import com.knight.kotlin.library_network.bean.BaseResponse
import com.knight.kotlin.module_wechat.entity.WechatArticleListEntity
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 *
 * @ProjectName:    wanandroid
 * @Package:        com.knight.kotlin.module_wechat.api
 * @ClassName:      WechatApiService
 * @Description:    java类作用描述
 * @Author:         knight
 * @CreateDate:     2022/3/19 11:28 上午
 * @UpdateUser:     更新者
 * @UpdateDate:     2022/3/19 11:28 上午
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */

interface WechatApiService {

    /**
     * 获取公众号数据
     */
    @GET("wxarticle/list/{cid}/{page}/json")
    suspend fun getWechatArticle(@Path("cid") cid:Int,@Path("page") page:Int): BaseResponse<WechatArticleListEntity>

    /**
     * 文章点赞/收藏
     *
     */
    @POST("lg/collect/{collectArticleId}/json")
    suspend fun collectArticle(@Path("collectArticleId") collectArticleId:Int): BaseResponse<Any>


    /**
     *
     * 文章取消点赞
     */
    @POST("lg/uncollect_originId/{uncollectArticleId}/json")
    suspend fun uncollectArticle(@Path("uncollectArticleId") uncollectArticleId:Int):BaseResponse<Any>

    /**
     *
     * 根据关键字搜索微信文章
     */
    @GET("wxarticle/list/{cid}/{page}/json")
    suspend fun getWechatArticleByKeywords(@Path("cid") cid:Int,@Path("page") page:Int,@Query("k") k:String):BaseResponse<WechatArticleListEntity>
}