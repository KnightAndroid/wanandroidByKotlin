package com.knight.kotlin.library_network.exception
import com.knight.kotlin.library_network.enum.ResponseExceptionEnumCode as ExceptionType

/**
 * Author:Knight
 * Time:2021/12/15 14:06
 * Description:请求响应异常，各种Code专门定义的异常
 */


class ResponseException(val type:ExceptionType,val msg:String):Exception(msg)

/**
 * 空异常，表示该异常已经被处理过了，不需要再额外处理
 *
 */
class ResponseEmptyException:Exception()