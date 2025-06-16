package com.knight.kotlin.library_network.enum

/**
 * Author:Knight
 * Time:2021/12/15 14:21
 * Description:请求响应异常的类型
 */
enum class ResponseExceptionEnum :ResponseExceptionEnumCode{

   COOKIE_ILLEGAL {
       override fun getCode(): Int {
          return -1001
       }

       override fun getMessage(): String {
          return "请登录"
       }
   },
   ERROR {
       override fun getCode(): Int {
           return -1
       }

       override fun getMessage(): String {
           return ""
       }
   },
   SUCCESS {
       override fun getCode(): Int {
           return 0
       }

       override fun getMessage(): String {
           return ""
       }
   }
}