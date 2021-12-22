package com.knight.kotlin.module_main

import androidx.activity.viewModels
import com.alibaba.android.arouter.facade.annotation.Route
import com.knight.kotlin.library_base.activity.BaseActivity
import com.knight.kotlin.library_base.route.RouteActivity
import com.knight.kotlin.module_main.databinding.MainActivityBinding
import com.knight.kotlin.module_main.vm.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@Route(path = RouteActivity.Main.MainActivity)
class MainActivity : BaseActivity<MainActivityBinding,MainViewModel>() {


    /**
     *
     * MainActivity的ViewModel 通过Hilt自动注入
     */
    override val mViewModel:MainViewModel by viewModels()


    override fun MainActivityBinding.initView() {

    }

    override fun initObserver() {}

    override fun initRequestData() {}
}