package com.knight.kotlin.module_home.repo

import com.knight.kotlin.library_base.repository.BaseRepository
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

    suspend fun getEveryDayPushArticle() = request<EveryDayPushArticlesBean>{
        mHomeApiService.getEveryDayPushArticle().run {
            responseCodeExceptionHandler(code, msg)
            emit(data)
        }
    }
}