package com.knight.kotlin.module_constellate.api

import com.knight.kotlin.library_network.bean.BaseResponse
import com.knight.kotlin.module_constellate.entity.ConstellateFortuneEntity
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query


/**
 * @author created by luguian
 * @organize
 * @Date 2025/8/6 9:35
 * @descript:星座运势接口
 */
interface ConstellateFortuneApi {




    //https://api.vvhan.com/api/horoscope?type=scorpio&time=today 由于本接口及其不稳定改用 http://api.suxun.site/api/constellation?type=scorpio&time=nextday
    @Headers("Domain-Name:constellate")
    @GET("api/constellation")
    suspend fun getConstellateFortune(@Query("type") type: String, @Query("time") time:String) : BaseResponse<ConstellateFortuneEntity>





}