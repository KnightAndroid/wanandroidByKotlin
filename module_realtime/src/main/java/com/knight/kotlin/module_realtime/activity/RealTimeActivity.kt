package com.knight.kotlin.module_realtime.activity

import androidx.fragment.app.Fragment
import com.knight.kotlin.library_base.activity.BaseActivity
import com.knight.kotlin.library_base.route.RouteActivity
import com.knight.kotlin.library_base.vm.EmptyViewModel
import com.knight.kotlin.module_realtime.databinding.RealtimeMainActivityBinding
import com.knight.kotlin.module_realtime.fragment.RealTimeHomeFragment
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@Route(path = RouteActivity.RealTime.RealTimeMainActivity)
class RealTimeActivity : BaseActivity<RealtimeMainActivityBinding,EmptyViewModel>() {
    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun initObserver() {

    }

    override fun initRequestData() {

    }

    override fun reLoadData() {

    }

    override fun RealtimeMainActivityBinding.initView() {
        val fm = supportFragmentManager
        val ft = fm.beginTransaction()

        val fragment: Fragment =  RealTimeHomeFragment()
        if (fragment.isAdded) {
            ft.show(fragment)
        } else {
            ft.add(fragmentContainerView.id, fragment)
        }
        ft.commitNowAllowingStateLoss()
    }
}