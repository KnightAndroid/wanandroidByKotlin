package com.knight.kotlin.module_eye_discover.fragment

import androidx.recyclerview.widget.LinearLayoutManager
import com.knight.kotlin.library_base.entity.EyeMetroCard
import com.knight.kotlin.library_base.fragment.BaseFragment
import com.knight.kotlin.library_base.ktx.init
import com.knight.kotlin.library_base.route.RouteFragment
import com.knight.kotlin.library_base.util.CacheUtils
import com.knight.kotlin.module_eye_discover.adapter.EyeDiscoverSearchResultAdapter
import com.knight.kotlin.module_eye_discover.databinding.EyeDiscoverSearchItemFragmentBinding
import com.knight.kotlin.module_eye_discover.vm.EyeDiscoverSearchResultItemVm
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject

/**
 * @Description 搜索结果item项
 * @Author knight
 * @Time 2025/1/5 16:59
 *
 */
@AndroidEntryPoint
@Route(path = RouteFragment.Discover.DiscoverSearchResultItemFragment)
class EyeDiscoverSearchResultItemFragment : BaseFragment<EyeDiscoverSearchItemFragmentBinding,EyeDiscoverSearchResultItemVm>(),
    OnLoadMoreListener {




    private lateinit var type:String


        //发现适配器
    private val mEyeDiscoverSearchResultAdapter: EyeDiscoverSearchResultAdapter by lazy { EyeDiscoverSearchResultAdapter(mutableListOf()) }
    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun initObserver() {

    }

    override fun initRequestData() {

    }

    override fun reLoadData() {

    }

    override fun EyeDiscoverSearchItemFragmentBinding.initView() {
        type = arguments?.getString("type","") ?:""
        searchResultRv.init(LinearLayoutManager(activity),mEyeDiscoverSearchResultAdapter,true)
        mBinding.searchResultRefreshLayout.setOnLoadMoreListener(this@EyeDiscoverSearchResultItemFragment)
        mBinding.searchResultRefreshLayout.setEnableRefresh(false)
        val firstDataJson =CacheUtils.getCacheValue(type)!!
        val datas = Json.decodeFromString<List<EyeMetroCard<JsonObject>>>(firstDataJson)
        mEyeDiscoverSearchResultAdapter.submitList(datas)




    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {

    }
}