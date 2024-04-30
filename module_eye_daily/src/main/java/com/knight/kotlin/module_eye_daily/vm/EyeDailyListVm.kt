package com.knight.kotlin.module_eye_daily.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.knight.kotlin.library_base.ktx.showLoadingDialog
import com.knight.kotlin.library_base.vm.BaseViewModel
import com.knight.kotlin.module_eye_daily.entity.EyeDailyListEntity
import com.knight.kotlin.module_eye_daily.repo.EyeDailyListRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2024/4/29 15:10
 * Description:EyeDailyListVm
 */
@HiltViewModel
class EyeDailyListVm @Inject constructor(private val mRepo:EyeDailyListRepo) : BaseViewModel(){


    /**
     * 获取开眼banner数据
     */
    fun getDailyBanner():LiveData<EyeDailyListEntity> {
        showLoadingDialog()
        return mRepo.getDailyBanner().asLiveData()
    }
}