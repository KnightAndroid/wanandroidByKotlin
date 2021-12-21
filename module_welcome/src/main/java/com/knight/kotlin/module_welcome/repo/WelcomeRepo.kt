package com.knight.kotlin.module_welcome.repo

import com.knight.kotlin.library_base.repository.BaseRepository
import com.knight.kotlin.library_network.model.responseCodeExceptionHandler
import com.knight.kotlin.module_welcome.api.WelcomeApiService
import com.knight.kotlin.module_welcome.entity.AppThemeBean
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2021/12/21 18:00
 * Description:WelcomeRepo
 * 闪屏模块数据仓库层
 */
class WelcomeRepo @Inject constructor() : BaseRepository(){

    @Inject
    lateinit var mWelcomeApi:WelcomeApiService

    suspend fun getAppTheme() = request<AppThemeBean>{
        mWelcomeApi.getAppTheme().run {
            responseCodeExceptionHandler(code, msg)
            emit(data)
        }
    }
}