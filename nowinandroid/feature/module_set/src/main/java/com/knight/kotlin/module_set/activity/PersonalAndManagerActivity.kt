package com.knight.kotlin.module_set.activity

import android.view.View
import com.core.library_base.route.RouteActivity
import com.core.library_base.vm.EmptyViewModel
import com.flyjingfish.android_aop_core.annotations.SingleClick
import com.knight.kotlin.library_base.activity.BaseActivity
import com.knight.kotlin.library_base.ktx.getUser
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
class PersonalAndManagerActivity : BaseActivity<SetPersonalManagerActivityBinding, EmptyViewModel>() {

    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun SetPersonalManagerActivityBinding.initView() {
        includeSetMessageManagerToobar.baseTvTitle.setText(getString(R.string.set_personal_message_manager))
        getUser()?.let {
            setRlPersonalMessage.visibility = View.VISIBLE
        } ?: run {
            setRlPersonalMessage.visibility = View.GONE
        }
        setOnClickListener(setRlPersonalMessage,setRlDeviceMessage)
        includeSetMessageManagerToobar.baseIvBack.setOnClickListener { finish() }
    }

    override fun initObserver() {

    }

    override fun initRequestData() {

    }

    override fun reLoadData() {

    }

    @SingleClick
    override fun onClick(v: View) {
        when (v) {
            mBinding.setRlPersonalMessage -> {
                startPage(RouteActivity.Set.PersonalMessage)
            }

            mBinding.setRlDeviceMessage -> {
                startPage(RouteActivity.Set.DeviceMessage)
            }
        }
    }
}