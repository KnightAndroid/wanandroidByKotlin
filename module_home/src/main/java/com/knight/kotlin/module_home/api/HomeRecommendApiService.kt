package com.knight.kotlin.module_home.api

import com.knight.kotlin.library_base.entity.BaiduTopRealTimeBean
import com.knight.kotlin.library_base.entity.UserInfoEntity
import com.knight.kotlin.library_common.entity.OfficialAccountEntity
import com.knight.kotlin.library_network.bean.BaseResponse
import com.knight.kotlin.module_home.entity.BannerBean
import com.knight.kotlin.module_home.entity.EveryDayPushArticlesBean
import com.knight.kotlin.module_home.entity.HomeArticleListBean
import com.knight.kotlin.module_home.entity.TopArticleBean
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Author:Knight
 * Time:2022/2/18 10:15
 * Description:HomeRecommendApiService
 */
interface HomeRecommendApiService {


    /**
     * 获取每日推荐文章
     */
    @Headers("Domain-Name:gitee")
    @GET("MengSuiXinSuoYuan/wanandroid_server/raw/master/wanandroid_config/advert/dailyPushArticles.json")
    suspend fun getEveryDayPushArticle(): BaseResponse<EveryDayPushArticlesBean>
    /**
     *
     * 获取置顶文章
     */
    @GET("article/top/json")
    suspend fun getTopArticle(): BaseResponse<MutableList<TopArticleBean>>


    /**
     * 获取广告数据
     */
    @GET("banner/json")
    suspend fun getBanner():BaseResponse<MutableList<BannerBean>>


    /**
     * 获取首页文章数据
     */
    @GET("article/list/{page}/json?page_size=10")
    suspend fun getHomeArticle(@Path("page") page:Int):BaseResponse<HomeArticleListBean>

    /**
     * 获取热搜新闻
     */
    @Headers("Domain-Name:baidu")
    @GET("board?platform=pc&tab=realtime")
    suspend fun getBaiduRealTime() : BaiduTopRealTimeBean

    /**
     * 获取公众号数据
     *
     */
    @GET("wxarticle/chapters/json")
    suspend fun getOfficialAccount():BaseResponse<MutableList<OfficialAccountEntity>>

    /**
     *
     * 获取未读消息
     */
    @GET("message/lg/count_unread/json")
    suspend fun getUnreadMessage():BaseResponse<Int>


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

    /**
     * 登录接口
     *
     */
    @FormUrlEncoded
    @POST("user/login")
    suspend fun login(@Field("username") userName:String, @Field("password") passWord:String): BaseResponse<UserInfoEntity>


}