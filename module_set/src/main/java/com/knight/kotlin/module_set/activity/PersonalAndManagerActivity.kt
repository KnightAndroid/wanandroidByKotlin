package com.knight.kotlin.module_set.activity

import androidx.activity.viewModels
import com.alibaba.android.arouter.facade.annotation.Route
import com.knight.kotlin.library_base.activity.BaseActivity
import com.knight.kotlin.library_base.route.RouteActivity
import com.knight.kotlin.library_base.vm.EmptyViewModel
import com.knight.kotlin.module_set.R
import com.knight.kotlin.module_set.databinding.SetPersonalManagerActivityBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Author:Knight
 * Time:2023/5/6 15:52
 * Description:PersonalAndManagerActivity
 */
@AndroidEntryPoint
@Route(path = RouteActivity.Set.PersonalDeviceMessage)
class PersonalAndManagerActivity : BaseActivity<SetPersonalManagerActivityBinding,EmptyViewModel>() {
    override val mViewModel: EmptyViewModel by viewModels()
    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun SetPersonalManagerActivityBinding.initView() {
        includeSetMessageManagerToobar.baseTvTitle.setText(getString(R.string.set_personal_message_manager))
        includeSetMessageManagerToobar.baseIvBack.setOnClickListener { finish() }
    }

    override fun initObserver() {

    }

    override fun initRequestData() {

    }

    override fun reLoadData() {

    }
}