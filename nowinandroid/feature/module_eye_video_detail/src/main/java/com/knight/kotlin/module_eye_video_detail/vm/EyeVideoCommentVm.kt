package com.knight.kotlin.module_eye_video_detail.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.core.library_base.vm.BaseViewModel
import com.knight.kotlin.library_network.bean.EyeApiResponse
import com.knight.kotlin.module_eye_video_detail.entity.EyeRelateListEntity
import com.knight.kotlin.module_eye_video_detail.entity.EyeVideoResultComment
import com.knight.kotlin.module_eye_video_detail.repo.EyeVideoCommentRepo
import com.knight.kotlin.module_eye_video_detail.repo.EyeVideoDetailRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * @Description
 * @Author knight
 * @Time 2025/5/20 20:56
 *
 */
@HiltViewModel
class EyeVideoCommentVm @Inject constructor(private val mRepo: EyeVideoCommentRepo) : BaseViewModel() {


    /**
     *
     * 获取视频详情数据
     */
    fun getVideoDetail(resource_id:Long,resource_type:String,sort_type:String,failureCallBack:((String?) ->Unit) ?= null): LiveData<EyeApiResponse<EyeVideoResultComment>> {
        return mRepo.getVideoComments(resource_id, resource_type, sort_type,failureCallBack).asLiveData()
    }

}