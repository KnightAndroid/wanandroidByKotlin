package com.knight.kotlin.module_set.repo

import com.knight.kotlin.library_base.repository.BaseRepository
import com.knight.kotlin.library_network.model.responseCodeExceptionHandler
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.module_set.api.AppUpdateRecordService
import com.knight.kotlin.module_set.entity.VersionRecordListEntity
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/8/26 11:06
 * Description:AppUpdateRepo
 */
class AppUpdateRepo @Inject constructor() : BaseRepository() {

    @Inject
    lateinit var mAppUpdateRecordService: AppUpdateRecordService


    /**
     *
     * 查询APP版本更新记录
     */
    fun checkAppUpdateRecord() = request<VersionRecordListEntity>({
        mAppUpdateRecordService.checkAppUpdateRecord().run {
            responseCodeExceptionHandler(code, msg)
            emit(data)
        }
    }){
        it?.run {
            toast(it)
        }
    }
}