package com.knight.kotlin.module_utils.repo

import com.knight.kotlin.library_base.repository.BaseRepository
import com.knight.kotlin.library_network.model.responseCodeExceptionHandler
import com.knight.kotlin.module_utils.api.UtilsApi
import com.knight.kotlin.module_utils.entity.UtilsEntity
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/6/2 14:50
 * Description:UtilsRepo
 */
class UtilsRepo @Inject constructor() : BaseRepository() {

    @Inject
    lateinit var mUtilsApi: UtilsApi

    /**
     * 获取问答文章列表
     */
    suspend fun getUtils() = request<MutableList<UtilsEntity>>{
        mUtilsApi.getUtils().run {
            responseCodeExceptionHandler(code, msg)
            emit(data)
        }
    }
}