package com.knight.kotlin.module_set.activity

import android.view.View
import androidx.activity.viewModels
import com.alibaba.android.arouter.facade.annotation.Route
import com.knight.kotlin.library_aop.clickintercept.SingleClick
import com.knight.kotlin.library_base.activity.BaseActivity
import com.knight.kotlin.library_base.config.Appconfig
import com.knight.kotlin.library_base.event.MessageEvent
import com.knight.kotlin.library_base.ktx.observeLiveData
import com.knight.kotlin.library_base.route.RouteActivity
import com.knight.kotlin.library_base.util.CacheUtils
import com.knight.kotlin.library_base.util.EventBusUtils
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
    override val mViewModel: SetVm by viewModels()
    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun SetActivityBinding.initView() {
        setOnClickListener(setRlLogout)
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