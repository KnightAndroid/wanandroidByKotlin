package com.knight.kotlin.module_mine.repo

import com.knight.kotlin.library_base.repository.BaseRepository
import com.knight.kotlin.library_network.model.responseCodeExceptionHandler
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.module_mine.api.CoinRankApiService
import com.knight.kotlin.module_mine.entity.CoinRankListEntity
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/5/7 13:55
 * Description:CoinRankRepo
 */
class CoinRankRepo @Inject constructor() : BaseRepository() {
    @Inject
    lateinit var mCoinRankApiService: CoinRankApiService

    /**
     *
     * 获取积分排行榜
     */
    fun getRankCoin(page:Int) = request<CoinRankListEntity>({
        mCoinRankApiService.getRankCoin(page).run {
            responseCodeExceptionHandler(code,msg)
            emit(data)
        }
    }){
        it?.run {
            toast(it)
        }
    }
}