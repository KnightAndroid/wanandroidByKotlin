package com.knight.kotlin.module_welcome.activity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Build
import androidx.annotation.RequiresApi
import com.core.library_base.route.RouteActivity
import com.core.library_common.util.ColorUtils
import com.hjq.permissions.XXPermissions
import com.knight.kotlin.library_base.activity.BaseMviActivity
import com.knight.kotlin.library_common.config.Appconfig
import com.knight.kotlin.library_common.entity.AppThemeBean
import com.knight.kotlin.library_common.entity.LocationEntity
import com.knight.kotlin.library_common.util.CacheUtils
import com.knight.kotlin.library_permiss.permission.PermissionLists
import com.knight.kotlin.library_permiss.permission.base.IPermission
import com.knight.kotlin.library_util.baidu.LocationUtils
import com.knight.kotlin.library_util.baidu.OnceLocationListener
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.module_welcome.contract.WelcomeContract
import com.knight.kotlin.module_welcome.databinding.WelcomeActivityBinding
import com.knight.kotlin.module_welcome.fragment.WelcomePrivacyAgreeFragment
import com.knight.kotlin.module_welcome.util.WeekImagePreloadUtils
import com.knight.kotlin.module_welcome.vm.WelcomeVm
import com.wyjson.router.GoRouter
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@Route(path = RouteActivity.Weclome.WeclmeActivity)
class WelcomeActivity :
    BaseMviActivity<
            WelcomeActivityBinding,
            WelcomeVm,
            WelcomeContract.Event,
            WelcomeContract.State,
            WelcomeContract.Effect>() {

    // ========================
    // 初始化 View
    // ========================
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun WelcomeActivityBinding.initView() {
        startLogoAnim()
    }

    // ========================
    // 订阅 State / Effect
    // ========================
    override fun initObserver() {

    }

    // ========================
    // 页面入口事件
    // ========================
    override fun initRequestData() {
        mViewModel.setEvent(WelcomeContract.Event.LoadAppTheme)
    }

    // ========================
    // 渲染 State
    // ========================
    override fun renderState(state: WelcomeContract.State) {
        //StateFlow / LiveData 一订阅，就会立刻发射当前 State 所以会走两次
        state.theme?.let {
            setAppThemeData(it)
        }
    }

    // ========================
    // 处理 Effect（一次性）
    // ========================
    override fun handleEffect(effect: WelcomeContract.Effect) {
        when (effect) {
            is WelcomeContract.Effect.ShowError -> {
                toast(effect.msg)
            }
            WelcomeContract.Effect.GoMain -> {
                GoRouter.getInstance()
                    .build(RouteActivity.Main.MainActivity)
                    .go()
                finish()
            }

            WelcomeContract.Effect.ShowPrivacyDialog -> {
                WelcomePrivacyAgreeFragment()
                    .show(supportFragmentManager, "dialog_privacy")
            }
        }
    }

    // ========================
    // Logo 动画（只发 Event）
    // ========================
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    private fun startLogoAnim() {

        mBinding.logoAnim.setTextColor(CacheUtils.getThemeColor())

        mBinding.logoAnim.addOffsetAnimListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)

                // 🔥 动画结束 = 发事件
                mViewModel.setEvent(WelcomeContract.Event.LogoAnimFinished)
            }
        })

        mBinding.logoAnim.startAnimation()

        checkLocationPermission()
        WeekImagePreloadUtils.preloadTodayImage(this)
    }

    // ========================
    // 权限 & 定位（View 层逻辑）
    // ========================
    private fun checkLocationPermission() {
        val permissions: List<IPermission> = listOf(
            PermissionLists.getAccessFineLocationPermission(),
            PermissionLists.getAccessCoarseLocationPermission(),
            PermissionLists.getAccessBackgroundLocationPermission()
        )

        if (XXPermissions.isGrantedPermissions(this, permissions)) {
            LocationUtils.getLocation(object : OnceLocationListener {
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onReceiveLocation(location: LocationEntity?) {}
            })
        }
    }

    // ========================
    // 设置主题数据
    // ========================
    private fun setAppThemeData(data: AppThemeBean) {
        Appconfig.appThemeData = data

        if (data.forceTheme) {
            CacheUtils.setThemeColor(
                ColorUtils.convertToColorInt(data.themeColor)
            )
        } else {
            data.themeColor =
                ColorUtils.convertToRGB(CacheUtils.getThemeColor())
        }
    }

    override fun reLoadData() {}

    override fun setThemeColor(isDarkMode: Boolean) {}
}