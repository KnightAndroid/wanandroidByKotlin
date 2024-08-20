package com.knight.kotlin.module_eye_discover.repo

import com.knight.kotlin.library_base.ktx.dimissLoadingDialog
import com.knight.kotlin.library_base.repository.BaseRepository
import com.knight.kotlin.library_network.model.responseCodeExceptionHandler
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.module_eye_discover.api.EyeDiscoverSpecialTopicApi
import com.knight.kotlin.module_eye_discover.entity.EyeSpecialTopicDetailEntity
import javax.inject.Inject


/**
 * @author created by luguian
 * @organize
 * @Date 2024/8/19 14:45
 * @descript:专题类Repo
 */
class EyeDiscoverSpecialTopicRepo @Inject constructor() : BaseRepository() {


    @Inject
    lateinit var mEyeDiscoverSpecialTopical: EyeDiscoverSpecialTopicApi



    /**
     * 获取详细专题数据
     */
    fun getDiscoverSpecialTopicalDetail(id:Long) = request<EyeSpecialTopicDetailEntity> ({
        mEyeDiscoverSpecialTopical.getSpecialTopicDetailData(id).run {
            dimissLoadingDialog()
            responseCodeExceptionHandler(errorCode, errorMessage)
            emit(this)
        }

    }){
        dimissLoadingDialog()
        it?.let { it1 -> toast(it1) }
    }
}