package com.knight.kotlin.module_mine.repo

import com.knight.kotlin.library_base.repository.BaseRepository
import com.knight.kotlin.library_network.model.responseCodeExceptionHandler
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.module_mine.api.MyCoinsApiService
import com.knight.kotlin.module_mine.entity.MyDetailCoinListEntity
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/5/11 10:45
 * Description:MyDetailCoinRepo
 */
class MyDetailCoinRepo @Inject constructor() : BaseRepository() {
    @Inject
    lateinit var mMyCoinsApiService: MyCoinsApiService

    /**
     *
     * 获取自己积分明细
     */
    fun getMyDetailCoin(page:Int) = request<MyDetailCoinListEntity>({
        mMyCoinsApiService.getMyDetailCoin(page).run {
            responseCodeExceptionHandler(code,msg)
            emit(data)
        }
    }){
        it?.run {
            toast(it)
        }
    }
}