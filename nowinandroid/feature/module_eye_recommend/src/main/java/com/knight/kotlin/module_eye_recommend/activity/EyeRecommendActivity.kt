package com.knight.kotlin.module_eye_recommend.activity

import com.core.library_base.route.RouteActivity
import com.knight.kotlin.library_base.activity.BaseActivity
import com.knight.kotlin.module_eye_recommend.R
import com.knight.kotlin.module_eye_recommend.adapter.EyeRecommendAdapter
import com.knight.kotlin.module_eye_recommend.databinding.EyeRecommendActivityBinding
import com.knight.kotlin.module_eye_recommend.vm.EyeRecommendVm
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
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
class EyeRecommendActivity: BaseActivity<EyeRecommendActivityBinding, EyeRecommendVm>(), OnRefreshListener, OnLoadMoreListener {


    //适配器
    private val mEyeRecommendAdater: EyeRecommendAdapter by lazy {
        EyeRecommendAdapter(mutableListOf())
    }

    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun initObserver() {

    }

    override fun initRequestData() {
         mViewModel.getRecommendData("card","recommend").observerKt {
               //后续处理 要筛选掉广告，因为广告图片打不开

         }
    }

    override fun reLoadData() {

    }

    override fun EyeRecommendActivityBinding.initView() {
        mBinding.title = getString(R.string.eye_recommend_video_title)
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        TODO("Not yet implemented")
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        TODO("Not yet implemented")
    }
}