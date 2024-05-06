package com.knight.kotlin.module_home.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.knight.kotlin.library_base.vm.BaseViewModel
import com.knight.kotlin.library_common.entity.AppUpdateBean
import com.knight.kotlin.module_home.repo.HomeRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2021/12/27 20:13
 * Description:HomeVm
 */
@HiltViewModel
class HomeVm @Inject constructor(private val mRepo:HomeRepo): BaseViewModel(){

    /**
     * 检查APP版本更新
     */
    fun checkAppUpdateMessage(): LiveData<AppUpdateBean> {
        return mRepo.checkAppUpdateMessage().asLiveData()
    }











}