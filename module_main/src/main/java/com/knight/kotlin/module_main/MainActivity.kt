package com.knight.kotlin.module_main

import android.view.KeyEvent
import androidx.activity.viewModels
import com.alibaba.android.arouter.facade.annotation.Route
import com.knight.kotlin.library_base.activity.BaseActivity
import com.knight.kotlin.library_base.route.RouteActivity
import com.knight.kotlin.library_base.util.ActivityManagerUtils
import com.knight.kotlin.library_base.util.CacheUtils
import com.knight.kotlin.library_base.util.ColorUtils
import com.knight.kotlin.library_util.ViewInitUtils
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.module_main.databinding.MainActivityBinding
import com.knight.kotlin.module_main.vm.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@Route(path = RouteActivity.Main.MainActivity)
class MainActivity : BaseActivity<MainActivityBinding,MainViewModel>() {


    /**
     * MainActivity的ViewModel 通过Hilt自动注入
     */
    override val mViewModel:MainViewModel by viewModels()

    private var mExitAppTime: Long = 0

    override fun MainActivityBinding.initView() {
        ViewInitUtils.setViewPager2Init(this@MainActivity,mainViewpager,mViewModel.fragments,
            isOffscreenPageLimit = true,
            isUserInputEnabled = false
        )
        btnNav.run {
            itemTextColor = ColorUtils.createColorStateList(CacheUtils.getThemeColor(),ColorUtils.convertToColorInt("a6a6a6"))
            itemIconTintList = ColorUtils.createColorStateList(CacheUtils.getThemeColor(),ColorUtils.convertToColorInt("a6a6a6"))
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
    override fun reLoadData() {

    }

    override fun setThemeColor(isDarkMode: Boolean) {

    }


    override fun onKeyDown(keyCode:Int,event: KeyEvent):Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - mExitAppTime <= 2000) {
                ActivityManagerUtils.getInstance()?.finishAllActivity()
                System.exit(0)
            } else {
                mExitAppTime = System.currentTimeMillis()
                toast(R.string.main_exit_tip)
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }


    override fun onDestroy() {
        super.onDestroy()
        mViewModel.fragments.clear()

    }
}