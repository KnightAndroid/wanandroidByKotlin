package com.knight.kotlin.module_set.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.knight.kotlin.library_base.vm.BaseViewModel
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.module_set.entity.VersionRecordListEntity
import com.knight.kotlin.module_set.repo.AppUpdateRepo
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
 * Time:2022/8/26 11:13
 * Description:AppUpdateRecordVm
 */
@HiltViewModel
class AppUpdateRecordVm @Inject constructor(private val mRepo:AppUpdateRepo) : BaseViewModel(){
    //获取APP版本更新日志
    val appUpdateRecordMessage = MutableLiveData<VersionRecordListEntity>()

    /**
     * 检查APP版本更新
     */
    fun checkAppUpdateMessage() {
        viewModelScope.launch {
            mRepo.checkAppUpdateRecord()
                //指定线程
                .flowOn(Dispatchers.IO)
                .onStart {
                    //开始
                }
                .onEach {
                    appUpdateRecordMessage.postValue(it)
                }
                .onCompletion {
                    //结束
                }
                .catch { toast(it.message ?: "") }
                .collect()

        }
    }
}