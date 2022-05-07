package com.knight.kotlin.module_navigate.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.knight.kotlin.library_base.vm.BaseViewModel
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.module_navigate.entity.HierachyListEntity
import com.knight.kotlin.module_navigate.repo.HierachyRepository
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
 * Time:2022/5/6 14:01
 * Description:HierachyVm
 */
@HiltViewModel
class HierachyVm @Inject constructor(private val mRepo: HierachyRepository) : BaseViewModel() {
    //导航数据
    val hierachyList = MutableLiveData<MutableList<HierachyListEntity>>()

    //获取体系数据
    fun getHierachyData() {
        viewModelScope.launch(Dispatchers.IO) {
            mRepo.getHierachyData()
                .onStart {

                }
                .onEach {
                    hierachyList.postValue(it)
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