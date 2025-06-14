package com.knight.kotlin.module_realtime.api

import com.knight.kotlin.library_base.entity.BaiduTopRealTimeBean
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query


/**
 * @author created by luguian
 * @organize
 * @Date 2025/3/20 9:56
 * @descript:热搜api
 */
interface RealTimeApiService {





    /**
     * 获取主页热搜新闻
     */
    @Headers("Domain-Name:baidu")
    @GET("board")
    suspend fun getDataByTab(@Query("platform") platform:String, @Query("tab") tab:String): BaiduTopRealTimeBean

    /**
     * 获取子tab热搜数据
     */
    @Headers("Domain-Name:baidu")
    @GET("board")
    suspend fun getChildDataByTab(@Query("platform") platform:String, @Query("tab") tab:String,@Query("tag") tag:String): BaiduTopRealTimeBean


    //https://xzdx.top/api/tophub
    //传入参数： type 类型 type 类型值：
    //短剧 sp 动漫 acg 电影 movie 电视剧 tv 综艺 vs 百度热点指数 baidu 微博热搜榜 weibo 抖音热搜榜 douyin 知乎热榜zhihu

    //历史上的今天 history 少数派热榜 sspai 哔哩哔哩全站日榜 biliall 哔哩哔哩热搜榜 bilihot



    //返回：
    //index 排行、下标
    //icon 图标
    //title 标题
    //author 作者
    //desc 描述
    //cover 封面
    //heat 热度
    //type 类型
    //url 地址
    //text 热点爆款
    //area 地区
    //channel 类别
    //year 年份
    // date 时间
    //avg 评分
    // count 集数

    //https://github.com/baiwumm/next-daily-hot?tab=readme-ov-file

}