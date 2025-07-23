package com.knight.kotlin.module_home.api

import com.knight.kotlin.library_network.bean.BaseResponse
import com.knight.kotlin.module_home.entity.ZaoBaoAudioBean
import com.knight.kotlin.module_home.entity.ZaoBaoBean
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query


/**
 * @author created by luguian
 * @organize
 * @Date 2025/4/11 15:36
 * @descript:
 */
interface HomeNewsApiService {


    /**
     * 获取热早报微语
     */
    @Headers("Domain-Name:zaobao")
    @GET("api/sixs")
    suspend fun getNews(@Query("type") type: String): ZaoBaoBean



    /**
     * 获取热早报微语
     */
    @Headers("Domain-Name:zaobaoother")
    @GET("zaobao")
    suspend fun getAudio(): BaseResponse<ZaoBaoAudioBean>
}