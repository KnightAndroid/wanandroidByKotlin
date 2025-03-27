package com.knight.kotlin.module_home

import com.knight.kotlin.library_base.BaseApp
import dagger.hilt.android.HiltAndroidApp


/**
 * @author created by luguian
 * @organize
 * @Date 2025/2/25 11:30
 * @descript:单独app应用
 */
@HiltAndroidApp  //单独运行使用
class HomeAloneApplication: BaseApp() {

    override fun onCreate() {
        super.onCreate()
    }
}