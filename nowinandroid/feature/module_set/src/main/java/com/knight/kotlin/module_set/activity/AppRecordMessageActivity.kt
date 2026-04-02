package com.knight.kotlin.module_set.activity

import androidx.recyclerview.widget.LinearLayoutManager
import com.core.library_base.ktx.init
import com.core.library_base.ktx.setOnClick
import com.core.library_base.route.RouteActivity
import com.knight.kotlin.library_base.activity.BaseMviActivity
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.module_set.R
import com.knight.kotlin.module_set.adapter.VersionRecordAdapter
import com.knight.kotlin.module_set.contract.AppUpdateRecordContract
import com.knight.kotlin.module_set.databinding.SetVersionRecordActivityBinding
import com.knight.kotlin.module_set.vm.AppUpdateRecordVm
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint

/**
 * Author:Knight
 * Time:2022/8/26 11:18
 * Description:AppRecordMessageActivity
 */
@AndroidEntryPoint
@Route(path = RouteActivity.Set.AppRecordMessageActivity)
class AppRecordMessageActivity : BaseMviActivity<
        SetVersionRecordActivityBinding,
        AppUpdateRecordVm,
        AppUpdateRecordContract.Event,
        AppUpdateRecordContract.State,
        AppUpdateRecordContract.Effect>(),
    OnRefreshListener {

    private val mVersionRecordAdapter by lazy { VersionRecordAdapter() }

    override fun setThemeColor(isDarkMode: Boolean) {}

    override fun SetVersionRecordActivityBinding.initView() {
        includeVersionRecordToolbar.baseTvTitle.text = getString(R.string.set_app_record)
        includeVersionRecordToolbar.baseIvBack.setOnClick { finish() }

        requestLoading(includeVersionRecordCollectfreshalayout.baseFreshlayout)

        includeVersionRecordCollectfreshalayout.baseBodyRv.init(
            LinearLayoutManager(this@AppRecordMessageActivity),
            mVersionRecordAdapter,
            false
        )

        includeVersionRecordCollectfreshalayout.baseFreshlayout
            .setOnRefreshListener(this@AppRecordMessageActivity)
    }

    override fun initObserver() {

    }

    override fun initRequestData() {
        // 首次加载
        mViewModel.setEvent(AppUpdateRecordContract.Event.LoadData)
    }

    //===================== MVI =====================//

    override fun initEvent() {

    }

    override fun renderState(state: AppUpdateRecordContract.State) {

        // 结束刷新
        mBinding.includeVersionRecordCollectfreshalayout.baseFreshlayout.finishRefresh()

        if (state.isLoading) {
            requestLoading(mBinding.includeVersionRecordCollectfreshalayout.baseFreshlayout)
        }

        state.listData?.let { data ->
            requestSuccess()

            if (!data.datas.isNullOrEmpty()) {
                mVersionRecordAdapter.submitList(data.datas)
            } else {
                requestEmptyData()
            }
        }
    }

    override fun handleEffect(effect: AppUpdateRecordContract.Effect) {
        when (effect) {
            is AppUpdateRecordContract.Effect.ShowError -> {
                mBinding.includeVersionRecordCollectfreshalayout.baseFreshlayout.finishRefresh()
                requestFailure()
                toast(effect.msg)
            }
        }
    }

    //===================== 刷新 =====================//

    override fun onRefresh(refreshLayout: RefreshLayout) {
        mViewModel.setEvent(AppUpdateRecordContract.Event.LoadData)
    }

    override fun reLoadData() {
        // LoadSir 重试
        mViewModel.setEvent(AppUpdateRecordContract.Event.LoadData)
    }
}