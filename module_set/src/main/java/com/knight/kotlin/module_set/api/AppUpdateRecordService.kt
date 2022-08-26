package com.knight.kotlin.module_set.api

import com.knight.kotlin.library_network.bean.BaseResponse
import com.knight.kotlin.module_set.entity.VersionRecordListEntity
import retrofit2.http.GET
import retrofit2.http.Headers

/**
 * Author:Knight
 * Time:2022/8/26 10:53
 * Description:AppUpdateRecordService
 */
interface AppUpdateRecordService {

    /**
     * 查看更新日志记录
     */
    @Headers("BaseUrlName:gitee")
    @GET("MengSuiXinSuoYuan/wanandroid_server/raw/master/wanandroid_config/kotlin/versionrecord.json")
    suspend fun checkAppUpdateRecord(): BaseResponse<VersionRecordListEntity>

}