package com.knight.kotlin.module_vedio.repo

import com.knight.kotlin.library_base.repository.BaseRepository
import com.knight.kotlin.library_network.model.responseCodeExceptionHandler
import com.knight.kotlin.module_vedio.api.VedioApiService
import com.knight.kotlin.module_vedio.entity.VedioListEntity
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2024/2/26 11:31
 * Description:VedioRepo
 */
class VedioRepo @Inject constructor() : BaseRepository() {

    @Inject
    lateinit var mVedioService: VedioApiService

    suspend fun getDouyinVedios() = request<MutableList<VedioListEntity>>{
        mVedioService.getDouyinVedios().run {
            responseCodeExceptionHandler(code, msg)
            emit(data)
        }
    }
}