package com.knight.kotlin.module_set.activity

import android.graphics.drawable.GradientDrawable
import android.view.View
import com.core.library_base.ktx.appStr
import com.core.library_base.ktx.dimissLoadingDialog
import com.core.library_base.ktx.showLoadingDialog
import com.core.library_base.route.RouteActivity
import com.core.library_base.util.EventBusUtils
import com.flyjingfish.android_aop_core.annotations.SingleClick
import com.knight.kotlin.library_base.activity.BaseMviActivity
import com.knight.kotlin.library_common.config.Appconfig
import com.knight.kotlin.library_common.util.CacheUtils
import com.knight.kotlin.library_network.client.ClientConfig
import com.knight.kotlin.library_util.CacheFileUtils
import com.knight.kotlin.library_util.DialogUtils
import com.knight.kotlin.library_util.startPage
import com.knight.kotlin.library_util.startPageWithParams
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.module_set.R
import com.knight.kotlin.module_set.annoation.ColorStyle
import com.knight.kotlin.module_set.contract.SetContract
import com.knight.kotlin.module_set.databinding.SetActivityBinding
import com.knight.kotlin.module_set.dialog.ColorPickerDialog
import com.knight.kotlin.module_set.vm.SetVm
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@Route(path = RouteActivity.Set.SetActivity)
class SetActivity : BaseMviActivity<
        SetActivityBinding,
        SetVm,
        SetContract.Event,
        SetContract.State,
        SetContract.Effect
        >() {

    override fun SetActivityBinding.initView() {
        setOnClickListener(
            setRlLogout,
            setRlDarkmode,
            setRlTheme,
            setRlLanguage,
            setRlNightTime,
            setRlChangeTextSize,
            setRlGesturePassword,
            setRlClearCache,
            setRlRepository,
            setRlOfficialwebsite,
            setRlAbout,
            setRlPersonMessageManager
        )

        includeSetToobar.baseIvBack.setOnClickListener { finish() }
        includeSetToobar.baseTvTitle.text = getString(R.string.set_app_name)

        setTvCachememory.text = CacheFileUtils.getToalCacheSize(this@SetActivity)

        initDarkMode()
    }

    override fun initObserver() {

    }

    override fun initRequestData() {

    }

    override fun setThemeColor(isDarkMode: Boolean) {
        val color = CacheUtils.getThemeColor()
        updateTextColor(color)
        setThemeTextColor()

        if (!isDarkMode) {
            showEyeCare(true)
            showDarkMode(!CacheUtils.getIsEyeCare())
        } else {
            mBinding.setRlTheme.visibility = View.GONE
            mBinding.setRlStatustheme.visibility = View.GONE
            showEyeCare(false)
        }
    }

    /**
     * ================= MVI =================
     */

    override fun renderState(state: SetContract.State) {
        with(mBinding) {
            if (state.isLoading) showLoadingDialog(msg = appStr(R.string.set_logout))
            else dimissLoadingDialog()

            setRlLogout.visibility = if (state.isLogin) View.VISIBLE else View.GONE
            setRlGesturePassword.visibility = if (state.isLogin) View.VISIBLE else View.GONE

            setTvCachememory.text = state.cacheSize
        }
    }

    override fun handleEffect(effect: SetContract.Effect) {
        when (effect) {

            SetContract.Effect.LogoutSuccess -> logoutSuccess()

            is SetContract.Effect.ShowError -> {
                toast(effect.msg)
            }

            SetContract.Effect.CacheCleared -> {
                toast(R.string.set_clearchae_successfully)
            } else ->{}
        }
    }

    /**
     * ================= 点击 =================
     */

    @SingleClick
    override fun onClick(v: View) {
        when (v) {

            mBinding.setRlLogout -> {
                DialogUtils.getConfirmDialog(
                    this,
                    getString(R.string.set_confirm_logout),
                    { _, _ ->
                        mViewModel.setEvent(SetContract.Event.Logout)
                    }
                ) { _, _ -> }
            }

            mBinding.setRlClearCache -> {
                DialogUtils.getConfirmDialog(
                    this,
                    getString(R.string.set_clearcache_tip),
                    { _, _ ->
                        mViewModel.setEvent(SetContract.Event.ClearCache)
                    }
                ) { _, _ -> }
            }

            mBinding.setRlTheme -> openColorPicker()

            mBinding.setRlDarkmode -> startPage(RouteActivity.Set.DarkModelActivity)
            mBinding.setRlLanguage -> startPage(RouteActivity.Set.SetLanguageActivity)
            mBinding.setRlNightTime -> startPage(RouteActivity.Set.SetAutoNightActivity)
            mBinding.setRlChangeTextSize -> startPage(RouteActivity.Set.SetChangeTextSizeActivity)
            mBinding.setRlGesturePassword -> startPage(RouteActivity.Set.SetGestureLockActivity)
            mBinding.setRlPersonMessageManager -> startPage(RouteActivity.Set.PersonalDeviceMessage)
            mBinding.setRlAbout -> startPage(RouteActivity.Set.AboutActivity)

            mBinding.setRlRepository -> {
                startPageWithParams(
                    RouteActivity.Web.WebPager,
                    "webUrl" to "https://github.com/KnightAndroid/wanandroidByKotlin",
                    "webTitle" to getString(R.string.set_project_repository)
                )
            }

            mBinding.setRlOfficialwebsite -> {
                startPageWithParams(
                    RouteActivity.Web.WebPager,
                    "webUrl" to "https://www.wanandroid.com/",
                    "webTitle" to getString(R.string.set_official_website)
                )
            }
        }
    }

    /**
     * ================= UI =================
     */

    private fun logoutSuccess() {
        CacheUtils.loginOut()
        Appconfig.user = null
        ClientConfig.cookieManager.cookieStore.removeAll()

        EventBusUtils.postEvent(
            com.core.library_base.event.MessageEvent(
                com.core.library_base.event.MessageEvent.MessageType.LogoutSuccess
            )
        )
    }

    private fun initDarkMode() {
        val text = when {
            CacheUtils.getFollowSystem() -> getString(R.string.set_follow_system)
            CacheUtils.getNormalDark() -> getString(R.string.set_dark_open)
            else -> getString(R.string.set_dark_close)
        }
        mBinding.setTvDarkmodeStatus.text = text
    }

    private fun openColorPicker() {
        ColorPickerDialog.Builder(
            this,
            CacheUtils.getThemeColor(),
            ColorStyle.THEMECOLOR,
            getString(R.string.set_recover_themecolor)
        ).setOnColorPickedListener {
            mViewModel.setEvent(SetContract.Event.ChangeTheme(it))
        }.build().show()
    }

    private fun setThemeTextColor() {
        val color = CacheUtils.getThemeColor()
        val drawable = GradientDrawable().apply {
            shape = GradientDrawable.OVAL
            setColor(color)
        }
        mBinding.setShowThemecolor.background = drawable
    }

    private fun updateTextColor(color: Int) {
        mBinding.setTvBasic.setTextColor(color)
        mBinding.setTvCommon.setTextColor(color)
        mBinding.setTvOther.setTextColor(color)
    }

    private fun showDarkMode(show: Boolean) {
        mBinding.setRlDarkmode.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun showEyeCare(show: Boolean) {
        mBinding.setRlEyecare.visibility = if (show) View.VISIBLE else View.GONE
    }
}