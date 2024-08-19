package com.knight.kotlin.module_eye_discover.activity

import com.knight.kotlin.library_base.activity.BaseActivity
import com.knight.kotlin.library_base.route.RouteActivity
import com.knight.kotlin.module_eye_discover.databinding.EyeDiscoverSpecialTopicActivityBinding
import com.knight.kotlin.module_eye_discover.vm.EyeDiscoverSpecialTopicVm
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint


/**
 * @author created by luguian
 * @organize
 * @Date 2024/8/19 11:04
 * @descript:专题详细页面
 */
@AndroidEntryPoint
@Route(path = RouteActivity.EyeDiscover.EyeDiscoverActivity)
class EyeSpecialTopicDetailActivity : BaseActivity<EyeDiscoverSpecialTopicActivityBinding,EyeDiscoverSpecialTopicVm>() {
    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun initObserver() {

    }

    override fun initRequestData() {

    }

    override fun reLoadData() {

    }

    override fun EyeDiscoverSpecialTopicActivityBinding.initView() {

    }
}