package com.knight.kotlin.library_base.ktx

import com.knight.kotlin.library_base.util.GsonUtils
import java.lang.reflect.Type

fun toJson(any: Any): String = GsonUtils.toJson(any)
fun <T> fromJson(json: String, typeOfT: Type): T? = GsonUtils.get<T>(json,typeOfT)
inline fun <reified T> fromJson(json: String): T = GsonUtils.get(json,T::class.java)!!
