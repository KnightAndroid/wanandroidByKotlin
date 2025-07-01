package com.knight.kotlin.module_eye_discover.fragment

import android.graphics.Color
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import com.core.library_base.entity.EyeApiRequest
import com.core.library_base.entity.EyeMetroCard
import com.core.library_base.fragment.BaseFragment
import com.core.library_base.route.RouteFragment
import com.core.library_base.util.CacheUtils
import com.google.android.material.tabs.TabLayoutMediator
import com.knight.kotlin.library_util.LogUtils
import com.knight.kotlin.library_util.ViewInitUtils
import com.knight.kotlin.library_util.bindWechatViewPager2
import com.knight.kotlin.module_eye_discover.api.EyeDiscoverSearchResultApi
import com.knight.kotlin.module_eye_discover.databinding.EyeDiscoverSearchResultFragmentBinding
import com.knight.kotlin.module_eye_discover.entity.EyeSearchResultItem
import com.knight.kotlin.module_eye_discover.vm.EyeDiscoverSearchResultVm
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.builtins.ListSerializer

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.Json.Default.encodeToString
import kotlinx.serialization.json.JsonObject

/**
 * @Description 开眼搜索界面
 * @Author knight
 * @Time 2024/12/30 20:24
 *
 */
@AndroidEntryPoint
@Route(path = RouteFragment.Discover.DiscoverSearchResultFragment)
class EyeDiscoverSearchResultFragment:
    BaseFragment<EyeDiscoverSearchResultFragmentBinding, EyeDiscoverSearchResultVm>() {

   private lateinit var query:String

    private val mFragments = mutableListOf<EyeDiscoverSearchResultItemFragment>()
   private  var api_request : EyeApiRequest? = null

    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun initObserver() {

    }

    override fun initRequestData() {
         mViewModel.getSearchResultByQuery(query).observerKt {


            var  mNavDatas = it.item_list.map { it.nav } .toMutableList()
             for (index  in 0 until  it.item_list.size) {
                 try {
                     CacheUtils.saveCacheValue(it.item_list[index].nav.type, Json.encodeToString(ListSerializer(
                         EyeMetroCard.serializer(JsonObject.serializer())),it.item_list[index].card_list.get(0).card_data?.body?.metro_list!!))
                     api_request = it.item_list[index].card_list.last().card_data?.body?.api_request
                 } catch (e: NullPointerException) {
                     e.printStackTrace()
                }
                 mFragments.add(EyeDiscoverSearchResultItemFragment().also {
                     it.arguments = bundleOf("type" to mNavDatas[index].type,"api_Request" to Json.encodeToString(EyeApiRequest.serializer(), api_request?.let { it } ?: run{EyeApiRequest()}))
                 })
             }
             if (mFragments.size > 0) {
                 ViewInitUtils.setViewPager2Init(requireActivity(),mBinding.searchResultViewPager,mFragments,
                     isOffscreenPageLimit = true,
                     isUserInputEnabled = false
                 )

                 TabLayoutMediator(mBinding.searchResultItemIndicator, mBinding.searchResultViewPager) { tab, pos ->
                     tab.text = mNavDatas[pos].title }.attach()

                 mBinding.searchResultItemIndicator.setSelectedTabIndicatorColor(CacheUtils.getThemeColor())
                 mBinding.searchResultItemIndicator.setTabTextColors(if (CacheUtils.getNormalDark() ) Color.parseColor("#D3D3D3") else Color.parseColor("#CC000000"),CacheUtils.getThemeColor())
             }

         }
    }

    override fun reLoadData() {

    }

    override fun EyeDiscoverSearchResultFragmentBinding.initView() {
          query = arguments?.getString("query","") ?: ""
    }
}