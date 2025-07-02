package com.knight.kotlin.module_constellate.activity


import com.knight.kotlin.library_base.activity.BaseActivity
import com.core.library_base.route.RouteActivity
import com.core.library_base.vm.EmptyViewModel
import com.knight.kotlin.module_constellate.R
import com.knight.kotlin.module_constellate.databinding.ConstellateFateActivityBinding
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint


/**
 * @author created by luguian
 * @organize 
 * @Date 2025/6/19 9:25
 * @descript:星座具体信息
 */
@AndroidEntryPoint
@Route(path = RouteActivity.Constellate.ConstellateFateActivity)
class ConstellateFateActivity : BaseActivity<ConstellateFateActivityBinding, EmptyViewModel>(){
    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun initObserver() {

    }

    override fun initRequestData() {

    }

    override fun reLoadData() {

    }

    override fun ConstellateFateActivityBinding.initView() {
        constellateCloudView.setCloudImages(R.drawable.constellate_cloud_behind, R.drawable.constellate_cloud_front,0.5f,1.0f)
        constellateCloudView.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding.constellateCloudView.stop()
    }

}