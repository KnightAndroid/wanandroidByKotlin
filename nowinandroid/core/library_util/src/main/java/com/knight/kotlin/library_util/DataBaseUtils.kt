package com.knight.kotlin.library_util

import com.core.library_common.ktx.getUser
import com.knight.kotlin.library_database.entity.HistoryReadRecordsEntity
import com.knight.kotlin.library_database.entity.SearchHistroyKeywordEntity
import com.knight.kotlin.library_database.repository.HistoryReadRecordsRepository
import com.knight.kotlin.library_database.repository.HistroyKeywordsRepository

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.util.Date
import kotlin.coroutines.CoroutineContext

/**
 * Author:Knight
 * Time:2022/4/19 11:35
 * Description:DataBaseUtils
 */
object DataBaseUtils : CoroutineScope by CoroutineScope(Dispatchers.Main) {

    var job: Job = Job()

    // CoroutineScope 的实现
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    /**
     *
     * 保存搜索记录
     */
    public fun saveSearchKeyword(keyword: String) = launch {
        HistroyKeywordsRepository.getInstance()?.insertHistroyKeyword(
            SearchHistroyKeywordEntity(
                0, keyword,
                Date()
            )
        )
            ?.flowOn(Dispatchers.IO)
            ?.onStart {
                //开始
            }
            ?.onEach {

            }
            ?.onCompletion {
                //结束
            }
            ?.catch { toast(it.message ?: "") }
            ?.collect()
    }

    /**
     *
     * 保存阅读历史记录
     */
    fun saveHistoryRecord(historyReadRecordEntity: HistoryReadRecordsEntity) = launch {
        HistoryReadRecordsRepository.getInstance().findHistoryReadRecords(
            historyReadRecordEntity.webUrl, historyReadRecordEntity.articleId,
            getUser()?.id ?: 0
        )
            .flowOn(Dispatchers.IO)
            .onStart {

            }
            .onEach {
                it?.let {
                    HistoryReadRecordsRepository.getInstance().updateHistoryReadRecord(it)
                        .flowOn(Dispatchers.IO)
                        .onStart {  }
                        .onEach {  }
                        .onCompletion {  }
                        .catch {  }
                        .collect {  }
                } ?: run {

                    HistoryReadRecordsRepository.getInstance().insertHistoryReadRecord(historyReadRecordEntity)
                        .flowOn(Dispatchers.IO)
                        .onStart {  }
                        .onEach {  }
                        .onCompletion {  }
                        .catch {  }
                        .collect {  }

                }

            }
            .onCompletion {

            }
            .catch {
            }
            .collect()


    }


    public fun cancelJob() {
        job.cancel()
    }

}