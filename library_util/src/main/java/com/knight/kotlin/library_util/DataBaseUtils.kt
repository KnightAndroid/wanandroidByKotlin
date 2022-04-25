package com.knight.kotlin.library_util

import com.knight.kotlin.library_database.entity.SearchHistroyKeywordEntity
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


    public fun cancelJob() {
        job.cancel()
    }

}