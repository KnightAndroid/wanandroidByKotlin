package com.knight.kotlin.module_set.activity

import android.graphics.drawable.GradientDrawable
import android.view.View
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
import com.knight.kotlin.library_util.dismissHud
import com.knight.kotlin.library_util.showHud
import com.knight.kotlin.module_set.R
import com.knight.kotlin.module_set.databinding.SetActivityBinding
import com.knight.kotlin.module_set.vm.SetVm
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@Route(path = RouteActivity.Set.SetActivity)
class SetActivity : BaseActivity<SetActivityBinding, SetVm>(){

    //状态栏是否着色
    private var statusIsWithTheme = false
    override val mViewModel: SetVm by viewModels()


    override fun SetActivityBinding.initView() {
        setOnClickListener(setRlLogout)
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

    }
    override fun setThemeColor(isDarkMode: Boolean) {
        setThemeTextColor()
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
        dismissHud()
        mBinding.setRlLogout.visibility = View.GONE
        mBinding.setRlGesturePassword.visibility = View.GONE
        CacheUtils.loginOut()
        Appconfig.user = null
        EventBusUtils.postEvent(MessageEvent(MessageEvent.MessageType.LogoutSuccess))

    }

    @SingleClick
    override fun onClick(v: View) {
        when (v) {
            mBinding.setRlLogout ->{
                DialogUtils.getConfirmDialog(
                    this@SetActivity,
                    resources.getString(R.string.set_confirm_logout),
                    { _, _ ->
                        showHud(this@SetActivity,getString(R.string.set_logout))
                        mViewModel.logout()
                    }) { dialog, which -> }
            }
        }
    }
}