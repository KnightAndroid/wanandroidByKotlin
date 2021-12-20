package com.knight.kotlin.library_base

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.util.Log
import com.knight.kotlin.library_base.app.LoadModuleProxy
import com.knight.kotlin.library_base.util.ActivityManagerUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlin.system.measureTimeMillis

/**
 * Author:Knight
 * Time:2021/12/15 16:23
 * Description:BaseApp
 */
open class BaseApp : Application() {

    /**
     * 协程
     */
    private val mCoroutineScope by lazy(mode = LazyThreadSafetyMode.NONE) { MainScope()}

    private val mLoadModuleProxy by lazy(mode = LazyThreadSafetyMode.NONE) {LoadModuleProxy()}



    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context:Context

        @SuppressLint("StaticFieldLeak")
        lateinit var application:BaseApp
    }


    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        context = base
        application = this
        mLoadModuleProxy.onAttachBaseContext(base)
    }


    override fun onCreate() {
        super.onCreate()

        //全局监听Activity 生命周期
        registerActivityLifecycleCallbacks(ActivityManagerUtils.getInstance())
        mLoadModuleProxy.onCreate(this)

        //策略初始化第三方依赖
        initDepends()
    }


    /**
     *
     * 初始化第三方依赖
     */
    private fun initDepends() {
        //开启一个Default Coroutine 进行初始化不会立即使用的第三方
        mCoroutineScope.launch { Dispatchers.Default }
        //前台初始化
        val allTimeMillis = measureTimeMillis {
            val depends = mLoadModuleProxy.initFrontTask()
            var dependInfo:String
            depends.forEach {
                val dependTimeMillis = measureTimeMillis { dependInfo = it() }
            }
        }
        //这里可以统计初始化所需要时间
    }


    override fun onTerminate() {
        super.onTerminate()
        mLoadModuleProxy.onTerminate(this)
        mCoroutineScope.cancel()
    }

}