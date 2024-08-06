package com.knight.kotlin.module_eye_discover.activity

import com.knight.kotlin.library_base.activity.BaseActivity
import com.knight.kotlin.library_base.route.RouteActivity
import com.knight.kotlin.library_util.LogUtils
import com.knight.kotlin.module_eye_discover.databinding.EyeDiscoverActivityBinding
import com.knight.kotlin.module_eye_discover.vm.EyeDiscoverVm
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
class EyeDiscoverActivity : BaseActivity<EyeDiscoverActivityBinding, EyeDiscoverVm>(){
    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun initObserver() {

    }

    override fun initRequestData() {
        mViewModel.getDiscoverData().observerKt {
            LogUtils.d(it.size.toString())
        }
    }

    override fun reLoadData() {

    }

    override fun EyeDiscoverActivityBinding.initView() {

    }
}