package com.knight.kotlin.module_home.repo

import com.knight.kotlin.library_base.repository.BaseRepository
import com.knight.kotlin.library_common.entity.AppUpdateBean
import com.knight.kotlin.library_network.model.responseCodeExceptionHandler
import com.knight.kotlin.module_home.api.HomeApiService
import com.knight.kotlin.module_home.entity.EveryDayPushArticlesBean
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2021/12/27 20:22
 * Description:HomeRepo
 */
class HomeRepo @Inject constructor() : BaseRepository() {
    @Inject
    lateinit var mHomeApiService:HomeApiService

    /**
     * 获取推送文章信息接口
     */
    suspend fun getEveryDayPushArticle() = request<EveryDayPushArticlesBean>{
        mHomeApiService.getEveryDayPushArticle().run {
            responseCodeExceptionHandler(code, msg)
            emit(data)
        }
    }

    /**
     *
     * 检查APP版本更新接口
     */
    suspend fun checkAppUpdateMessage() = request<AppUpdateBean>{
        mHomeApiService.checkAppUpdateMessage().run {
            responseCodeExceptionHandler(code, msg)
            emit(data)
        }
    }






}