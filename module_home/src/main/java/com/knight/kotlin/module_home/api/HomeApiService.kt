package com.knight.kotlin.module_home.api

import com.knight.kotlin.library_base.entity.UserInfoEntity
import com.knight.kotlin.library_common.entity.AppUpdateBean
import com.knight.kotlin.library_network.bean.BaseResponse
import com.knight.kotlin.module_home.entity.EveryDayPushArticlesBean
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

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


    /**
     * 登录接口
     *
     */
    @FormUrlEncoded
    @POST("user/login")
    suspend fun login(@Field("username") userName:String, @Field("password") passWord:String): BaseResponse<UserInfoEntity>












}