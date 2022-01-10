package com.knight.kotlin.library_base.app

import android.app.Application
import android.content.Context
import java.util.ServiceLoader

/**
 * Author:Knight
 * Time:2021/12/15 17:53
 * Description:加载组件代理类 组件初始化工作由该代理类代理实现
 */
class LoadModuleProxy : ApplicationLifecycle{

    /**
     *
     * 服务加载类
     */
    private var mLoader:ServiceLoader<ApplicationLifecycle> = ServiceLoader.load(ApplicationLifecycle::class.java)

    /**
     *
     * 同[Application.attachBaseContext]
     * @param context Context
     *
     */
    override fun onAttachBaseContext(context: Context) {
        mLoader.forEach {
            it.onAttachBaseContext(context)
        }
    }

    /**
     * 同[Application.onCreate]
     * @param application Application
     */
    override fun onCreate(application: Application) {
        mLoader.forEach { it.onCreate(application) }
    }

    /**
     * 同[Application.onTerminate]
     * @param application Application
     *
     */
    override fun onTerminate(application: Application) {
        mLoader.forEach { it.onTerminate(application) }
    }


    /**
     * 主线程前台初始化
     * @return MutableList<() -> String> 初始化方法集合
     *
     */
//    override fun initFrontTask(): MutableList<() -> String> {
//        val list:MutableList<() -> String> = mutableListOf()
//        mLoader.forEach { list.addAll(it.initFrontTask()) }
//        return list
//    }
//
//    /**
//     * 不需要立即初始化的放在这里进行后台初始化
//     */
//    override fun initByBackTask() {
//        mLoader.forEach { it.initByBackTask() }
//
//    }

    override fun initSafeTask():MutableList<() -> String> {
        val list:MutableList<() -> String> = mutableListOf()
        mLoader.forEach { list.addAll(it.initSafeTask()) }
        return list
    }

    override fun initDangerousTask() {
        mLoader.forEach { it.initDangerousTask() }
    }

}