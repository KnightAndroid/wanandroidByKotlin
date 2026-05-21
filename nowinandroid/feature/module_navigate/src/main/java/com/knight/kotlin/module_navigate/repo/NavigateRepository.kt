package com.knight.kotlin.module_navigate.repo

import com.knight.kotlin.library_base.repository.BaseRepository
import com.knight.kotlin.library_network.model.responseCodeExceptionHandler
import com.knight.kotlin.module_navigate.api.NavigateApi
import com.knight.kotlin.module_navigate.entity.NavigateListEntity
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/5/5 14:58
 * Description:NavigateRepository
 */
class NavigateRepository @Inject constructor(
    private val api: NavigateApi
) : BaseRepository() {

    /**
     * 获取导航数据
     */
    fun getNavigateData() =
        request<MutableList<NavigateListEntity>> {

            val res = api.getNavigateData()

            responseCodeExceptionHandler(
                res.code,
                res.msg
            )

            emit(res.data)
        }
}