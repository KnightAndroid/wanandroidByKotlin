package com.knight.kotlin.module_realtime.repo

import com.knight.kotlin.library_base.repository.BaseRepository
import com.knight.kotlin.library_network.model.responseCodeExceptionHandler
import com.knight.kotlin.module_realtime.api.RealTimeApiService
import javax.inject.Inject


/**
 * @author created by luguian
 * @organize
 * @Date 2025/3/20 9:55
 * @descript:
 */
open class RealTimeBaseRepo @Inject constructor(
    private val api: RealTimeApiService
) : BaseRepository() {

    fun getDataByTab(platform: String, tab: String) =
        request {
            val resp = api.getDataByTab(platform, tab)

            responseCodeExceptionHandler(resp.error.code, resp.error.message)

            emit(resp.data)
        }

    fun getChildDataByTab(platform: String, tab: String, tag: String) =
        request {
            val resp = api.getChildDataByTab(platform, tab, tag)

            responseCodeExceptionHandler(resp.error.code, resp.error.message)

            emit(resp.data)
        }
}