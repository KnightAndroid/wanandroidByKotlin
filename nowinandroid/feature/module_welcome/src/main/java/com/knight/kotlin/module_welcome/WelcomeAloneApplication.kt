package com.knight.kotlin.module_welcome

import com.core.library_base.BaseApp


/**
 * @author created by luguian
 * @organize
 * @Date 2025/2/25 11:30
 * @descript:单独app应用
 */
//单独运行使用@HiltAndroidApp
class WelcomeAloneApplication: BaseApp() {

    override fun onCreate() {
        super.onCreate()
       // GoRouter.autoLoadRouteModule(BaseApp.application)
    }
}