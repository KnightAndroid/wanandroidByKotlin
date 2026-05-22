package com.knight.kotlin.module_navigate.repo

import com.knight.kotlin.library_base.repository.BaseRepository
import com.knight.kotlin.library_network.model.responseCodeExceptionHandler
import com.knight.kotlin.module_navigate.api.NavigateRightTreeApi
import com.knight.kotlin.module_navigate.entity.HierachyListEntity
import com.knight.kotlin.module_navigate.entity.NavigateListEntity
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/5/5 15:42
 * Description:NavigateTreeRightRepository
 */
class NavigateTreeRightRepository @Inject constructor(
    private val api: NavigateRightTreeApi
) : BaseRepository() {

    /**
     * 获取导航数据
     */
    fun getTreeNavigateData() =
        request<MutableList<NavigateListEntity>> {

            val res = api.getTreeNavigateData()

            responseCodeExceptionHandler(
                res.code,
                res.msg
            )

            emit(res.data)
        }

    /**
     * 获取体系数据
     */
    fun getTreeHierachyData() =
        request<MutableList<HierachyListEntity>> {

            val res = api.getTreeHierachyData()

            responseCodeExceptionHandler(
                res.code,
                res.msg
            )

            emit(res.data)
        }
}