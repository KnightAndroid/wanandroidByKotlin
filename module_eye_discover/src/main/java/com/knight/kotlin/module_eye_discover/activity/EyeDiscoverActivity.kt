package com.knight.kotlin.module_eye_discover.activity

import com.knight.kotlin.library_base.activity.BaseActivity
import com.knight.kotlin.library_base.route.RouteActivity
import com.knight.kotlin.library_base.vm.EmptyViewModel
import com.knight.kotlin.module_eye_discover.databinding.EyeDiscoverActivityBinding
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint


/**
 * @author created by luguian
 * @organize
 * @Date 2024/8/5 17:53
 * @descript:开眼发现
 */
@AndroidEntryPoint
@Route(path = RouteActivity.EyeDiscover.EyeDiscoverActivity)
class EyeDiscoverActivity : BaseActivity<EyeDiscoverActivityBinding, EmptyViewModel>(){
    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun initObserver() {

    }

    override fun initRequestData() {

    }

    override fun reLoadData() {

    }

    override fun EyeDiscoverActivityBinding.initView() {

    }
}