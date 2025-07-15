package com.knight.kotlin.module_welcome.activity

import XXPermissions
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Build
import androidx.annotation.RequiresApi
import com.baidu.location.BDLocation
import com.core.library_base.route.RouteActivity
import com.core.library_common.util.ColorUtils
import com.knight.kotlin.library_base.activity.BaseActivity
import com.knight.kotlin.library_common.config.Appconfig
import com.knight.kotlin.library_common.util.CacheUtils
import com.knight.kotlin.library_permiss.permission.PermissionLists
import com.knight.kotlin.library_permiss.permission.base.IPermission
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

//        if (!BaiduSoDownloaderUtils.isSoDownloaded(this@WelcomeActivity)) {
//            pbLoadSo.visibility = View.VISIBLE
//            BaiduSoDownloaderUtils.load(this@WelcomeActivity, object : BaiduSoDownloaderUtils.OnSoLoadCallback {
//
//                override fun onProgress(totalProgress: Int, text: String) {
//                    ThreadUtils.postMain({
//                        pbLoadSo.setProgressWithText(totalProgress, text)
//                    })
//
//                }
//                override fun onSuccess() {
//                    ThreadUtils.postMain({
//                        if (CacheUtils.getAgreeStatus()) {
//                            GoRouter.getInstance().build(RouteActivity.Main.MainActivity).go()
//                            finish()
//                        } else {
//                            WelcomePrivacyAgreeFragment().show(supportFragmentManager, "dialog_privacy")
//                        }
//                    })
//
//                }
//
//                override fun onFailure(e: Throwable) {
//
//                }
//            })
//            startLogoAnim(false)
//        } else {
//            startLogoAnim(true)
//        }


        startLogoAnim(true)


    }


    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    fun startLogoAnim(executeAfterAnimation: Boolean) {
        mBinding.logoAnim.setTextColor(CacheUtils.getThemeColor())
        mBinding.logoAnim.addOffsetAnimListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                if (executeAfterAnimation) {
                    if (CacheUtils.getAgreeStatus()) {
                        GoRouter.getInstance().build(RouteActivity.Main.MainActivity).go()
                        finish()
                    } else {
                        WelcomePrivacyAgreeFragment().show(supportFragmentManager, "dialog_privacy")
                    }
                }

            }
        })
        mBinding.logoAnim.startAnimation()
        val permission: List<IPermission> = listOf(PermissionLists.getAccessFineLocationPermission(),PermissionLists.getAccessCoarseLocationPermission(),PermissionLists.getAccessBackgroundLocationPermission())
        if (XXPermissions.isGrantedPermissions(this@WelcomeActivity, permission)) {
            LocationUtils.getLocation(object : OnceLocationListener {
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onReceiveLocation(location: BDLocation?) {

                }
            })
        }
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