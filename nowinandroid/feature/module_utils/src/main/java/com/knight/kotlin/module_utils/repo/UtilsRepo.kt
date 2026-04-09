package com.knight.kotlin.module_utils.repo

import com.knight.kotlin.library_base.repository.BaseRepository
import com.knight.kotlin.library_network.model.responseCodeExceptionHandler
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.module_utils.api.UtilsApi
import com.knight.kotlin.module_utils.entity.UtilsEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
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
//    fun getUtils() = request<MutableList<UtilsEntity>>({
//        mUtilsApi.getUtils().run {
//            responseCodeExceptionHandler(code, msg)
//            emit(data)
//        }
//    }){
//        it?.run {
//            toast(it)
//        }
//    }


    fun getUtils() : Flow<MutableList<UtilsEntity>> = request {
        val response = mUtilsApi.getUtils()
        responseCodeExceptionHandler(response.code, response.msg)
        val data = response.data
        emit(data)
    }
}