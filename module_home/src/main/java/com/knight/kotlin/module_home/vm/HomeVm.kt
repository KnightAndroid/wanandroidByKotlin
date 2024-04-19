package com.knight.kotlin.module_home.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.knight.kotlin.library_base.vm.BaseViewModel
import com.knight.kotlin.library_common.entity.AppUpdateBean
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.module_home.repo.HomeRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2021/12/27 20:13
 * Description:HomeVm
 */
@HiltViewModel
class HomeVm @Inject constructor(private val mRepo:HomeRepo): BaseViewModel(){



    //获取APP版本更新
    val appUpdateMessage = MutableLiveData<AppUpdateBean>()





    /**
     * 检查APP版本更新
     */
    fun checkAppUpdateMessage() {
        viewModelScope.launch {
            mRepo.checkAppUpdateMessage()
                //指定线程
                .flowOn(Dispatchers.IO)
                .onStart {
                    //开始
                }
                .onEach {
                    appUpdateMessage.postValue(it)
                }
                .onCompletion {
                    //结束
                }
                .catch { toast(it.message ?: "") }
                .collect()

        }


    }











}