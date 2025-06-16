package com.knight.kotlin.library_network.log.extension

import com.knight.kotlin.library_network.log.L
import com.knight.kotlin.library_network.log.bean.JSONConfig

/**
 * Author:Knight
 * Time:2021/12/22 10:58
 * Description:L+Extension
 */
inline fun <reified T> T.logTag() = T::class.javaClass.name

inline fun <reified T> Class<T>.logTag() = simpleName


fun String?.e() = L.e(this)

fun String?.w() = L.w(this)

fun String?.i() = L.i(this)

fun String?.d() = L.d(this)

fun Any?.json()  = L.json(this)


inline fun L.e(msg: msgFunction)  = e(msg.invoke())

inline fun L.e(tag: String?, msg: msgFunction) = e(tag, msg.invoke())

inline fun L.w(msg: msgFunction) = w(msg.invoke())

inline fun L.w(tag: String?, msg: msgFunction) = w(tag, msg.invoke())

inline fun L.i(msg: msgFunction) = i(msg.invoke())

inline fun L.i(tag: String?, msg: msgFunction) = i(tag, msg.invoke())

inline fun L.d(msg: msgFunction) = d(msg.invoke())

inline fun L.d(tag: String?, msg: msgFunction) = d(tag, msg.invoke())

inline fun L.json(any:anyFunction)  = json(any.invoke())

inline fun L.json(any:anyFunction,jsonConfig: JSONConfig)  = json(any.invoke(),jsonConfig)