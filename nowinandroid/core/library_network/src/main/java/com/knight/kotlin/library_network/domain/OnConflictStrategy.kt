package com.knight.kotlin.library_network.domain

import okhttp3.Headers
import okhttp3.Request

/**
 * Author:Knight
 * Time:2024/4/16 10:23
 * Description:OnConflictStrategy 密封类 不能被继承
 */
sealed class OnConflictStrategy {

     abstract fun apply(
         originalHeaders: Headers,
         builder: Request.Builder,
         key:String,
         value:String)

     internal fun Headers?.contains(key:String):Boolean{
         if (this == null)
             return false
         return key.isNotEmpty() && !this.get(key).isNullOrEmpty()
     }

    /**
     * if header is exists in original request,ignore(do nothing).
     */
    object IGNORE : OnConflictStrategy() {
        override fun apply(
            originalHeaders: Headers,
            builder: Request.Builder,
            key: String,
            value: String
        ) {
            if (originalHeaders.contains(key))
                return
            builder.addHeader(key,value)
        }

    }


    /**
     * if the header is exist in origal request,replace it.
     */
    object REPLACE : OnConflictStrategy() {
        override fun apply(
            originalHeaders: Headers,
            builder: Request.Builder,
            key: String,
            value: String
        ) {
            builder.header(key, value)
        }
    }

    /**
     * add header directly,because the request allow add same headers.
     */
    object ADD : OnConflictStrategy() {
        override fun apply(
            originalHeaders: Headers,
            builder: Request.Builder,
            key: String,
            value: String
        ) {
            builder.addHeader(key, value)
        }

    }
    /**
     * if the header is exits in original request ,throw IllegalStateException.
     */
    object ABORT : OnConflictStrategy() {
        override fun apply(
            originalHeaders: Headers,
            builder: Request.Builder,
            key: String,
            value: String
        ) {
            if (originalHeaders.contains(key))
                throw IllegalStateException("the header(key=$key,value=$value) is exits in original request,and the OnConflictStrategy is set Abort.")
            builder.header(key, value)//use set rather than use addHeader method
        }

    }



}