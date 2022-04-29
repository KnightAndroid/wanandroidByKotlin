package com.knight.kotlin.module_home.repo

import com.knight.kotlin.library_base.entity.SearchHotKeyEntity
import com.knight.kotlin.library_base.repository.BaseRepository
import com.knight.kotlin.library_network.model.responseCodeExceptionHandler
import com.knight.kotlin.module_home.api.HomeSearchApiService
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/4/12 16:41
 * Description:HomeSearchRepo
 */
class HomeSearchRepo @Inject constructor() : BaseRepository() {
    @Inject
    lateinit var mHomeSearchApiService: HomeSearchApiService

    /**
     * 获取热词
     *
     */
    suspend fun getHotKey() = request<MutableList<SearchHotKeyEntity>>{
        mHomeSearchApiService.getHotKey().run{
            responseCodeExceptionHandler(code, msg)
            emit(data)
        }

    }
}