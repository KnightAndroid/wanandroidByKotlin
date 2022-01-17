package com.knight.kotlin.module_home.api

import com.knight.kotlin.library_common.entity.AppUpdateBean
import com.knight.kotlin.library_network.bean.BaseResponse
import com.knight.kotlin.module_home.entity.EveryDayPushArticlesBean
import retrofit2.http.GET
import retrofit2.http.Headers

/**
 * Author:Knight
 * Time:2021/12/27 19:50
 * Description:HomeApiService
 */
interface HomeApiService {

    /**
     * 获取每日推荐文章
     */
    @Headers("BaseUrlName:gitee")
    @GET("MengSuiXinSuoYuan/wanandroid_server/raw/master/wanandroid_config/advert/dailyPushArticles.json")
    suspend fun getEveryDayPushArticle(): BaseResponse<EveryDayPushArticlesBean>


    /**
     * 版本更新接口
     */
    @Headers("BaseUrlName:gitee")
    @GET("MengSuiXinSuoYuan/wanandroid_server/raw/master/wanandroid_config/kotlin/update.json")
    suspend fun checkAppUpdateMessage(): BaseResponse<AppUpdateBean>









}