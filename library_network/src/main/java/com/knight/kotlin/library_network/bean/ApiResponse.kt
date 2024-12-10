package com.knight.kotlin.library_network.bean

import com.google.gson.JsonObject


/**
 * @author created by luguian
 * @organize
 * @Date 2024/12/9 18:16
 * @descript:
 */
class ApiResponse<T> (
    val code: Long,
    val message: JsonObject,
    val result: T? = null,
)