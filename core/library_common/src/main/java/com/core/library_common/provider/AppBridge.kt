package com.core.library_common.provider

import android.app.Application

/**
 * Author:Knight
 * Time:2021/12/16 13:49
 * Description:AppBridge
 */
class AppBridge {



    companion object {
        fun getApplicationByReflect(): Application? {
            return AppField.INSTANCE.getApplicationByReflect()
        }
    }

}