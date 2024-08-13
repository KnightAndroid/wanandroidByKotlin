package com.knight.kotlin.module_eye_discover.activity

import com.knight.kotlin.library_base.activity.BaseActivity
import com.knight.kotlin.library_base.route.RouteActivity
import com.knight.kotlin.module_eye_discover.databinding.EyeDiscoverCategoryDetailActivityBinding
import com.knight.kotlin.module_eye_discover.vm.EyeDiscoverVm
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint


/**
 * @author created by luguian
 * @organize
 * @Date 2024/8/13 17:28
 * @descript:分类详细界面
 */
@AndroidEntryPoint
@Route(path = RouteActivity.EyeDiscover.EyeCategoryDetailActivity)
class EyeCategoryDetailActivity : BaseActivity<EyeDiscoverCategoryDetailActivityBinding, EyeDiscoverVm>(){
    override fun setThemeColor(isDarkMode: Boolean) {
        TODO("Not yet implemented")
    }

    override fun initObserver() {
        TODO("Not yet implemented")
    }

    override fun initRequestData() {
        TODO("Not yet implemented")
    }

    override fun reLoadData() {
        TODO("Not yet implemented")
    }

    override fun EyeDiscoverCategoryDetailActivityBinding.initView() {
        TODO("Not yet implemented")
    }
}