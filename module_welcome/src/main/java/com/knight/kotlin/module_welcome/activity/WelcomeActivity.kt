package com.knight.kotlin.module_welcome.activity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import com.knight.kotlin.library_base.activity.BaseActivity
import com.knight.kotlin.library_base.config.Appconfig
import com.knight.kotlin.library_base.route.RouteActivity
import com.knight.kotlin.library_base.util.CacheUtils
import com.knight.kotlin.library_base.util.ColorUtils
import com.knight.kotlin.library_network.config.BaseUrlConfig
import com.knight.kotlin.module_welcome.databinding.WelcomeActivityBinding
import com.knight.kotlin.module_welcome.entity.AppThemeBean
import com.knight.kotlin.module_welcome.fragment.WelcomePrivacyAgreeFragment
import com.knight.kotlin.module_welcome.vm.WelcomeVm
import com.wyjson.router.GoRouter
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@Route(path = RouteActivity.Weclome.WeclmeActivity)
class WelcomeActivity : BaseActivity<WelcomeActivityBinding, WelcomeVm>() {

    override fun WelcomeActivityBinding.initView() {
        logoAnim.setTextColor(CacheUtils.getThemeColor())
        logoAnim.addOffsetAnimListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                if (CacheUtils.getAgreeStatus()) {
                    GoRouter.getInstance().build(RouteActivity.Main.MainActivity).go()
                    finish()
                } else {
                    WelcomePrivacyAgreeFragment().show(supportFragmentManager, "dialog_privacy")
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

    }

    /**
     * 页面请求接口
     *
     */
    override fun initRequestData() {
        mViewModel.getAppTheme().observerKt {
            setAppThemeData(it)
        }

        mViewModel.getIp(BaseUrlConfig.IP_URL).observerKt {
            Appconfig.IP = it.ip
        }
    }


    private fun setAppThemeData(data: AppThemeBean) {
        if (data.forceTheme) {
            Appconfig.appThemeColor = data.themeColor
            CacheUtils.setThemeColor(ColorUtils.convertToColorInt(Appconfig.appThemeColor))
        } else {
            Appconfig.appThemeColor = ColorUtils.convertToRGB(CacheUtils.getThemeColor())
        }

        Appconfig.gray = data.gray
    }

    override fun reLoadData() {

    }

    override fun setThemeColor(isDarkMode: Boolean) {

    }

}