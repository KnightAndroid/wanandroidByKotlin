package com.knight.kotlin.module_home.api

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
    @Headers("BaseUrlName:gitee")
    @GET("MengSuiXinSuoYuan/wanandroid_server/raw/master/wanandroid_config/advert/dailyPushArticles.json")
    suspend fun getEveryDayPushArticle(): BaseResponse<EveryDayPushArticlesBean>
}