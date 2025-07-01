package com.knight.kotlin.module_eye_recommend.api

import com.core.library_base.entity.EyeCardListEntity
import com.knight.kotlin.library_network.bean.EyeApiResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST


/**
 * @author created by luguian
 * @organize
 * @Date 2025/5/22 11:02
 * @descript:推荐页面api
 */
interface EyeRecommendApi {


    /**
     *
     * 根据label进行请求
     *
     */
    @Headers("Domain-Name:eye_sub")
    @POST("v1/card/page/get_page")
    @FormUrlEncoded
    suspend fun getEyeRecommendData(@Field("page_type") page_type:String, @Field("page_label") page_label:String): EyeApiResponse<EyeCardListEntity>



}