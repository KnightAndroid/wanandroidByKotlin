package com.knight.kotlin.module_mine.api

import com.knight.kotlin.library_network.bean.BaseResponse
import com.knight.kotlin.module_mine.entity.CoinRankListEntity
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Author:Knight
 * Time:2022/5/7 13:48
 * Description:CoinRankApiService
 */
interface CoinRankApiService {

    /**
     * 获取积分排行榜
     *
     */
    @GET("coin/rank/{page}/json?page_size=10")
    suspend fun getRankCoin(@Path("page") page:Int): BaseResponse<CoinRankListEntity>
}