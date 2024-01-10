package com.knight.kotlin.module_set.activity

import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.knight.kotlin.library_base.activity.BaseActivity
import com.knight.kotlin.library_base.ktx.init
import com.knight.kotlin.library_base.ktx.observeLiveData
import com.knight.kotlin.library_base.ktx.setOnClick
import com.knight.kotlin.library_base.route.RouteActivity
import com.knight.kotlin.module_set.R
import com.knight.kotlin.module_set.adapter.VersionRecordAdapter
import com.knight.kotlin.module_set.databinding.SetVersionRecordActivityBinding
import com.knight.kotlin.module_set.entity.VersionRecordListEntity
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
class AppRecordMessageActivity : BaseActivity<SetVersionRecordActivityBinding,AppUpdateRecordVm>(),
    OnRefreshListener {
    override val mViewModel: AppUpdateRecordVm by viewModels()

    private val mVersionRecordAdapter:VersionRecordAdapter by lazy {VersionRecordAdapter(arrayListOf())}
    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun SetVersionRecordActivityBinding.initView() {
        includeVersionRecordToolbar.baseTvTitle.setText(getString(R.string.set_app_record))
        includeVersionRecordToolbar.baseIvBack.setOnClick { finish() }
        requestLoading(includeVersionRecordCollectfreshalayout.baseFreshlayout)
        includeVersionRecordCollectfreshalayout.baseBodyRv.init(LinearLayoutManager(this@AppRecordMessageActivity),mVersionRecordAdapter,false)
        includeVersionRecordCollectfreshalayout.baseFreshlayout.setOnRefreshListener(this@AppRecordMessageActivity)
    }

    override fun initObserver() {
        observeLiveData(mViewModel.appUpdateRecordMessage,::getAppRecordMessage)
    }

    override fun initRequestData() {
        mViewModel.checkAppUpdateMessage()
    }

    override fun reLoadData() {

    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        mViewModel.checkAppUpdateMessage()
    }

    private fun getAppRecordMessage(data: VersionRecordListEntity) {
        requestSuccess()
        mBinding.includeVersionRecordCollectfreshalayout.baseFreshlayout.finishRefresh()
        if (data.datas.size > 0) {
            mVersionRecordAdapter.setNewInstance(data.datas)
        } else {
            requestEmptyData()
        }
    }

}