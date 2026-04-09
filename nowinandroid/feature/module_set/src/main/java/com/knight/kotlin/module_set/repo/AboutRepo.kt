package com.knight.kotlin.module_set.repo

import com.knight.kotlin.library_base.entity.AppUpdateBean
import com.knight.kotlin.library_base.repository.BaseRepository
import com.knight.kotlin.library_network.model.responseCodeExceptionHandler
import com.knight.kotlin.module_set.api.AboutApiService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/8/25 15:06
 * Description:AboutRepo
 */
class AboutRepo @Inject constructor(
    private val api: AboutApiService
) : BaseRepository() {

    /**
     * 检查APP版本更新
     *
     * 只做三件事：
     * 1. 请求接口
     * 2. 校验 code
     * 3. emit data
     */
    fun checkAppUpdateMessage(): Flow<AppUpdateBean> = request {
        val response = api.checkAppUpdateMessage()
        responseCodeExceptionHandler(response.code, response.msg)
        emit(response.data)
    }
}