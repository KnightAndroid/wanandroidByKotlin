package com.knight.kotlin.module_navigate.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.knight.kotlin.library_base.vm.BaseViewModel
import com.knight.kotlin.module_navigate.entity.HierachyListEntity
import com.knight.kotlin.module_navigate.repo.HierachyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/5/6 14:01
 * Description:HierachyVm
 */
@HiltViewModel
class HierachyVm @Inject constructor(private val mRepo: HierachyRepository) : BaseViewModel() {

    //获取体系数据
    fun getHierachyData() : LiveData<MutableList<HierachyListEntity>> {
        return mRepo.getHierachyData().asLiveData()

    }
}