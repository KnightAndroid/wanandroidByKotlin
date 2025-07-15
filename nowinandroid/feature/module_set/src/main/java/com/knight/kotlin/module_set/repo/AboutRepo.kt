package com.knight.kotlin.module_set.repo

import com.knight.kotlin.library_base.repository.BaseRepository
import com.knight.kotlin.library_base.entity.AppUpdateBean
import com.knight.kotlin.library_network.model.responseCodeExceptionHandler
import com.knight.kotlin.module_set.api.AboutApiService
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/8/25 15:06
 * Description:AboutRepo
 */
class AboutRepo @Inject constructor() : BaseRepository(){


    @Inject
    lateinit var mAboutApiService:AboutApiService


    /**
     *
     * 检查APP版本更新接口
     */
    fun checkAppUpdateMessage(failureCallBack:((String?) ->Unit) ?= null) = request<AppUpdateBean>({
        mAboutApiService.checkAppUpdateMessage().run {
            responseCodeExceptionHandler(code, msg)
            emit(data)
        }
    }){
        failureCallBack?.run {
            this(it)
        }
    }
}