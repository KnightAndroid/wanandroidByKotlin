package com.knight.kotlin.module_eye_video_detail.repo

import com.knight.kotlin.library_base.entity.EyeDailyListEntity
import com.knight.kotlin.library_base.ktx.dimissLoadingDialog
import com.knight.kotlin.library_base.repository.BaseRepository
import com.knight.kotlin.library_network.model.responseCodeExceptionHandler
import com.knight.kotlin.module_eye_video_detail.api.EyeVideoDetailApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class EveVideoDetailRepo @Inject constructor() : BaseRepository() {

    @Inject
    lateinit var mEyeVideoDetailAPi: EyeVideoDetailApi


    /**
     *
     * 获取视频详情
     * @param id 卡片id
     * @return
     */
    fun getVideoDetail(id:Long,failureCallBack:((String?) ->Unit) ?= null) : Flow<EyeDailyListEntity> = request<EyeDailyListEntity> ({
        mEyeVideoDetailAPi.getVideoDetail(id).run{
            responseCodeExceptionHandler(errorCode, errorMessage)
            dimissLoadingDialog()
        }

    }){
        dimissLoadingDialog()
        failureCallBack?.run {
            this(it)
        }
    }




}