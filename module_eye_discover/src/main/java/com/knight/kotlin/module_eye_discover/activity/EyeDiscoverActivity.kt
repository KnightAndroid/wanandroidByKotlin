package com.knight.kotlin.module_eye_discover.activity

import androidx.recyclerview.widget.LinearLayoutManager
import com.knight.kotlin.library_base.activity.BaseActivity
import com.knight.kotlin.library_base.ktx.init
import com.knight.kotlin.library_base.ktx.setOnClick
import com.knight.kotlin.library_base.route.RouteActivity
import com.knight.kotlin.module_eye_discover.R
import com.knight.kotlin.module_eye_discover.adapter.EyeDiscoverAdapter
import com.knight.kotlin.module_eye_discover.databinding.EyeDiscoverActivityBinding
import com.knight.kotlin.module_eye_discover.vm.EyeDiscoverVm
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
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
class EyeDiscoverActivity : BaseActivity<EyeDiscoverActivityBinding, EyeDiscoverVm>(), OnRefreshListener{



    //发现适配器
    private val mEyeDiscoverAdapter: EyeDiscoverAdapter by lazy { EyeDiscoverAdapter(
        this,mutableListOf()) }

    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun initObserver() {

    }

    override fun initRequestData() {
        mViewModel.getDiscoverData().observerKt {
            mBinding.discoverListRefreshLayout.finishRefresh()
            mEyeDiscoverAdapter.submitList(it)
        }
    }

    override fun reLoadData() {

    }

    override fun EyeDiscoverActivityBinding.initView() {
        includeEyeDiscoverToolbar.baseTvTitle.text = getString(R.string.eye_discover_toolbar_name)
        discoverListRefreshLayout.setOnRefreshListener(this@EyeDiscoverActivity)
        includeEyeDiscoverToolbar.baseIvBack.setOnClick {
            finish()
        }
        rvDiscoverList.init(
            LinearLayoutManager(this@EyeDiscoverActivity),
            mEyeDiscoverAdapter,
            true
        )
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        initRequestData()
    }
}