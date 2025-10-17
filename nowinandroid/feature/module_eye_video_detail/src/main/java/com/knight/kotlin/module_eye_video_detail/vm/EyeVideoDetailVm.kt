package com.knight.kotlin.module_eye_video_detail.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.core.library_base.vm.BaseViewModel
import com.knight.kotlin.library_network.bean.EyeApiResponse
import com.knight.kotlin.module_eye_video_detail.entity.EyeRelateListEntity
import com.knight.kotlin.module_eye_video_detail.entity.EyeVideoDetailResponseEntity
import com.knight.kotlin.module_eye_video_detail.repo.EyeVideoDetailRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class EyeVideoDetailVm @Inject constructor(private val mRepo: EyeVideoDetailRepo) : BaseViewModel() {


    /**
     *
     * 获取关联视频数据
     */
    fun getRelateVideoList(id:Long,failureCallBack:((String?) ->Unit) ?= null):LiveData<EyeRelateListEntity> {
        return mRepo.getRelateVideoList(id,failureCallBack).asLiveData()
    }


    /**
     *
     * 获取视频详情
     */
    fun getVideoDetail(resource_id:Long,resource_type:String,failureCallBack:((String?) ->Unit) ?= null): LiveData<EyeApiResponse<EyeVideoDetailResponseEntity>> {
        return mRepo.getVideoDetail(resource_id,resource_type,failureCallBack).asLiveData()

    }


}