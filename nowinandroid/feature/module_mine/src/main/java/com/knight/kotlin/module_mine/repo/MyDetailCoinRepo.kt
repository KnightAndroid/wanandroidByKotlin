package com.knight.kotlin.module_mine.repo

import com.knight.kotlin.library_base.repository.BaseRepository
import com.knight.kotlin.library_network.model.responseCodeExceptionHandler
import com.knight.kotlin.module_mine.api.MyCoinsApiService
import com.knight.kotlin.module_mine.entity.MyDetailCoinListEntity
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/5/11 10:45
 * Description:MyDetailCoinRepo
 */
/**
 * 我的积分明细
 */
class MyDetailCoinRepo @Inject constructor(
    private val api: MyCoinsApiService
) : BaseRepository() {

    /**
     * 获取积分明细
     */
    fun getMyDetailCoin(page: Int) =
        request<MyDetailCoinListEntity> {

            val res = api.getMyDetailCoin(page)

            responseCodeExceptionHandler(
                res.code,
                res.msg
            )

            emit(res.data)
        }
}