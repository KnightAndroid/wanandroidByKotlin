package com.knight.kotlin.module_home.api

import com.knight.kotlin.library_base.entity.BaiduTopRealTimeBean
import com.knight.kotlin.library_base.entity.UserInfoEntity
import com.knight.kotlin.library_common.entity.OfficialAccountEntity
import com.knight.kotlin.library_network.bean.BaseResponse
import com.knight.kotlin.module_home.entity.BannerBean
import com.knight.kotlin.module_home.entity.EveryDayPushArticlesBean
import com.knight.kotlin.module_home.entity.HomeArticleListBean
import com.knight.kotlin.module_home.entity.TopArticleBean
import com.knight.kotlin.module_home.entity.ZaoBaoBean
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

    /**
     * 获取热早报微语
     */
    @Headers("Domain-Name:zaobao")
    @GET("zaobao")
    suspend fun getZaoBao():BaseResponse<ZaoBaoBean>

    //天气API
    //https://wis.qq.com/weather/common?source=pc&weather_type=observe&province=%E5%9B%9B%E5%B7%9D&city=%E6%88%90%E9%83%BD&county=%E6%88%90%E5%8D%8E%E5%8C%BA
    //https://wis.qq.com/weather/common?source=pc&weather_type=observe|forecast_1h|forecast_24h|index|alarm|limit|tips|rise&province=%E5%9B%9B%E5%B7%9D&city=%E6%88%90%E9%83%BD&county=%E5%8F%8C%E6%B5%81%E5%8C%BA
    //weather_type参数(查询类型，多个|分隔):
    //        observe 当前天气
    //        forecast_1h  48小时天气
    //        forecast_24h  7天天气预报
    //        alarm 预警
    //        tips 天气介绍
    //        index 穿衣，舒适度等等...
    //        air 空气质量
    //        rise 日出
    //https://tianqi.qq.com/
}