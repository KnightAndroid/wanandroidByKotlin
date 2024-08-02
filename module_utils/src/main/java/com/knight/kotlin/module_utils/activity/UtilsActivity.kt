package com.knight.kotlin.module_utils.activity

import androidx.recyclerview.widget.LinearLayoutManager
import com.knight.kotlin.library_base.activity.BaseActivity
import com.knight.kotlin.library_base.ktx.init
import com.knight.kotlin.library_base.ktx.setOnClick
import com.knight.kotlin.library_base.route.RouteActivity
import com.knight.kotlin.library_util.startPageWithParams
import com.knight.kotlin.module_utils.R
import com.knight.kotlin.module_utils.adapter.UtilItemAdapter
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
class UtilsActivity : BaseActivity<UtilsActivityBinding,UtilsVm>(),OnRefreshListener,OnLoadMoreListener {


    //工具类适配器
    private val mUtilItemAdapter:UtilItemAdapter by lazy {UtilItemAdapter()}
    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun UtilsActivityBinding.initView() {
        includeUtilsToolbar.baseTvTitle.text = getString(R.string.utils_title)
        includeUtilsToolbar.baseIvBack.setOnClick { finish() }
        includeUtilsRv.baseBodyRv.init(LinearLayoutManager(this@UtilsActivity),mUtilItemAdapter,false)
        includeUtilsRv.baseFreshlayout.setOnRefreshListener(this@UtilsActivity)
        includeUtilsRv.baseFreshlayout.setEnableLoadMore(false)
        requestLoading(includeUtilsRv.baseFreshlayout)
        initListener()
    }

    override fun initObserver() {

    }

    override fun initRequestData() {
        mViewModel.getUtils().observerKt {
            setUtils(it)
        }
    }

    override fun reLoadData() {

    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        mViewModel.getUtils().observerKt {
            setUtils(it)
        }
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {

    }


    private fun setUtils(data:MutableList<UtilsEntity>) {
        requestSuccess()
        mBinding.includeUtilsRv.baseFreshlayout.finishRefresh()
        mUtilItemAdapter.submitList(data)

    }

    private fun initListener() {
        mUtilItemAdapter.run {
            setOnItemClickListener { adapter, view, position ->
                startPageWithParams(RouteActivity.Web.WebPager,
                    "webUrl" to items[position].link,
                    "webTitle" to items[position].tabName)
            }
        }
    }
}