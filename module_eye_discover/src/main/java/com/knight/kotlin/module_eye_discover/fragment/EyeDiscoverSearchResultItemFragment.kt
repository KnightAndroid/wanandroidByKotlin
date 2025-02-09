package com.knight.kotlin.module_eye_discover.fragment

import android.os.Build
import androidx.recyclerview.widget.LinearLayoutManager
import com.knight.kotlin.library_base.entity.EyeApiRequest
import com.knight.kotlin.library_base.entity.EyeMetroCard
import com.knight.kotlin.library_base.fragment.BaseFragment
import com.knight.kotlin.library_base.ktx.init
import com.knight.kotlin.library_base.route.RouteFragment
import com.knight.kotlin.library_base.util.CacheUtils
import com.knight.kotlin.library_util.LogUtils
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
import kotlinx.serialization.json.jsonPrimitive

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



    //类型
    private lateinit var type:String
    //下一页Url请求连接
    private  var api_request : EyeApiRequest? = null

    //搜索结果适配器
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

        val apiReqyestJson = arguments?.getString("api_Request") // 获取 JSON 字符串
        api_request = apiReqyestJson?.let { Json.decodeFromString(EyeApiRequest.serializer(), it) } // 反序列化回对象
        searchResultRv.init(LinearLayoutManager(activity),mEyeDiscoverSearchResultAdapter,true)
        mBinding.searchResultRefreshLayout.setOnLoadMoreListener(this@EyeDiscoverSearchResultItemFragment)
        mBinding.searchResultRefreshLayout.setEnableRefresh(false)
        val firstDataJson =CacheUtils.getCacheValue(type)!!
        val datas = Json.decodeFromString<List<EyeMetroCard<JsonObject>>>(firstDataJson)

        mEyeDiscoverSearchResultAdapter.submitList(datas)




    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        var map = api_request?.params?.mapValues { param ->
            param.value.jsonPrimitive.content
        }?.toMutableMap() ?: mutableMapOf()
       mViewModel.getMoreDataSearchByQuery(api_request!!.url,map).observerKt {
           it.item_list?.let {
               mEyeDiscoverSearchResultAdapter.addAll(it)
           }
           mBinding.searchResultRefreshLayout.finishLoadMore()

       }
    }
}