package com.knight.kotlin.module_welcome.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.core.library_base.vm.BaseViewModel

import com.knight.kotlin.module_welcome.entity.AppThemeBean
import com.knight.kotlin.module_welcome.repo.WelcomeRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2021/12/21 18:12
 * Description:WelcomeVm
 * 闪屏模块VM层
 */
@HiltViewModel
class WelcomeVm @Inject constructor(private val mRepo:WelcomeRepo) : BaseViewModel(){

    /**
     *
     * 获取App Theme
     *
     */
    fun getAppTheme() : LiveData<AppThemeBean> {
        return mRepo.getAppTheme().asLiveData()
    }


}