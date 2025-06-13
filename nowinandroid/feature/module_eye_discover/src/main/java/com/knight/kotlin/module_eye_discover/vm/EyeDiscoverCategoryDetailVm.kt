package com.knight.kotlin.module_eye_discover.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.knight.kotlin.library_base.ktx.showLoadingDialog
import com.knight.kotlin.library_base.vm.BaseViewModel
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.module_eye_discover.entity.EyeCategoryDetailEntity
import com.knight.kotlin.module_eye_discover.repo.EyeDiscoverCategoryDetailRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


/**
 * @author created by luguian
 * @organize
 * @Date 2024/8/13 17:11
 * @descript:开眼分类详细vm
 */
@HiltViewModel
class EyeDiscoverCategoryDetailVm @Inject constructor(private val mRepo: EyeDiscoverCategoryDetailRepo) : BaseViewModel(){


    /**
     *
     * 获取发现分类详细数据
     */
    fun getDiscoverCategoryDetailData(id: Long): LiveData<EyeCategoryDetailEntity> {
        showLoadingDialog()
        return mRepo.getCategoryDetailData(id, failureCallBack = {
            it?.let { it1 -> toast(it1) }
        }).asLiveData()

    }

    /**
     *
     * 获取更多分类详细数据
     */
    fun getLoadMoreCategoryDetailData(url: String): LiveData<EyeCategoryDetailEntity> {
        return mRepo.getLoadMoreCategoryDetailData(url).asLiveData()

    }





}