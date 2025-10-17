package com.knight.kotlin.module_eye_video_detail.repo

import com.core.library_base.ktx.dimissLoadingDialog
import com.knight.kotlin.library_base.repository.BaseRepository
import com.knight.kotlin.library_network.bean.EyeApiResponse
import com.knight.kotlin.library_network.model.responseCodeExceptionHandler
import com.knight.kotlin.module_eye_video_detail.api.EyeVideoDetailApi
import com.knight.kotlin.module_eye_video_detail.entity.EyeRelateListEntity
import com.knight.kotlin.module_eye_video_detail.entity.EyeVideoDetailResponseEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class EyeVideoDetailRepo @Inject constructor() : BaseRepository() {

    @Inject
    lateinit var mEyeVideoDetailAPi: EyeVideoDetailApi


    /**
     *
     * 获取管理视频数据
     * @param id 卡片id
     * @return
     */
    fun getRelateVideoList(id:Long,failureCallBack:((String?) ->Unit) ?= null) : Flow<EyeRelateListEntity> = request<EyeRelateListEntity> ({
        mEyeVideoDetailAPi.getRelateVideoList(id).run{
            responseCodeExceptionHandler(errorCode, errorMessage)
            dimissLoadingDialog()
            emit(this)
        }

    }){
        dimissLoadingDialog()
        failureCallBack?.run {
            this(it)
        }
    }


  //
    /**
     *
     * 获取视频详情
     *
     */
    fun getVideoDetail(resource_id:Long,resource_type:String,failureCallBack:((String?) ->Unit) ?= null):Flow<EyeApiResponse<EyeVideoDetailResponseEntity>> = request<EyeApiResponse<EyeVideoDetailResponseEntity>>({
        mEyeVideoDetailAPi.getVideoDetail(resource_id,resource_type).run{
            responseCodeExceptionHandler(code.toInt(), "")
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