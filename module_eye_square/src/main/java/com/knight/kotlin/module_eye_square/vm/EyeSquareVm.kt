package com.knight.kotlin.module_eye_square.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.knight.kotlin.library_base.entity.EyeCardListEntity
import com.knight.kotlin.library_base.vm.BaseViewModel
import com.knight.kotlin.module_eye_square.repo.EyeSquareRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


/**
 * @author created by luguian
 * @organize
 * @Date 2025/2/11 15:27
 * @descript:开眼广场vm
 */
@HiltViewModel
class EyeSquareVm @Inject constructor(private val mRepo:EyeSquareRepo) : BaseViewModel() {


    /**
     *
     * 获取社区/广场数据
     */
    fun getSquareDataByTypeAndLabel(page_type:String, page_label:String):LiveData<EyeCardListEntity> {
         return mRepo.getSquareDataByTypeAndLabel(page_type, page_label).asLiveData()
     }


    /**
     *
     * 获取上滑加载更多数据
     */
    fun getSquareMoreData(url:String,params:MutableMap<String,String>): LiveData<EyeCardListEntity> {
        return mRepo.getSquareMoreData(url,params).asLiveData()
    }

}