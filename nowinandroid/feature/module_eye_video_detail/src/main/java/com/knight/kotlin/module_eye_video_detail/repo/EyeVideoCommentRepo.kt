package com.knight.kotlin.module_eye_video_detail.repo

import com.core.library_base.ktx.dimissLoadingDialog
import com.knight.kotlin.library_base.repository.BaseRepository
import com.knight.kotlin.library_network.bean.EyeApiResponse
import com.knight.kotlin.module_eye_video_detail.api.EyeVideoCommentCommentApi
import com.knight.kotlin.module_eye_video_detail.entity.EyeVideoResultComment
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @Description
 * @Author knight
 * @Time 2025/5/20 20:57
 *
 */

class EyeVideoCommentRepo @Inject constructor() : BaseRepository() {

    @Inject
    lateinit var mEyeVideoCommentCommentApi: EyeVideoCommentCommentApi


    /**
     *
     * 获取视频评论列表
     * @param id 卡片id
     * @return
     */
    fun getVideoComments(resource_id:Long,resource_type:String,sort_type:String,failureCallBack:((String?) ->Unit) ?= null) : Flow<EyeApiResponse<EyeVideoResultComment>> = request<EyeApiResponse<EyeVideoResultComment>> ({
        mEyeVideoCommentCommentApi.getVideoComment(resource_id,resource_type,sort_type).run{
            //responseCodeExceptionHandler(errorCode, errorMessage)
            dimissLoadingDialog()
            emit(this)
        }

    }){
        dimissLoadingDialog()
        failureCallBack?.run {
            this(it)
        }
    }

}