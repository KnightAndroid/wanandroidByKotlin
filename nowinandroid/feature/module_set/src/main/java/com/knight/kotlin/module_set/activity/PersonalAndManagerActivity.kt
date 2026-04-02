package com.knight.kotlin.module_set.activity

import android.view.View
import com.core.library_base.contact.EmptyContract
import com.core.library_base.ktx.setOnClick
import com.core.library_base.route.RouteActivity
import com.core.library_base.vm.EmptyMviViewModel
import com.knight.kotlin.library_base.activity.BaseMviActivity
import com.knight.kotlin.library_common.ktx.getUser
import com.knight.kotlin.library_util.startPage
import com.knight.kotlin.module_set.R
import com.knight.kotlin.module_set.databinding.SetPersonalManagerActivityBinding
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint

/**
 * Author:Knight
 * Time:2023/5/6 15:52
 * Description:PersonalAndManagerActivity
 */
@AndroidEntryPoint
@Route(path = RouteActivity.Set.PersonalDeviceMessage)
class PersonalAndManagerActivity :
    BaseMviActivity<
            SetPersonalManagerActivityBinding,
            EmptyMviViewModel,
            EmptyContract.Event,
            EmptyContract.State,
            EmptyContract.Effect>() {

    override fun SetPersonalManagerActivityBinding.initView() {
        title = getString(R.string.set_personal_message_manager)

        includeSetMessageManagerToobar.baseTvTitle.text =
            getString(R.string.set_personal_message_manager)

        includeSetMessageManagerToobar.baseIvBack.setOnClick {
            finish()
        }

        initData()
        initListener()
    }

    override fun initObserver() {}

    override fun initRequestData() {}

    override fun reLoadData() {}

    override fun renderState(state: EmptyContract.State) {}

    override fun handleEffect(effect: EmptyContract.Effect) {}

    override fun setThemeColor(isDarkMode: Boolean) {}

    // =========================
    // 初始化
    // =========================

    private fun initData() {
        val hasUser = getUser() != null
        updateUserVisible(hasUser)
    }

    private fun initListener() = with(mBinding) {
        setRlPersonalMessage.setOnClick {
            startPage(RouteActivity.Set.PersonalMessage)
        }

        setRlDeviceMessage.setOnClick {
            startPage(RouteActivity.Set.DeviceMessage)
        }
    }

    // =========================
    // UI控制
    // =========================

    private fun updateUserVisible(show: Boolean) {
        mBinding.setRlPersonalMessage.visibility =
            if (show) View.VISIBLE else View.GONE
    }
}