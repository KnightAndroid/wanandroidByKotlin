package com.knight.kotlin.module_welcome.activity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import androidx.activity.viewModels
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.knight.kotlin.library_base.activity.BaseActivity
import com.knight.kotlin.library_base.config.Appconfig
import com.knight.kotlin.library_base.ktx.observeLiveData
import com.knight.kotlin.library_base.route.RouteActivity
import com.knight.kotlin.library_base.util.CacheUtils
import com.knight.kotlin.library_base.util.ColorUtils
import com.knight.kotlin.module_welcome.databinding.WelcomeActivityBinding
import com.knight.kotlin.module_welcome.entity.AppThemeBean
import com.knight.kotlin.module_welcome.fragment.WelcomePrivacyAgreeFragment
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
                if (CacheUtils.getAgreeStatus()) {
                    ARouter.getInstance().build(RouteActivity.Main.MainActivity).navigation()
                    finish()
                } else {
                    WelcomePrivacyAgreeFragment().show(supportFragmentManager,"dialog_privacy")
                }
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
       Appconfig.appThemeColor = data.themeColor
       if (data.forceTheme) {
           CacheUtils.setThemeColor(ColorUtils.convertToColorInt(Appconfig.appThemeColor))
       }
       Appconfig.gray = data.gray
    }

    override fun reLoadData() {

    }

    override fun setThemeColor(isDarkMode: Boolean) {

    }

}