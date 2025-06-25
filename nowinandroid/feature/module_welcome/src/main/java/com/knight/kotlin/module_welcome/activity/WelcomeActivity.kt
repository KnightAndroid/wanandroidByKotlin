package com.knight.kotlin.module_welcome.activity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Build
import androidx.annotation.RequiresApi
import com.baidu.location.BDLocation
import com.knight.kotlin.library_base.activity.BaseActivity
import com.knight.kotlin.library_base.config.Appconfig
import com.knight.kotlin.library_base.route.RouteActivity
import com.knight.kotlin.library_base.util.CacheUtils
import com.knight.kotlin.library_base.util.ColorUtils
import com.knight.kotlin.library_common.util.BaiduSoDownloaderUtils
import com.knight.kotlin.library_permiss.XXPermissions
import com.knight.kotlin.library_permiss.permissions.Permission
import com.knight.kotlin.library_util.baidu.LocationUtils
import com.knight.kotlin.library_util.baidu.OnceLocationListener
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

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun WelcomeActivityBinding.initView() {
        BaiduSoDownloaderUtils.load(this@WelcomeActivity,object : BaiduSoDownloaderUtils.OnSoLoadCallback {
            override fun onSuccess() {
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
                val permission:List<String> = listOf(Permission.ACCESS_FINE_LOCATION, Permission.ACCESS_COARSE_LOCATION, Permission.ACCESS_BACKGROUND_LOCATION)
                if (XXPermissions.isGrantedPermissions(this@WelcomeActivity,permission)) {
                    LocationUtils.getLocation(object : OnceLocationListener {
                        @RequiresApi(Build.VERSION_CODES.O)
                        override fun onReceiveLocation(location: BDLocation?) {

                        }
                    })
                }
            }

            override fun onFailure(e: Throwable) {

            }
        })
        logoAnim.setTextColor(CacheUtils.getThemeColor())
//        logoAnim.addOffsetAnimListener(object : AnimatorListenerAdapter() {
//            override fun onAnimationEnd(animation: Animator) {
//                super.onAnimationEnd(animation)
//                if (CacheUtils.getAgreeStatus()) {
//                    GoRouter.getInstance().build(RouteActivity.Main.MainActivity).go()
//                    finish()
//                } else {
//                    WelcomePrivacyAgreeFragment().show(supportFragmentManager, "dialog_privacy")
//                }
//            }
//        })
//        logoAnim.startAnimation()
//        val permission:List<String> = listOf(Permission.ACCESS_FINE_LOCATION, Permission.ACCESS_COARSE_LOCATION, Permission.ACCESS_BACKGROUND_LOCATION)
//        if (XXPermissions.isGrantedPermissions(this@WelcomeActivity,permission)) {
//            LocationUtils.getLocation(object : OnceLocationListener {
//                @RequiresApi(Build.VERSION_CODES.O)
//                override fun onReceiveLocation(location: BDLocation?) {
//
//                }
//            })
//        }

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