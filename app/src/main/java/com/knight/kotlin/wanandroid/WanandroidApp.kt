package com.knight.kotlin.wanandroid
import com.knight.kotlin.library_base.BaseApp
import dagger.hilt.android.HiltAndroidApp
import org.greenrobot.eventbus.EventBus

/**
 *Author:Knight
 *Time:2021/12/14 16:52
 *Description:WanandroidApp
 */
@HiltAndroidApp
class WanandroidApp :BaseApp() {
    override fun onCreate() {
        // 开启EventBusAPT 优化反射效率 当组件作为App运行时需要将添加的Index注释掉 因为找不到对应的类了
        EventBus.builder()
            .installDefaultEventBus()
        super.onCreate()
    }
}