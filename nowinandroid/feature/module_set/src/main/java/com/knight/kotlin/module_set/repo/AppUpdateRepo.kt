package com.knight.kotlin.module_set.repo

import com.knight.kotlin.library_base.repository.BaseRepository
import com.knight.kotlin.library_network.model.responseCodeExceptionHandler
import com.knight.kotlin.module_set.api.AppUpdateRecordService
import com.knight.kotlin.module_set.entity.VersionRecordListEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/8/26 11:06
 * Description:AppUpdateRepo
 */
class AppUpdateRepo @Inject constructor(
    private val api: AppUpdateRecordService
) : BaseRepository() {

    /**
     * 查询APP版本更新记录
     *
     * 只做三件事：
     * 1. 请求接口
     * 2. 校验 code
     * 3. emit data
     */
    fun checkAppUpdateRecord(): Flow<VersionRecordListEntity> = request {
        val response = api.checkAppUpdateRecord()
        responseCodeExceptionHandler(response.code, response.msg)
        emit(response.data)
    }
}