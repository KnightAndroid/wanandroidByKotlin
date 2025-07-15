package com.knight.kotlin.module_set.api

import com.knight.kotlin.library_base.entity.AppUpdateBean
import com.knight.kotlin.library_network.bean.BaseResponse
import retrofit2.http.GET
import retrofit2.http.Headers

/**
 * Author:Knight
 * Time:2022/8/25 15:05
 * Description:AboutApiService
 */
interface AboutApiService {

    /**
     * 版本更新接口
     */
    @Headers("Domain-Name:gitee")
    @GET("MengSuiXinSuoYuan/wanandroid_server/raw/master/wanandroid_config/kotlin/update.json")
    suspend fun checkAppUpdateMessage(): BaseResponse<AppUpdateBean>
}