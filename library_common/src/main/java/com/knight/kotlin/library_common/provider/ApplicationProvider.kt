package com.knight.kotlin.library_common.provider

import android.app.Application

/**
 * Author:Knight
 * Time:2021/12/16 14:18
 * Description:ApplicationProvider
 */
class ApplicationProvider constructor(app: Application?) {
    private var app: Application? = null


    init {
        this.app = app
    }

    companion object {
        private var instance: ApplicationProvider? = null
        fun init(app: Application?) {
            if (instance == null) {
                instance = ApplicationProvider(app)
            }
        }

        /**
         *
         * 获取本App的application
         * @return
         */
        fun getInstance(): ApplicationProvider? {
            if (instance == null) {
                synchronized(ApplicationProvider::class.java) {
                    if (null == instance) {
                        instance = ApplicationProvider(AppBridge.getApplicationByReflect())
                    }
                }
            }
            return instance
        }
    }




    fun getApplication(): Application? {
        return app
    }

}