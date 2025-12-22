package com.knight.kotlin.module_welcome.repo

import com.knight.kotlin.library_base.repository.BaseRepository
import com.knight.kotlin.library_common.entity.AppThemeBean
import com.knight.kotlin.library_network.model.responseCodeExceptionHandler
import com.knight.kotlin.module_welcome.api.WelcomeApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * @author created by luguian
 * @organize
 * @Date 2025/12/22 10:07
 * @descript:
 */
class WelcomeRepo @Inject constructor(
    private val api: WelcomeApiService
) : BaseRepository() {

    /**
     * 获取 App 主题配置
     *
     * 只做三件事：
     * 1. 请求接口
     * 2. 校验 code
     * 3. emit data
     */
    fun getAppTheme(): Flow<AppThemeBean> = flow {
        val response = api.getAppTheme()
        responseCodeExceptionHandler(response.code, response.msg)
        val data = response.data
        emit(data)
    }
}