package com.knight.kotlin.module_eye_discover.repo

import com.knight.kotlin.library_base.repository.BaseRepository
import com.knight.kotlin.library_network.model.responseCodeExceptionHandler
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


    fun getCategoryDetailData (id: Long,failureCallBack:((String?) ->Unit) ?= null) = request<EyeCategoryDetailEntity> ({
        mEyeDiscoverCategoryDetailApi.getCategoryDetailData(id).run {
            responseCodeExceptionHandler(errorCode, errorMessage)
        }

    }){
        failureCallBack?.run {
            this(it)
        }
    }



}