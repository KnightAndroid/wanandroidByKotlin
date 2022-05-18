package com.knight.kotlin.module_mine.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.knight.kotlin.library_base.vm.BaseViewModel
import com.knight.kotlin.library_database.entity.HistoryReadRecordsEntity
import com.knight.kotlin.library_database.repository.HistoryReadRecordsRepository
import com.knight.kotlin.module_mine.repo.HistoryRecordsRepo
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
 * Time:2022/5/17 17:12
 * Description:HistoryRecordViewModel
 */
@HiltViewModel
class HistoryRecordViewModel @Inject constructor(private val mRepo: HistoryRecordsRepo) : BaseViewModel() {

    /**
     * 本地数据仓库
     */
    private val repository: HistoryReadRecordsRepository = HistoryReadRecordsRepository()
    val historyReadcords = MutableLiveData<MutableList<HistoryReadRecordsEntity>>()

    /**
     *
     * 删除某项阅读历史成功
     */
    val deleteHistoryRecordSuccess = MutableLiveData<Boolean>()

    /**
     *
     * 删除所有阅读历史成功
     */
    val deleteAllHistoryRecordSuccess = MutableLiveData<Boolean>()
    /**
     * 获取历史阅读数据
     */
    fun queryPartHistoryRecords(start:Int,end:Int,userId:Int) {
        viewModelScope.launch {
            repository.queryPartHistoryRecords(start, end, userId)
                .flowOn(Dispatchers.IO)
                .onStart {

                }
                .onEach {
                    historyReadcords.postValue(it)
                }
                .onCompletion {

                }
                .catch {

                }
                .collect()
        }
    }

    /**
     *
     * 删除某项阅读历史
     */
    fun deleteHistoryRecord(id:Long) {
        viewModelScope.launch {
            repository.deleteHistoryRecord(id)
                .flowOn(Dispatchers.IO)
                .onStart {  }
                .onEach {
                    deleteHistoryRecordSuccess.postValue(true)
                }
                .onCompletion {

                }
                .catch {

                }
                .collect()
        }

    }

    /**
     * 删除所有阅读历史
     *
     */
    fun deleteAllHistoryRecord() {
        viewModelScope.launch {
            repository.deleteAllHistoryRecord()
                .flowOn(Dispatchers.IO)
                .onStart {  }
                .onEach {
                    deleteAllHistoryRecordSuccess.postValue(true)
                }
                .onCompletion {

                }
                .catch {

                }
                .collect()

        }
    }
}