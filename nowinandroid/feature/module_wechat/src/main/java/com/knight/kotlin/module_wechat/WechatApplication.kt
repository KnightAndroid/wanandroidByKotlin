package com.knight.kotlin.module_wechat

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.core.library_base.app.ApplicationLifecycle
import com.google.auto.service.AutoService


/**
 *
 * @ProjectName:    wanandroid
 * @Package:        com.knight.kotlin.module_wechat
 * @ClassName:      WechatApplication
 * @Description:    java类作用描述
 * @Author:         knight
 * @CreateDate:     2022/3/18 3:00 下午
 * @UpdateUser:     更新者
 * @UpdateDate:     2022/3/18 3:00 下午
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
@AutoService(ApplicationLifecycle::class)
class WechatApplication: ApplicationLifecycle {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var mWechatApplication: WechatApplication
    }
    override fun onAttachBaseContext(context: Context) {
        mWechatApplication = this
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