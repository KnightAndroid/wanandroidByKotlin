package com.knight.kotlin.module_eye_square.api

import com.knight.kotlin.library_base.entity.EyeCardListEntity
import com.knight.kotlin.library_network.bean.EyeApiResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST


/**
 * @author created by luguian
 * @organize
 * @Date 2025/2/11 15:06
 * @descript:开眼广场数据APi
 */
interface EyeSquareApi {

    /**
     *
     * 根据关键字搜索
     */
    @Headers("Domain-Name:eye_sub")
    @POST("v1/card/page/get_page")
    @FormUrlEncoded
    suspend fun getSquareDataByTypeAndLabel(@Field("page_type") page_type:String,@Field("page_label") page_label:String): EyeApiResponse<EyeCardListEntity>
}