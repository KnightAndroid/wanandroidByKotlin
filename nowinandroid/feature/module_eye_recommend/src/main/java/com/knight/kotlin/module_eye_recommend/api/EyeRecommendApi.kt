package com.knight.kotlin.module_eye_recommend.api

import com.knight.kotlin.library_base.entity.EyeCardListEntity
import com.knight.kotlin.library_network.bean.EyeApiResponse
import com.knight.kotlin.module_eye_recommend.entity.EyeSmallRecommendEntity
import retrofit2.http.Field
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Url


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



    @Headers("Domain-Name:eye_sub")
    @FormUrlEncoded
    @POST
    suspend fun getEyeRecommendMoreData(@Url url :String, @FieldMap params:MutableMap<String,String>) : EyeApiResponse<EyeSmallRecommendEntity>

}