package com.knight.kotlin.module_set.activity

import androidx.activity.viewModels
import com.alibaba.android.arouter.facade.annotation.Route
import com.knight.kotlin.library_base.activity.BaseActivity
import com.knight.kotlin.library_base.route.RouteActivity
import com.knight.kotlin.library_base.vm.EmptyViewModel
import com.knight.kotlin.module_set.databinding.SetPersonalMessageActivityBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Author:Knight
 * Time:2023/5/8 16:14
 * Description:PersonalMessageActivity
 */
@AndroidEntryPoint
@Route(path = RouteActivity.Set.PersonalMessage)
class PersonalMessageActivity : BaseActivity<SetPersonalMessageActivityBinding, EmptyViewModel>() {
    override val mViewModel: EmptyViewModel by viewModels()
    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun SetPersonalMessageActivityBinding.initView() {

    }

    override fun initObserver() {

    }

    override fun initRequestData() {

    }

    override fun reLoadData() {

    }
}