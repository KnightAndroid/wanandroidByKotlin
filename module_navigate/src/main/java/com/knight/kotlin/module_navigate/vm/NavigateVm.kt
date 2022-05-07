package com.knight.kotlin.module_navigate.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.knight.kotlin.library_base.vm.BaseViewModel
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.module_navigate.entity.NavigateListEntity
import com.knight.kotlin.module_navigate.repo.NavigateRepository
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
 * Time:2022/5/5 15:09
 * Description:NavigateVm
 */
@HiltViewModel
class NavigateVm @Inject constructor(private val mRepo: NavigateRepository) : BaseViewModel() {

    //导航数据
    val navigateList = MutableLiveData<MutableList<NavigateListEntity>>()

    //获取导航数据
    fun getNavigateData() {
        viewModelScope.launch(Dispatchers.IO) {
            mRepo.getNavigateData()
                .onStart {

                }
                .onEach {
                    navigateList.postValue(it)
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