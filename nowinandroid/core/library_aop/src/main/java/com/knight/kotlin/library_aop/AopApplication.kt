package com.knight.kotlin.library_aop

import android.app.Application
import android.content.Context
import android.widget.Toast
import com.core.library_base.app.ApplicationLifecycle
import com.core.library_base.util.ProcessUtil
import com.google.auto.service.AutoService
import com.knight.kotlin.library_aop.loginintercept.ILoginFilter
import com.knight.kotlin.library_aop.loginintercept.LoginManager
import com.knight.kotlin.library_base.BaseApp
import com.core.library_common.entity.UserInfoEntity
import com.knight.kotlin.library_base.ktx.getUser


/**
 * Author:Knight
 * Time:2022/1/17 14:35
 * Description:AopApplication
 */
@AutoService(ApplicationLifecycle::class)
class AopApplication: ApplicationLifecycle {
    override fun onAttachBaseContext(context: Context) {

    }

    override fun onCreate(application: Application) {

    }

    override fun onTerminate(application: Application) {

    }

    override fun initSafeTask(): MutableList<() -> String> {
        val list = mutableListOf<() -> String>()
        //在主进程初始化
        if (ProcessUtil.isMainProcess(BaseApp.context)) {
            initLoginFilter(BaseApp.application)
        }
        return list
    }

    override fun initDangerousTask() {

    }


    /**
     *
     *
     * @param application
     */
    private fun initLoginFilter(application: Application) {
        val iLoginFilter: ILoginFilter = object : ILoginFilter {
            override fun login(applicationContext: Context?, loginDefine: Boolean) {
                //没跳过登录
                // TODO: 后面直接跳转到登录页面
                if (!loginDefine) {
                    Toast.makeText(application,"还没有登录,请登录后在操作",Toast.LENGTH_LONG).show()
                }
            }

            override fun isLogin(applicationContext: Context?): Boolean {
                val userInfoEntity: UserInfoEntity? = getUser()
                return userInfoEntity != null
            }
        }
        LoginManager.getInstance()?.init(application, iLoginFilter)
    }


}