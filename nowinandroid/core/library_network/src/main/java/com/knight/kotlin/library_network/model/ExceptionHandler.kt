package com.knight.kotlin.library_network.model

import com.knight.kotlin.library_network.exception.ResponseEmptyException
import com.knight.kotlin.library_network.exception.ResponseException
import com.knight.kotlin.library_network.enum.ResponseExceptionEnum as ExceptionType
/**
 * Author:Knight
 * Time:2021/12/15 14:27
 * Description:ExceptionHandler
 */

@Throws(ResponseException::class)
suspend fun responseCodeExceptionHandler(code:Int,msg:String) {
    //进行异常处理
    when (code) {
        ExceptionType.COOKIE_ILLEGAL.getCode() -> {
            throw ResponseEmptyException()
        }
        ExceptionType.SUCCESS.getCode() -> {}
        else -> {
            throw ResponseException(ExceptionType.ERROR,msg)
        }

    }

}