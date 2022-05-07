package com.knight.kotlin.module_navigate.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.knight.kotlin.library_base.vm.BaseViewModel
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.module_navigate.entity.HierachyListEntity
import com.knight.kotlin.module_navigate.entity.NavigateListEntity
import com.knight.kotlin.module_navigate.repo.NavigateTreeRightRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/5/5 15:47
 * Description:NavigateRightTreeVm
 */
@HiltViewModel
class NavigateRightTreeVm @Inject constructor(private val mRepo: NavigateTreeRightRepository) : BaseViewModel() {

    //导航数据
    val navigateTreeList = MutableLiveData<MutableList<NavigateListEntity>>()

    //体系数据
    val hierachyTreeList = MutableLiveData<MutableList<HierachyListEntity>>()

    //获取导航数据
    fun getTreeNavigateData() {
        viewModelScope.launch(Dispatchers.IO) {
            mRepo.getTreeNavigateData()
                .onStart {

                }
                .onEach {
                    navigateTreeList.postValue(it)
                }
                .onCompletion {

                }
                .catch {
                    toast(it.message ?: "")

                }
                .collect()
        }

    }

    //获取体系数据
    fun getTreeHierachyData() {
        viewModelScope.launch(Dispatchers.IO) {
            mRepo.getTreeHierachyData()
                .onStart {

                }
                .onEach {
                    hierachyTreeList.postValue(it)
                }
                .onCompletion {

                }
                .catch {
                    toast(it.message ?: "")

                }
                .collect()
        }

    }


}