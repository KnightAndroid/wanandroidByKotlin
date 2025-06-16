package com.knight.kotlin.module_set.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.knight.kotlin.library_base.vm.BaseViewModel
import com.knight.kotlin.module_set.repo.SetRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/3/30 15:35
 * Description:SetVm
 */
@HiltViewModel
class SetVm @Inject constructor(private val mRepo:SetRepo) : BaseViewModel() {


    /**
     * 退出账户
     */
    fun logout():LiveData<Any> {
        return mRepo.logout().asLiveData()
    }
}