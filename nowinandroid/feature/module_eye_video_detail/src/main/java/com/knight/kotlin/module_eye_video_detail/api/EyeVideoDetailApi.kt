package com.knight.kotlin.module_eye_video_detail.api

import com.knight.kotlin.library_network.bean.EyeApiResponse
import com.knight.kotlin.module_eye_video_detail.entity.EyeRelateListEntity
import com.knight.kotlin.module_eye_video_detail.entity.EyeVideoDetailResponseEntity
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * Author:Knight
 * Time:2024/5/13 15:43
 * Description:EyeVideoDetailApi
 */
interface EyeVideoDetailApi {

    /**
     * 获取关联视频数据
     *
     *
     * @return
     */
    @Headers("Domain-Name:eye")
    @POST("v4/video/related")
    suspend fun getRelateVideoList(@Query("id") id: Long): EyeRelateListEntity


    /**
     * 获取视频详情
     *
     */
    @Headers("Domain-Name:eye_sub")
    @GET("v1/content/item/get_item_detail_v2")
    suspend fun getVideoDetail(@Query("resource_id") resource_id : Long,@Query("resource_type")resource_type :String): EyeApiResponse<EyeVideoDetailResponseEntity>
}