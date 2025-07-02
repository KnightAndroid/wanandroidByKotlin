package com.knight.kotlin.module_navigate.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.core.library_base.vm.BaseViewModel
import com.knight.kotlin.module_navigate.entity.NavigateListEntity
import com.knight.kotlin.module_navigate.repo.NavigateRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/5/5 15:09
 * Description:NavigateVm
 */
@HiltViewModel
class NavigateVm @Inject constructor(private val mRepo: NavigateRepository) : BaseViewModel() {

    //获取导航数据
    fun getNavigateData() : LiveData<MutableList<NavigateListEntity>> {
        return mRepo.getNavigateData().asLiveData()
    }
}