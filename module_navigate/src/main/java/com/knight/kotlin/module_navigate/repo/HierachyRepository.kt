package com.knight.kotlin.module_navigate.repo

import com.knight.kotlin.library_base.repository.BaseRepository
import com.knight.kotlin.library_network.model.responseCodeExceptionHandler
import com.knight.kotlin.module_navigate.api.HierachyApi
import com.knight.kotlin.module_navigate.entity.HierachyListEntity
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/5/6 13:55
 * Description:HierachyRepository
 */
class HierachyRepository @Inject constructor(): BaseRepository() {
    @Inject
    lateinit var mHierachyApi: HierachyApi

    /**
     *
     * 获取最新导航数据
     */
    suspend fun getHierachyData() = request<MutableList<HierachyListEntity>> {
        mHierachyApi.getHierachyData().run {
            responseCodeExceptionHandler(code, msg)
            emit(data)
        }
    }
}