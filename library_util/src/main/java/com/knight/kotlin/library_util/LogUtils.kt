package com.knight.kotlin.library_util

import android.text.TextUtils
import android.util.Log


/**
 * Author:Knight
 * Time:2021/12/20 16:47
 * Description:LogUtils
 */
class LogUtils {


    companion object {
        private val DEFAULT_TAG:String = "wanandroidAPP"
        private val isLog:Boolean = BuildConfig.DEBUG
        fun debugInfo(tag:String,msg:String) {
            if (!isLog || TextUtils.isEmpty(msg)) {
                return;
            }
            Log.d(tag, msg);
        }

        fun d(message: String) {
            var message = message
            if (isLog) {
                val segmentSize = 3 * 1024
                val length = message.length.toLong()
                if (length <= segmentSize) {
                    Log.d(DEFAULT_TAG, message)
                } else {
                    while (message.length > segmentSize) {
                        val logContent = message.substring(0, segmentSize)
                        message = message.replace(logContent, "")
                        Log.d(DEFAULT_TAG, logContent)
                    }
                    Log.d(DEFAULT_TAG, message)
                }
            }
        }
    }



}