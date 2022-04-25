package com.knight.kotlin.library_network.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * Author:Knight
 * Time:2022/4/24 10:09
 * Description:HttpInterceptor
 */
class HttpInterceptor : Interceptor {

    private val requestMap = LinkedHashMap<String, Long>()//实现拦截重复请求

    override fun intercept(chain: Interceptor.Chain): Response {
        val key = chain.request().url.toString()//请求url
        val time = System.currentTimeMillis()//请求时间
        try {
            //暂时还是有问题 ** 假如两个请求 同时进来 还是会请求
            if(equalRequestInfo(key)) {
                chain.call().cancel()
                throw IOException("cancel")
            }
         //   requestMap[key] = time
            addRequestInfo(key,time)
            val builder = chain.request().newBuilder()
            return chain.proceed(builder.build())
        } catch (e: IOException) {
            throw e
        } finally {
            deleteRequestInfo(key,time)
        }
    }


    private fun addRequestInfo(key: String, time: Long) {
        synchronized(HttpInterceptor::class.java) {
            requestMap[key] = time
        }
    }


    private fun equalRequestInfo(key: String): Boolean {
        synchronized(HttpInterceptor::class.java) {
            if (requestMap.size > 0 && requestMap.containsKey(key)) {
                return true
            }
            return false
        }
    }

    private fun deleteRequestInfo(key: String, time: Long) {
        synchronized(HttpInterceptor::class.java) {
            if (requestMap.containsKey(key) && requestMap.containsValue(time)) {
                requestMap.remove(key)
            }
        }


    }
}