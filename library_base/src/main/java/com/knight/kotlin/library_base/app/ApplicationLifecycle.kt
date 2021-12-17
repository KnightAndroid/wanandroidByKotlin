package com.knight.kotlin.library_base.app

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
     * 主线程前台初始化
     * @return MutableList<() -> String> 初始化方法集合
     */
    fun initFrontTask():MutableList<() -> String>

    /**
     *
     * 不需要立即初始化 后台初始化
     */
    fun initByBackTask()
}