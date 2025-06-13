package com.knight.kotlin.module_mine.api

import com.knight.kotlin.library_network.bean.BaseResponse
import com.knight.kotlin.module_mine.entity.MyDetailCoinListEntity
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Author:Knight
 * Time:2022/5/11 10:42
 * Description:MyCoinsApiService
 */
interface MyCoinsApiService {
    /**
     * 获取他人分享文章
     *
     */
    @GET("lg/coin/list/{page}/json?page_size=10")
    suspend fun getMyDetailCoin(@Path("page") page:Int): BaseResponse<MyDetailCoinListEntity>
}