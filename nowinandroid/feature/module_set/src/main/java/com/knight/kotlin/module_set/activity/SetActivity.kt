package com.knight.kotlin.module_set.activity

import android.graphics.drawable.GradientDrawable
import android.view.View
import com.core.library_base.event.MessageEvent
import com.core.library_base.ktx.appStr
import com.core.library_base.ktx.dimissLoadingDialog
import com.core.library_base.ktx.showLoadingDialog
import com.core.library_base.route.RouteActivity
import com.core.library_base.util.EventBusUtils
import com.core.library_common.util.ColorUtils
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
import com.knight.kotlin.library_widget.RippleAnimation
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
        SetContract.Effect>() {

    private var ignoreCheckChange = false

    //===================== 初始化 =====================//

    override fun SetActivityBinding.initView() {

        setOnClickListener(
            setRlLogout, setRlDarkmode, setRlTheme,
            setRlLanguage, setRlNightTime, setRlChangeTextSize,
            setRlGesturePassword, setRlClearCache, setRlRepository,
            setRlOfficialwebsite, setRlAbout, setRlPersonMessageManager
        )

        includeSetToobar.baseIvBack.setOnClickListener { finish() }
        includeSetToobar.baseTvTitle.text = getString(R.string.set_app_name)

        initListener()
    }

    override fun initObserver() {}

    override fun initRequestData() {}

    override fun initEvent() {
        mViewModel.setEvent(SetContract.Event.Init)
    }

    //===================== 监听 =====================//

    private fun initListener() {

        mBinding.setCbStatusTheme.setOnCheckedChangeListener { _, isChecked ->
            if (ignoreCheckChange) return@setOnCheckedChangeListener

            mViewModel.setEvent(SetContract.Event.ChangeStatusTheme(isChecked))

            EventBusUtils.postEvent(
                MessageEvent(MessageEvent.MessageType.ChangeStatusTheme).put(isChecked)
            )
        }

        mBinding.setCbEyecare.setOnCheckedChangeListener { _, isChecked ->
            if (ignoreCheckChange) return@setOnCheckedChangeListener

            mViewModel.setEvent(SetContract.Event.ChangeEyeCare(isChecked))

            RippleAnimation.create(mBinding.setCbEyecare).setDuration(250).start()

            EventBusUtils.postEvent(
                MessageEvent(MessageEvent.MessageType.EyeMode).put(isChecked)
            )

            openOrCloseEye(isChecked)
            showDarkMode(!isChecked)
        }
    }

    //===================== MVI - 渲染 =====================//

    override fun renderState(state: SetContract.State) {

        ignoreCheckChange = true

        // ⭐关键：统一数据源
        themeColor = state.themeColor

        // 登录态
        mBinding.setRlLogout.visibility =
            if (state.isLogin) View.VISIBLE else View.GONE

        mBinding.setRlGesturePassword.visibility =
            if (state.isLogin) View.VISIBLE else View.GONE

        // checkbox
        mBinding.setCbStatusTheme.isChecked = state.isStatusWithTheme
        mBinding.setCbEyecare.isChecked = state.isEyeCare

        // 状态栏 tint
        mBinding.setCbStatusTheme.buttonTintList =
            ColorUtils.createColorStateList(
                state.themeColor,
                ColorUtils.convertToColorInt("a6a6a6")
            )

        // 主题色圆点
        val drawable = GradientDrawable().apply {
            shape = GradientDrawable.OVAL
            setColor(state.themeColor)
        }
        mBinding.setShowThemecolor.background = drawable

        // 文本颜色
        updateTextColor(state.themeColor)

        // ❗优化点：不要再自己算
        mBinding.setTvCachememory.text = state.cacheSize

        ignoreCheckChange = false
    }

    //===================== Effect =====================//

    override fun handleEffect(effect: SetContract.Effect) {
        when (effect) {

            is SetContract.Effect.ShowLoading -> {
                showLoadingDialog(appStr(R.string.set_logout))
            }

            is SetContract.Effect.HideLoading -> {
                dimissLoadingDialog()
            }

            is SetContract.Effect.LogoutSuccess -> {
                logoutSuccess()
            }

            is SetContract.Effect.ShowToast -> {
                toast(effect.msg)
            }
        }
    }

    //===================== 主题控制（保留但修正） =====================//

    override fun setThemeColor(isDarkMode: Boolean) {

        val state = mViewModel.viewState.value

        updateTextColor(state.themeColor)

        if (!isDarkMode) {

            showEyeCare(true)

            if (state.isEyeCare) {
                showDarkMode(false)
            } else {
                showDarkMode(true)
            }

        } else {
            mBinding.setRlTheme.visibility = View.GONE
            mBinding.setRlStatustheme.visibility = View.GONE
            showEyeCare(false)
        }
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

    //===================== 点击 =====================//

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

            mBinding.setRlDarkmode -> startPage(RouteActivity.Set.DarkModelActivity)

            // ⭐⭐⭐核心修复：不再直接改UI！！！
            mBinding.setRlTheme -> {
                ColorPickerDialog.Builder(
                    this,
                    CacheUtils.getThemeColor(),
                    ColorStyle.THEMECOLOR,
                    getString(R.string.set_recover_themecolor)
                ).setOnColorPickedListener { color ->

                    // ✅ 只发事件 → renderState 自动更新UI
                    mViewModel.setEvent(
                        SetContract.Event.ChangeThemeColor(color)
                    )

                    EventBusUtils.postEvent(
                        MessageEvent(MessageEvent.MessageType.RecreateMain)
                    )

                }.build().show()
            }

            mBinding.setRlClearCache -> {
                DialogUtils.getConfirmDialog(
                    this,
                    getString(R.string.set_clearcache_tip),
                    { _, _ ->
                        CacheFileUtils.cleadAllCache(this)
                        toast(R.string.set_clearchae_successfully)

                        // ⭐通知VM更新
                        val size = CacheFileUtils.getToalCacheSize(this)
                        mViewModel.setEvent(SetContract.Event.UpdateCacheSize(size))
                    }
                ) { _, _ -> }
            }

            mBinding.setRlLanguage -> startPage(RouteActivity.Set.SetLanguageActivity)
            mBinding.setRlNightTime -> startPage(RouteActivity.Set.SetAutoNightActivity)
            mBinding.setRlChangeTextSize -> startPage(RouteActivity.Set.SetChangeTextSizeActivity)
            mBinding.setRlGesturePassword -> startPage(RouteActivity.Set.SetGestureLockActivity)

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

            mBinding.setRlPersonMessageManager -> {
                startPage(RouteActivity.Set.PersonalDeviceMessage)
            }

            mBinding.setRlAbout -> {
                startPage(RouteActivity.Set.AboutActivity)
            }
        }
    }

    //===================== 原逻辑 =====================//

    private fun logoutSuccess() {
        CacheUtils.loginOut()
        Appconfig.user = null
        ClientConfig.cookieManager.cookieStore.removeAll()

        EventBusUtils.postEvent(MessageEvent(MessageEvent.MessageType.LogoutSuccess))
    }
}