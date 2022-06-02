package com.knight.kotlin.module_utils.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.knight.kotlin.library_base.vm.BaseViewModel
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.module_utils.entity.UtilsEntity
import com.knight.kotlin.module_utils.repo.UtilsRepo
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
 * Time:2022/6/2 14:56
 * Description:UtilsVm
 */
@HiltViewModel
class UtilsVm @Inject constructor (private val mRepo:UtilsRepo) : BaseViewModel() {

    val utilsList = MutableLiveData<MutableList<UtilsEntity>>()

    /**
     * 获取工具类
     */
    fun getUtils() {
        viewModelScope.launch(Dispatchers.IO) {
            mRepo.getUtils()
                .onStart {

                }
                .onEach { utilsList.postValue(it) }
                .onCompletion {

                }
                .catch {
                    toast(it.message ?: "")

                }
                .collect()
        }
    }
}