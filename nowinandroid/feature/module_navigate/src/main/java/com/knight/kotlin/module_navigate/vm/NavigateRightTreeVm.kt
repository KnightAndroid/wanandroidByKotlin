package com.knight.kotlin.module_navigate.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.knight.kotlin.library_base.vm.BaseViewModel
import com.knight.kotlin.module_navigate.entity.HierachyListEntity
import com.knight.kotlin.module_navigate.entity.NavigateListEntity
import com.knight.kotlin.module_navigate.repo.NavigateTreeRightRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/5/5 15:47
 * Description:NavigateRightTreeVm
 */
@HiltViewModel
class NavigateRightTreeVm @Inject constructor(private val mRepo: NavigateTreeRightRepository) : BaseViewModel() {

    //获取导航数据
    fun getTreeNavigateData(): LiveData<MutableList<NavigateListEntity>> {
        return mRepo.getTreeNavigateData().asLiveData()
    }

    //获取体系数据
    fun getTreeHierachyData():LiveData<MutableList<HierachyListEntity>> {
        return mRepo.getTreeHierachyData().asLiveData()
    }


}