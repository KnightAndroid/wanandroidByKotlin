package com.knight.kotlin.module_utils.activity

import androidx.recyclerview.widget.LinearLayoutManager
import com.core.library_base.ktx.init
import com.core.library_base.ktx.setOnClick
import com.core.library_base.route.RouteActivity
import com.knight.kotlin.library_base.activity.BaseActivity
import com.knight.kotlin.library_base.activity.BaseMviActivity
import com.knight.kotlin.library_util.startPageWithParams
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.library_widget.ktx.setSafeOnItemClickListener
import com.knight.kotlin.module_utils.R
import com.knight.kotlin.module_utils.adapter.UtilItemAdapter
import com.knight.kotlin.module_utils.contract.UtilsContract
import com.knight.kotlin.module_utils.databinding.UtilsActivityBinding
import com.knight.kotlin.module_utils.entity.UtilsEntity
import com.knight.kotlin.module_utils.vm.UtilsVm
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
@Route(path = RouteActivity.Utils.UtilsActivity)
class UtilsActivity :
    BaseMviActivity<
            UtilsActivityBinding,
            UtilsVm,
            UtilsContract.Event,
            UtilsContract.State,
            UtilsContract.Effect>(),
    OnRefreshListener {

    // ========================
    // Adapter
    // ========================
    private val mUtilItemAdapter by lazy { UtilItemAdapter() }

    // ========================
    // 初始化 View
    // ========================
    override fun UtilsActivityBinding.initView() {

        title= getString(R.string.utils_title)
        includeUtilsToolbar.baseIvBack.setOnClick { finish() }

        includeUtilsRv.baseBodyRv.init(
            LinearLayoutManager(this@UtilsActivity),
            mUtilItemAdapter,
            false
        )

        includeUtilsRv.baseFreshlayout.setOnRefreshListener(this@UtilsActivity)
        includeUtilsRv.baseFreshlayout.setEnableLoadMore(false)

        initListener()
    }

    // ========================
    // 订阅（一般可空）
    // ========================
    override fun initObserver() {}

    // ========================
    // 页面入口请求
    // ========================
    override fun initRequestData() {
        mViewModel.setEvent(UtilsContract.Event.LoadUtils)
    }

    // ========================
    // 渲染 State
    // ========================
    override fun renderState(state: UtilsContract.State) {

        if (state.isLoading) {
            requestLoading(mBinding.includeUtilsRv.baseFreshlayout)
        } else {
            requestSuccess()
            mBinding.includeUtilsRv.baseFreshlayout.finishRefresh()
        }

        mUtilItemAdapter.submitList(state.utils)
    }

    // ========================
    // 处理 Effect（一次性）
    // ========================
    override fun handleEffect(effect: UtilsContract.Effect) {
        when (effect) {
            is UtilsContract.Effect.ShowError -> {
                toast(effect.message)
            }
        }
    }

    // ========================
    // 下拉刷新
    // ========================
    override fun onRefresh(refreshLayout: RefreshLayout) {
        mViewModel.setEvent(UtilsContract.Event.LoadUtils)
    }

    // ========================
    // 点击事件
    // ========================
    private fun initListener() {
        mUtilItemAdapter.setSafeOnItemClickListener { _, _, position ->
            val item = mUtilItemAdapter.items[position]
            startPageWithParams(
                RouteActivity.Web.WebPager,
                "webUrl" to item.link,
                "webTitle" to item.tabName
            )
        }
    }

    override fun reLoadData() {
        mViewModel.setEvent(UtilsContract.Event.LoadUtils)
    }

    override fun setThemeColor(isDarkMode: Boolean) {}
}