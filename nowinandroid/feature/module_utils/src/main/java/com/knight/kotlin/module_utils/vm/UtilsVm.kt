package com.knight.kotlin.module_utils.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.knight.kotlin.library_base.vm.BaseViewModel
import com.knight.kotlin.module_utils.entity.UtilsEntity
import com.knight.kotlin.module_utils.repo.UtilsRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/6/2 14:56
 * Description:UtilsVm
 */
@HiltViewModel
class UtilsVm @Inject constructor (private val mRepo:UtilsRepo) : BaseViewModel() {

    /**
     * 获取工具类
     */
    fun getUtils() : LiveData<MutableList<UtilsEntity>> {
        return mRepo.getUtils().asLiveData()
    }
}