package com.knight.kotlin.library_database

import android.app.Application
import android.content.Context
import com.google.auto.service.AutoService
import com.knight.kotlin.library_base.BaseApp
import com.knight.kotlin.library_base.app.ApplicationLifecycle
import com.knight.kotlin.library_base.util.ProcessUtil
import com.knight.kotlin.library_database.managner.DataBaseManager

/**
 * Author:Knight
 * Time:2021/12/27 18:39
 * Description:DataBaseApplication
 */
@AutoService(ApplicationLifecycle::class)
class DataBaseApplication: ApplicationLifecycle {
    override fun onAttachBaseContext(context: Context) {

    }

    override fun onCreate(application: Application) {

    }

    override fun onTerminate(application: Application) {

    }

    override fun initSafeTask(): MutableList<() -> String> {
        val list = mutableListOf<() -> String>()
        if (ProcessUtil.isMainProcess(BaseApp.context)){
            list.add{initDataBase()}
        }
        return list
    }

    override fun initDangerousTask() {

    }



    /**
     * 初始化网络状态监听器
     */
    private fun initDataBase() :String {
        DataBaseManager.getDataBase(BaseApp.application,"wanandroid_database")
        return "DataBase --->> init complete"
    }




}