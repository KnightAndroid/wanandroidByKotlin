package com.knight.kotlin.module_main

import android.app.Application
import android.content.Context
import com.google.auto.service.AutoService
import com.knight.kotlin.library_base.app.ApplicationLifecycle

/**
 * Author:Knight
 * Time:2021/12/23 18:40
 * Description:MainApplication
 */
@AutoService(ApplicationLifecycle::class)
class MainApplication : ApplicationLifecycle{
    /**
     * 同[Application.attachBaseContext]
     * @param context Context
     */
    override fun onAttachBaseContext(context: Context) {}

    /**
     * 同[Application.onCreate]
     * @param application Application
     */
    override fun onCreate(application: Application) {}

    /**
     * 同[Application.onTerminate]
     * @param application Application
     */
    override fun onTerminate(application: Application) {}

    /**
     * 主线程前台初始化
     * @return MutableList<() -> String> 初始化方法集合
     */
    override fun initFrontTask(): MutableList<() -> String> = mutableListOf()
    /**
     * 不需要立即初始化的放在这里进行后台初始化
     */
    override fun initByBackTask() {
    }


}