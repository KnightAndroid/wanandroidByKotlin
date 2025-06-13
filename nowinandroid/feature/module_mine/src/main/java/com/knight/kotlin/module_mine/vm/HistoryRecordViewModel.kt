package com.knight.kotlin.module_mine.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.knight.kotlin.library_base.vm.BaseViewModel
import com.knight.kotlin.library_database.entity.HistoryReadRecordsEntity
import com.knight.kotlin.library_database.repository.HistoryReadRecordsRepository
import com.knight.kotlin.module_mine.repo.HistoryRecordsRepo
import dagger.hilt.android.lifecycle.HiltViewModel
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

    /**
     * 获取历史阅读数据
     */
    fun queryPartHistoryRecords(start:Int,end:Int,userId:Int) : LiveData<MutableList<HistoryReadRecordsEntity>> {
        return repository.queryPartHistoryRecords(start, end, userId).asLiveData()
    }

    /**
     *
     * 删除某项阅读历史
     */
    fun deleteHistoryRecord(id:Long):LiveData<Unit> {
       return repository.deleteHistoryRecord(id).asLiveData()
    }

    /**
     * 删除所有阅读历史
     *
     */
    fun deleteAllHistoryRecord():LiveData<Unit> {
        return repository.deleteAllHistoryRecord().asLiveData()
    }
}