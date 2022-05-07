package com.knight.kotlin.module_navigate.repo

import com.knight.kotlin.library_base.repository.BaseRepository
import com.knight.kotlin.library_network.model.responseCodeExceptionHandler
import com.knight.kotlin.module_navigate.api.NavigateApi
import com.knight.kotlin.module_navigate.entity.NavigateListEntity
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/5/5 14:58
 * Description:NavigateRepository
 */
class NavigateRepository @Inject constructor(): BaseRepository() {
    @Inject
    lateinit var mNavigateService:NavigateApi



    /**
     *
     * 获取最新导航数据
     */
    suspend fun getNavigateData() = request<MutableList<NavigateListEntity>> {
        mNavigateService.getNavigateData().run {
            responseCodeExceptionHandler(code, msg)
            emit(data)
        }
    }

}