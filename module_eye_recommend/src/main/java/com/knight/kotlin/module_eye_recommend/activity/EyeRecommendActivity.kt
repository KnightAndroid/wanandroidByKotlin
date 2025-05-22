package com.knight.kotlin.module_eye_recommend.activity

import com.knight.kotlin.library_base.activity.BaseActivity
import com.knight.kotlin.library_base.route.RouteActivity
import com.knight.kotlin.library_base.vm.EmptyViewModel
import com.knight.kotlin.module_eye_recommend.databinding.EyeRecommendActivityBinding
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint


/**
 * @author created by luguian
 * @organize 
 * @Date 2025/5/22 10:56
 * @descript:开眼推荐主页面
 */
@AndroidEntryPoint
@Route(path = RouteActivity.EyeRecommend.EyeRecommendActivity)
class EyeRecommendActivity:BaseActivity<EyeRecommendActivityBinding,EmptyViewModel>() {
    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun initObserver() {

    }

    override fun initRequestData() {

    }

    override fun reLoadData() {

    }

    override fun EyeRecommendActivityBinding.initView() {

    }
}