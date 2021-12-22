package com.knight.kotlin.module_welcome.activity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import androidx.activity.viewModels
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.knight.kotlin.library_base.activity.BaseActivity
import com.knight.kotlin.library_base.ktx.observeLiveData
import com.knight.kotlin.library_base.route.RouteActivity
import com.knight.kotlin.module_welcome.R
import com.knight.kotlin.module_welcome.databinding.WelcomeActivityBinding
import com.knight.kotlin.module_welcome.entity.AppThemeBean
import com.knight.kotlin.module_welcome.vm.WelcomeVm
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@Route(path = RouteActivity.Weclome.WeclmeActivity)
class WelcomeActivity : BaseActivity<WelcomeActivityBinding, WelcomeVm>() {
    override val mViewModel: WelcomeVm by viewModels()
    override fun WelcomeActivityBinding.initView() {
        setTheme(getActivityTheme())
        logoAnim.addOffsetAnimListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                ARouter.getInstance().build(RouteActivity.Main.MainActivity).navigation()
                finish()
            }
        })
        logoAnim.startAnimation()
    }


    /**
     *
     * 订阅LiveData
     */
    override fun initObserver() {
        observeLiveData(mViewModel.themeData,::setAppThemeData)
    }

    /**
     * 页面请求接口
     *
     */
    override fun initRequestData() {
       mViewModel.getAppTheme()
    }


    private fun setAppThemeData(data:AppThemeBean) {

    }

    override fun getActivityTheme(): Int {
        return R.style.welcomeAppSplash
    }
}