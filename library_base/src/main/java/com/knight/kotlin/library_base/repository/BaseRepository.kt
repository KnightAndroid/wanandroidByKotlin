package com.knight.kotlin.library_base.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart


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
    protected fun<T> request(requestBlock:suspend FlowCollector<T>.() -> Unit,failureCallBack:((String?) ->Unit) ?= null): Flow<T> {
        return flow(block = requestBlock).flowOn(Dispatchers.IO).onStart {
            //开始
        }
            .onEach {

            }
            .onCompletion {
                //结束

            }
            .catch {
                failureCallBack?.let { it1 -> it1(it.cause?.message + it.message) }
            }
    }


}