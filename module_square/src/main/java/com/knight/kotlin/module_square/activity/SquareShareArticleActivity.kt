package com.knight.kotlin.module_square.activity

import androidx.activity.viewModels
import com.alibaba.android.arouter.facade.annotation.Route
import com.knight.kotlin.library_base.activity.BaseActivity
import com.knight.kotlin.library_base.route.RouteActivity
import com.knight.kotlin.library_base.vm.EmptyViewModel
import com.knight.kotlin.module_square.databinding.SquareShareArticleActivityBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Author:Knight
 * Time:2022/4/6 17:48
 * Description:SquareShareArticleActivity
 */
@AndroidEntryPoint
@Route(path = RouteActivity.Square.SquareShareArticleActivity)
class SquareShareArticleActivity :BaseActivity<SquareShareArticleActivityBinding,EmptyViewModel>() {
    override val mViewModel: EmptyViewModel by viewModels()
    override fun setThemeColor(isDarkMode: Boolean) {
        TODO("Not yet implemented")
    }

    override fun SquareShareArticleActivityBinding.initView() {
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
}