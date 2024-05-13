package com.knight.kotlin.module_eye_daily.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.knight.kotlin.library_base.ktx.showLoadingDialog
import com.knight.kotlin.library_base.vm.BaseViewModel
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.library_base.entity.EyeDailyListEntity
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
        return mRepo.getDailyBanner(failureCallBack = {
            it?.let { it1 -> toast(it1) }
        }).asLiveData()
    }

    /**
     * 日报数据
     *
     *
     * @param url
     * @return
     */
    fun getDailyList(url:String?):LiveData<EyeDailyListEntity> {
        return url?.let {
            mRepo.getDailyList(url).asLiveData()
        } ?: run {
            MutableLiveData<EyeDailyListEntity>()
        }
    }
}