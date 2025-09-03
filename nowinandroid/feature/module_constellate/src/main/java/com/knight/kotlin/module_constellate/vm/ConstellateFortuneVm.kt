package com.knight.kotlin.module_constellate.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.core.library_base.ktx.showLoadingDialog
import com.core.library_base.vm.BaseViewModel
import com.knight.kotlin.module_constellate.entity.ConstellateResponseEntity
import com.knight.kotlin.module_constellate.repo.ConstellateFortuneRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


/**
 * @author created by luguian
 * @organize
 * @Date 2025/8/6 14:43
 * @descript:星座运势
 */
@HiltViewModel
class ConstellateFortuneVm @Inject constructor(private val mRepo : ConstellateFortuneRepo)  : BaseViewModel(){


    /**
     *
     * 根据类型星座运势
     */
    fun getConstellateFortune(type:String,time:String) : LiveData<ConstellateResponseEntity> {
        showLoadingDialog()
        return mRepo.getConstellateFortune(type,time).asLiveData()
        }





}