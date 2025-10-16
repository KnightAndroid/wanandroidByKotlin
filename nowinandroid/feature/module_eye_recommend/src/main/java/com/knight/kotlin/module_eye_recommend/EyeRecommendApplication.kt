package com.knight.kotlin.module_eye_recommend

import android.app.Application
import android.content.Context
import com.core.library_common.app.ApplicationLifecycle
import com.google.auto.service.AutoService


/**
 * @author created by luguian
 * @organize
 * @Date 2025/10/16 15:05
 * @descript:开眼推荐视频 application
 */
@AutoService(ApplicationLifecycle::class)
class EyeRecommendApplication: ApplicationLifecycle {
    override fun onAttachBaseContext(context: Context) {}

    override fun onCreate(application: Application) {}

    override fun onTerminate(application: Application) {}
    override fun initSafeTask(): MutableList<() -> String>  = mutableListOf()

    override fun initDangerousTask() {

    }
}