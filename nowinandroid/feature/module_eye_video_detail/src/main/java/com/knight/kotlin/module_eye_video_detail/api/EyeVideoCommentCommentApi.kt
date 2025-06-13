package com.knight.kotlin.module_eye_video_detail.api

import com.knight.kotlin.library_network.bean.EyeApiResponse
import com.knight.kotlin.module_eye_video_detail.entity.EyeVideoResultComment
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * @Description
 * @Author knight
 * @Time 2025/5/20 20:57
 *
 */

interface EyeVideoCommentCommentApi {


    /**
     * 获取视频评论
     *
     *
     * @return
     */
    @Headers("Domain-Name:eye_sub")
    @POST("v1/item/comment/get_cms_comment_list")
    suspend fun getVideoComment(@Query("resource_id") resource_id: Long,@Query("resource_type") resource_type: String,@Query("sort_type") sort_type: String): EyeApiResponse<EyeVideoResultComment>
}