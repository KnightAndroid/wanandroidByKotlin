package com.knight.kotlin.library_base.repository

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import java.io.IOException
import java.util.concurrent.CancellationException


/**
 * Author:Knight
 * Time:2021/12/15 16:09
 * Description:BaseRepository
 */
open class BaseRepository {

    /**
     *
     * 发起请求封装
     * 该方法将flow的执行切换至IO线程
     *
     * @param requestBlock 请求的整体逻辑
     * @return Flow<T>
     */
    protected fun<T> request(requestBlock:suspend FlowCollector<T>.() -> Unit): Flow<T> {
       return flow(block = requestBlock).flowOn(Dispatchers.IO).onStart {
       }
           .onEach {

           }
           .onCompletion {
               //结束
           }
           .catch {
               Log.d("sdsd",it.cause?.message + it.message)
           }
    }


    /**
     *
     * 发起请求封装
     * 该方法将flow的执行切换至IO线程
     *
     * @param requestBlock 请求的整体逻辑
     * @return Flow<T>
     */
    protected fun <T> request(
        requestBlock: suspend FlowCollector<T>.() -> Unit,
        failureCallBack: ((String?) -> Unit)? = null
    ): Flow<T> {
        return flow(requestBlock)
            .flowOn(Dispatchers.IO)
            .onStart {
                // 请求开始，可以放一些准备逻辑
            }
            .onEach {
                // 请求中间过程，可以做数据处理
            }
            .onCompletion {
                // 请求完成，可以做收尾操作
            }
            .catch { e ->
                when {
                    // 忽略OkHttp取消请求异常
                    e is IOException && e.message?.contains(
                        "cancel",
                        ignoreCase = true
                    ) == true -> {
                        // 可选：打印日志或静默处理

                    }

                    // 忽略协程取消异常
                    e is CancellationException -> {

                    }

                    else -> {
                        // 其他异常，调用回调并传递异常信息
                        failureCallBack?.let { it1 ->
                            it1(e.cause?.message + e.message)
                        }
                    }
                }
            }


    }


}