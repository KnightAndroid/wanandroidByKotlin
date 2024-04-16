package com.knight.kotlin.module_welcome.api

import com.knight.kotlin.library_network.bean.BaseResponse
import com.knight.kotlin.module_welcome.entity.AppThemeBean
import retrofit2.http.GET
import retrofit2.http.Headers

/**
 * Author:Knight
 * Time:2021/12/21 17:31
 * Description:WelcomeApiService
 * 闪屏模块接口代理类
 */
interface WelcomeApiService {

    @Headers("Domain-Name:gitee")
    @GET("MengSuiXinSuoYuan/wanandroid_server/raw/master/wanandroid_config/config/themeColor.json")
    suspend fun getAppTheme():BaseResponse<AppThemeBean>
}