package com.knight.kotlin.library_database.repository

import com.knight.kotlin.library_base.repository.BaseRepository
import com.knight.kotlin.library_database.dao.HistoryReadRecordsDao
import com.knight.kotlin.library_database.db.AppDataBase
import com.knight.kotlin.library_database.entity.HistoryReadRecordsEntity

/**
 * Author:Knight
 * Time:2022/5/17 15:23
 * Description:HistoryReadRecordsRepository
 */
class HistoryReadRecordsRepository : BaseRepository(){

    private val mDao: HistoryReadRecordsDao = AppDataBase.getInstance()?.mHistoryReadRecordsDao()!!
    companion object {
        private lateinit var instance: HistoryReadRecordsRepository
        fun getInstance(): HistoryReadRecordsRepository {
            if (!::instance.isInitialized) {
                instance = HistoryReadRecordsRepository()
            }
            return instance
        }
    }

    /**
     *
     * 插入阅读历史记录
     */
    suspend fun insertHistoryReadRecord(historyReadRecordsEntity: HistoryReadRecordsEntity) = request<Long>{
        mDao.insertHistoryReadRecords(historyReadRecordsEntity).run {
            emit(this)
        }
    }

    /**
     *
     * 更新阅读历史记录
     */
    suspend fun updateHistoryReadRecord(historyReadRecordsEntity: HistoryReadRecordsEntity) = request<Int>{
        mDao.updateHistoryReadRecord(historyReadRecordsEntity).run {
            emit(this)
        }
    }

    /**
     *
     * 查询部分阅读历史
     */
    fun queryPartHistoryRecords(start:Int,end:Int,userId:Int,failureCallBack:((String?) ->Unit) ?= null) = request<MutableList<HistoryReadRecordsEntity>> ({
        mDao.queryPartHistoryRecords(start, end, userId).run {
            emit(this)
        }
    }){
        failureCallBack?.run {
            this(it)
        }
    }

    /**
     *
     * 查询一条
     */
    suspend fun findHistoryReadRecords(webUrl:String,articleId:Int,userId:Int) = request<HistoryReadRecordsEntity?> {
        mDao.findHistoryReadRecords(webUrl, articleId, userId).run {
            emit(this)
        }
    }

    /**
     *
     * 删除单个
     */
    fun deleteHistoryRecord(id:Long,failureCallBack:((String?) ->Unit) ?= null) = request<Unit> ({
        mDao.deleteHistoryRecordsById(id).run {
            emit(Unit)
        }

    }){
        failureCallBack?.run {
            this(it)
        }
    }

    /**
     *
     * 删除全部
     */
    fun deleteAllHistoryRecord(failureCallBack:((String?) ->Unit) ?= null) = request<Unit> ({
        mDao.deleteAllHistoryRecords().run {
            emit(Unit)
        }
    }){
        failureCallBack?.run {
            this(it)
        }
    }


}