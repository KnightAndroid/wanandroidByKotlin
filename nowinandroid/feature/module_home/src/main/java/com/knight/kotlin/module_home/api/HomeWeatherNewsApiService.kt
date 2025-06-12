package com.knight.kotlin.module_home.api

import com.knight.kotlin.library_network.bean.BaseResponse
import com.knight.kotlin.module_home.entity.RainHourFallBean
import com.knight.kotlin.module_home.entity.WeatherNewBean
import com.knight.kotlin.module_home.entity.ZaoBaoBean
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query


/**
 * @author created by luguian
 * @organize
 * @Date 2025/3/27 14:17
 * @descript:每天天气 早报
 */
interface HomeWeatherNewsApiService {




    /**
     * 获取每天一图
     */
    @Headers("Domain-Name:bingying")
    @GET("HPImageArchive.aspx")
    suspend fun getTodayImage(@Query("format") format:String,@Query("idx") idx:String,@Query("n") n:String): WeatherNewBean

    /**
     *
     * 得到未来两小时 hourly=precipitation&forecast_hours=2&timezone=Asia/Shanghai
     */
    @Headers("Domain-Name:rainfall")
    @GET("v1/forecast")
    suspend fun getTwoHourRainFall(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude:Double,
        @Query("hourly") hourly:String,
        @Query("forecast_hours") forecast_hours:Int,
        @Query("timezone") timezone:String,
    ) : RainHourFallBean



    /**
     * 获取热早报微语
     */
    @Headers("Domain-Name:zaobao")
    @GET("zaobao")
    suspend fun getZaoBao(): BaseResponse<ZaoBaoBean>

}