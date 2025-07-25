package com.knight.kotlin.module_eye_daily.repo

import com.core.library_base.ktx.dimissLoadingDialog
import com.knight.kotlin.library_base.repository.BaseRepository
import com.knight.kotlin.library_base.entity.EyeDailyListEntity
import com.knight.kotlin.library_network.model.responseCodeExceptionHandler
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.module_eye_daily.api.EyeDailyListApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2024/4/29 14:31
 * Description:EyeDailyListRepo
 */
class EyeDailyListRepo @Inject constructor() : BaseRepository() {
     @Inject
     lateinit var mEyeDailyListApi : EyeDailyListApi

     /**
      *
      *
      * 得到广告图
      * @param failureCallBack 失败回调
      * @return
      */
     fun getDailyBanner(failureCallBack:((String?) ->Unit) ?= null): Flow<EyeDailyListEntity> = request<EyeDailyListEntity> ({
          mEyeDailyListApi.getDailyBanner().run {
               responseCodeExceptionHandler(errorCode, errorMessage)
               dimissLoadingDialog()
               emit(this)
          }
     }) {
          dimissLoadingDialog()
          failureCallBack?.run {
               this(it)
          }
     }

     /**
      * 日报数据
      *
      *
      * @param url
      * @return
      */
     fun getDailyList(url : String) : Flow<EyeDailyListEntity> = request<EyeDailyListEntity> ({
         mEyeDailyListApi.getDailyList(url).run {
              responseCodeExceptionHandler(errorCode, errorMessage)
              emit(this)
         }

     }){
         it?.run {
              toast(it)
         }
     }

}