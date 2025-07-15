package com.knight.kotlin.module_set

import android.app.Application
import android.content.Context
import com.core.library_common.app.ApplicationLifecycle
import com.google.auto.service.AutoService


/**
 *
 * @ProjectName:    wanandroid
 * @Package:        com.knight.kotlin.module_set
 * @ClassName:      SetApplication
 * @Description:    java类作用描述
 * @Author:         knight
 * @CreateDate:     2022/3/28 10:18 下午
 * @UpdateUser:     更新者
 * @UpdateDate:     2022/3/28 10:18 下午
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
@AutoService(ApplicationLifecycle::class)
class SetApplication : ApplicationLifecycle {
    override fun onAttachBaseContext(context: Context) {
    }

    override fun onCreate(application: Application) {
    }

    override fun onTerminate(application: Application) {
    }

    override fun initSafeTask(): MutableList<() -> String> = mutableListOf()

    override fun initDangerousTask() {

    }
}