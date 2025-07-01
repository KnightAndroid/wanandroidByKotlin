package com.core.library_base.app

import android.app.Application
import android.content.Context

/**
 * Author:Knight
 * Time:2021/12/15 16:30
 * Description:ApplicationLifecycle
 */
interface ApplicationLifecycle {

    /**
     * 同[Application.attachBaseContext]
     * @param context Context
     */
    fun onAttachBaseContext(context: Context)


    /**
     * 同[Application.onCreate]
     * @param application Application
     *
     */
    fun onCreate(application:Application)


    /**
     * 同[Application.onTerminate]
     * @param application Application
     */
    fun onTerminate(application:Application)


    /**
     *
     * 初始化安全类Task
     */
    fun initSafeTask():MutableList<()->String>


    /**
     *
     * 初始化危险Task
     */
    fun initDangerousTask()


}