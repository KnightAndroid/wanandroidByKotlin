package com.knight.kotlin.module_eye_video_detail.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.knight.kotlin.library_base.entity.EyeDailyListEntity
import com.knight.kotlin.library_base.ktx.showLoadingDialog
import com.knight.kotlin.library_base.vm.BaseViewModel
import com.knight.kotlin.module_eye_video_detail.repo.EyeVideoDetailRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class EyeVideoDetailVm @Inject constructor(private val mRepo: EyeVideoDetailRepo) : BaseViewModel() {


    /**
     *
     * 获取视频详情数据
     */
    fun getVideoDetail(id:Long,failureCallBack:((String?) ->Unit) ?= null):LiveData<EyeDailyListEntity> {
        showLoadingDialog()
        return mRepo.getVideoDetail(id,failureCallBack).asLiveData()
    }


}