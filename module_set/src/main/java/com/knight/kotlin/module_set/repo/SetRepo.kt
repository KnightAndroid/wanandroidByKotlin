package com.knight.kotlin.module_set.repo

import com.knight.kotlin.library_base.repository.BaseRepository
import com.knight.kotlin.library_network.model.responseCodeExceptionHandler
import com.knight.kotlin.module_set.api.SetApiService
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/3/30 15:24
 * Description:SetRepo
 */
class SetRepo @Inject constructor(): BaseRepository() {

    @Inject
    lateinit var mSetApiService: SetApiService

    /**
     * 获取微信文章数据
     *
     */
    suspend fun logout() = request<Any> {
        mSetApiService.logout().run {
            responseCodeExceptionHandler(code, msg)
            emit(data)
        }
    }
}