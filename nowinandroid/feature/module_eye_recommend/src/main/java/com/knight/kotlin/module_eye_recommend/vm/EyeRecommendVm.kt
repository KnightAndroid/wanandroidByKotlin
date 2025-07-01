package com.knight.kotlin.module_eye_recommend.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.core.library_base.entity.EyeCardListEntity
import com.core.library_base.vm.BaseViewModel
import com.knight.kotlin.module_eye_recommend.repo.EyeRecommendRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


/**
 * @author created by luguian
 * @organize
 * @Date 2025/5/23 14:58
 * @descript:开眼推荐vm
 */
@HiltViewModel
class EyeRecommendVm @Inject constructor(private val mRepo:EyeRecommendRepo) : BaseViewModel(){

    /**
     *
     * 获取推荐数据
     */
    fun getRecommendData(page_type:String, page_label:String): LiveData<EyeCardListEntity> {
        return mRepo.getEyeRecommendData(page_type, page_label).asLiveData()
    }

}