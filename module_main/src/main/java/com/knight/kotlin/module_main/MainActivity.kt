package com.knight.kotlin.module_main

import androidx.activity.viewModels
import com.alibaba.android.arouter.facade.annotation.Route
import com.knight.kotlin.library_base.activity.BaseActivity
import com.knight.kotlin.library_base.route.RouteActivity
import com.knight.kotlin.library_util.ViewInitUtils
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
        ViewInitUtils.setViewPager2Init(this@MainActivity,mainViewpager,mViewModel.fragments,false)
        btnNav.run {
            setOnNavigationItemSelectedListener {
                when(it.itemId) {
                    R.id.homeFragment -> mainViewpager.setCurrentItem(0,false)
                    R.id.squareFragment -> mainViewpager.setCurrentItem(1,false)
                    R.id.projectFragment -> mainViewpager.setCurrentItem(2,false)
                    R.id.navigateFragment -> mainViewpager.setCurrentItem(3,false)
                    R.id.mineFragment -> mainViewpager.setCurrentItem(4,false)
                }
                true
            }
        }
    }

    override fun initObserver() {}

    override fun initRequestData() {}
}