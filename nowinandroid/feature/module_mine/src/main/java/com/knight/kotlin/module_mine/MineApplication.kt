package com.knight.kotlin.module_mine

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.core.library_base.app.ApplicationLifecycle
import com.google.auto.service.AutoService

/**
 *
 * @ProjectName:    wanandroid
 * @Package:        com.knight.kotlin.module_mine
 * @ClassName:      MineApplication
 * @Description:    java类作用描述
 * @Author:         knight
 * @CreateDate:     2022/3/23 3:47 下午
 * @UpdateUser:     更新者
 * @UpdateDate:     2022/3/23 3:47 下午
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
@AutoService(ApplicationLifecycle::class)
class MineApplication : ApplicationLifecycle {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var mMineApplication: MineApplication
    }
    override fun onAttachBaseContext(context: Context) {
        mMineApplication = this
    }

    override fun onCreate(application: Application) {

    }

    override fun onTerminate(application: Application) {

    }

    override fun initSafeTask(): MutableList<() -> String> {
        val list = mutableListOf<() -> String>()
        return list
    }

    override fun initDangerousTask() {

    }

}