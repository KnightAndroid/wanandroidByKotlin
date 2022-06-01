package com.knight.kotlin.module_set.activity

import android.graphics.drawable.GradientDrawable
import android.view.View
import android.widget.CompoundButton
import androidx.activity.viewModels
import com.alibaba.android.arouter.facade.annotation.Route
import com.knight.kotlin.library_aop.clickintercept.SingleClick
import com.knight.kotlin.library_base.activity.BaseActivity
import com.knight.kotlin.library_base.config.Appconfig
import com.knight.kotlin.library_base.event.MessageEvent
import com.knight.kotlin.library_base.ktx.getUser
import com.knight.kotlin.library_base.ktx.observeLiveData
import com.knight.kotlin.library_base.route.RouteActivity
import com.knight.kotlin.library_base.util.CacheUtils
import com.knight.kotlin.library_base.util.ColorUtils
import com.knight.kotlin.library_base.util.EventBusUtils
import com.knight.kotlin.library_util.CacheFileUtils
import com.knight.kotlin.library_util.DialogUtils
import com.knight.kotlin.library_util.startPage
import com.knight.kotlin.library_widget.RippleAnimation
import com.knight.kotlin.module_set.R
import com.knight.kotlin.module_set.annoation.ColorStyle
import com.knight.kotlin.module_set.databinding.SetActivityBinding
import com.knight.kotlin.module_set.dialog.ColorPickerDialog
import com.knight.kotlin.module_set.vm.SetVm
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@Route(path = RouteActivity.Set.SetActivity)
class SetActivity : BaseActivity<SetActivityBinding, SetVm>(){

    //状态栏是否着色
    private var statusIsWithTheme = false
    override val mViewModel: SetVm by viewModels()


