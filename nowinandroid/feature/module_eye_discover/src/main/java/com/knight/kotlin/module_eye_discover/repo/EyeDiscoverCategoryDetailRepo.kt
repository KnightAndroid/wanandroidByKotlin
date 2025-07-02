package com.knight.kotlin.module_eye_discover.repo

import com.core.library_base.ktx.dimissLoadingDialog
import com.knight.kotlin.library_base.repository.BaseRepository
import com.knight.kotlin.library_network.model.responseCodeExceptionHandler
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.module_eye_discover.api.EyeDiscoverCategoryDetailApi
import com.knight.kotlin.module_eye_discover.entity.EyeCategoryDetailEntity
import javax.inject.Inject


/**
 * @author created by luguian
 * @organize
 * @Date 2024/8/13 16:43
 * @descript:分类详细 Repo
 */
class EyeDiscoverCategoryDetailRepo @Inject constructor() : BaseRepository() {

    @Inject
    lateinit var mEyeDiscoverCategoryDetailApi : EyeDiscoverCategoryDetailApi

    /**
     * 首次获取分类详细数据
     */
    fun getCategoryDetailData (id: Long,failureCallBack:((String?) ->Unit) ?= null) = request<EyeCategoryDetailEntity> ({
        mEyeDiscoverCategoryDetailApi.getCategoryDetailData(id).run {
            dimissLoadingDialog()
            responseCodeExceptionHandler(errorCode, errorMessage)
            emit(this)
        }

    }){
        dimissLoadingDialog()
        failureCallBack?.run {
            this(it)
        }
    }

    /**
     * 获取更多的分类数据
     */
    fun getLoadMoreCategoryDetailData(url:String) = request<EyeCategoryDetailEntity> ({
        mEyeDiscoverCategoryDetailApi.getLoadMoreCategoryDetailData(url).run {
            responseCodeExceptionHandler(errorCode, errorMessage)
            emit(this)
        }

    }){
        it?.let { it1 -> toast(it1) }
    }




}