    override fun SetActivityBinding.initView() {
        setOnClickListener(setRlLogout,setRlDarkmode,setRlTheme,setRlLanguage,setRlNightTime)
        setCachememory.setText(CacheFileUtils.getToalCacheSize(this@SetActivity))
        includeSetToobar.baseIvBack.setOnClickListener { finish() }
        includeSetToobar.baseTvTitle.setText(getString(R.string.set_app_name))
        getUser()?.let {
            setRlLogout.visibility = View.VISIBLE
            setRlGesturePassword.visibility = View.VISIBLE
        } ?: run {
            setRlLogout.visibility = View.GONE
            setRlGesturePassword.visibility = View.GONE
        }
        statusIsWithTheme = CacheUtils.getStatusBarIsWithTheme()
        setCbStatusTheme.isChecked = statusIsWithTheme
        setCbStatusTheme.buttonTintList = ColorUtils.createColorStateList(CacheUtils.getThemeColor(), ColorUtils.convertToColorInt("a6a6a6"))
        setCbEyecare.isChecked = CacheUtils.getIsEyeCare()
        setCbEyecare.buttonTintList = ColorUtils.createColorStateList(CacheUtils.getThemeColor(), ColorUtils.convertToColorInt("a6a6a6"))
        initDarkMode()
        setCbStatusTheme.setOnCheckedChangeListener(object:CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                statusIsWithTheme = isChecked
                CacheUtils.statusBarIsWithTheme(isChecked)
                buttonView?.buttonTintList = ColorUtils.createColorStateList(CacheUtils.getThemeColor(), ColorUtils.convertToColorInt("a6a6a6"))
                EventBusUtils.postEvent(MessageEvent(MessageEvent.MessageType.ChangeStatusTheme).put(isChecked))
            }
        })
        setCbEyecare.buttonTintList = ColorUtils.createColorStateList(CacheUtils.getThemeColor(), ColorUtils.convertToColorInt("a6a6a6"))
        setCbEyecare.isChecked = CacheUtils.getIsEyeCare()

        setCbEyecare.setOnCheckedChangeListener(object:CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
                CacheUtils.setIsEyeCare(isChecked)
                buttonView.buttonTintList =
                    ColorUtils.createColorStateList(CacheUtils.getThemeColor(), ColorUtils.convertToColorInt("a6a6a6"))
                RippleAnimation.create(mBinding.setCbEyecare).setDuration(250).start()
                EventBusUtils.postEvent(MessageEvent(MessageEvent.MessageType.EyeMode).put(isChecked))
                openOrCloseEye(isChecked)
                showDarkMode(!isChecked)

            }
        })

    }
    override fun setThemeColor(isDarkMode: Boolean) {
        updateTextColor(themeColor)
        setThemeTextColor()
        if (!isDarkMode) {
            //是否护眼 护眼模式就不显示深色模式
            showEyeCare(true)
            if (isEyeCare) {
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

    fun setThemeTextColor() {
        mBinding.setTvBasic.setTextColor(themeColor)
        mBinding.setTvCommon.setTextColor(themeColor)
        mBinding.setTvOther.setTextColor(themeColor)
        //设置主题颜色
        val gradientThemeDrawable = GradientDrawable()
        gradientThemeDrawable.shape = GradientDrawable.OVAL
        gradientThemeDrawable.setColor(CacheUtils.getThemeColor())
        mBinding.setShowThemecolor.background = gradientThemeDrawable
    }

    override fun initObserver() {
         observeLiveData(mViewModel.logoutStatus,::logoutSuccess)
    }

    override fun initRequestData() {

    }

    override fun reLoadData() {

    }

    private fun logoutSuccess(logout:Boolean) {
       // dismissLoading()
        mBinding.setRlLogout.visibility = View.GONE
        mBinding.setRlGesturePassword.visibility = View.GONE
        CacheUtils.loginOut()
        Appconfig.user = null
        EventBusUtils.postEvent(MessageEvent(MessageEvent.MessageType.LogoutSuccess))

    }


    private fun initDarkMode() {
        if (CacheUtils.getFollowSystem()) {
            mBinding.setTvDarkmodeStatus.setText(getString(R.string.set_follow_system))
        } else {
            if (CacheUtils.getNormalDark()) {
                mBinding.setTvDarkmodeStatus.setText(getString(R.string.set_dark_open))
            } else {
                mBinding.setTvDarkmodeStatus.setText(getString(R.string.set_dark_close))
            }
        }
    }

    @SingleClick
    override fun onClick(v: View) {
        when (v) {
            mBinding.setRlLogout -> {
                DialogUtils.getConfirmDialog(
                    this@SetActivity,
                    resources.getString(R.string.set_confirm_logout),
                    { _, _ ->
                    //    showLoading(getString(R.string.set_logout))
                        mViewModel.logout()
                    }) { dialog, which -> }
            }

            mBinding.setRlDarkmode -> {
                startPage(RouteActivity.Set.DarkModelActivity)
            }

            mBinding.setRlTheme -> {
                ColorPickerDialog.Builder(this,CacheUtils.getThemeColor(),
                    ColorStyle.THEMECOLOR,getString(R.string.set_recover_themecolor))
                    .setOnColorPickedListener(object :ColorPickerDialog.OnColorPickedListener{
                        override fun onColorPicked(color: Int) {
                            themeColor = color
                            CacheUtils.setThemeColor(color)
                            val gradientThemeDrawable = GradientDrawable()
                            gradientThemeDrawable.shape = GradientDrawable.OVAL
                            gradientThemeDrawable.setColor(CacheUtils.getThemeColor())
                            mBinding.setShowThemecolor.setBackground(gradientThemeDrawable)
                            updateTextColor(color)
                            EventBusUtils.postEvent(MessageEvent(MessageEvent.MessageType.RecreateMain))
                            //SystemUtils.restartApp(this@SetActivity)
                        }
                    }).build().show()
            }

            mBinding.setRlLanguage -> {
                startPage(RouteActivity.Set.SetLanguageActivity)
            }


            mBinding.setRlNightTime -> {
                startPage(RouteActivity.Set.SetAutoNightActivity)
            }
        }
    }

    /**
     * 更改主题颜色
     *
     * @param color
     */
    private fun updateTextColor(color: Int) {
        mBinding.setTvBasic.setTextColor(color)
        mBinding.setTvCommon.setTextColor(color)
        mBinding.setTvOther.setTextColor(color)
    }


    /**
     *
     * 设置是否深色模式
     */
    private fun showDarkMode(show:Boolean) {
        mBinding.setRlDarkmode.visibility = if (show) View.VISIBLE else View.GONE
    }

    /**
     *
     * 设置是否显示护眼模式状态
     * 深色模式 不显示护眼模式 普通模式显示护眼模式
     *
     */
    private fun showEyeCare(show:Boolean) {
        mBinding.setRlEyecare.visibility = if (show) View.VISIBLE else View.GONE
    }
}