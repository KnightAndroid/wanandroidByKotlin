package com.knight.kotlin.module_eye_discover.fragment

import android.graphics.Color
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import com.google.android.material.tabs.TabLayoutMediator
import com.knight.kotlin.library_base.fragment.BaseFragment
import com.knight.kotlin.library_base.ktx.toHtml
import com.knight.kotlin.library_base.route.RouteFragment
import com.knight.kotlin.library_base.util.CacheUtils
import com.knight.kotlin.library_base.util.ColorUtils
import com.knight.kotlin.library_base.vm.EmptyViewModel
import com.knight.kotlin.library_util.LogUtils
import com.knight.kotlin.library_util.ViewInitUtils
import com.knight.kotlin.library_util.bindWechatViewPager2
import com.knight.kotlin.module_eye_discover.api.EyeDiscoverSearchResultApi
import com.knight.kotlin.module_eye_discover.databinding.EyeDiscoverSearchResultFragmentBinding
import com.knight.kotlin.module_eye_discover.entity.EyeSearchResultItem
import com.knight.kotlin.module_eye_discover.vm.EyeDiscoverSearchResultVm
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint

/**
 * @Description 开眼搜索界面
 * @Author knight
 * @Time 2024/12/30 20:24
 *
 */
@AndroidEntryPoint
@Route(path = RouteFragment.Discover.DiscoverSearchResultFragment)
class EyeDiscoverSearchResultFragment:BaseFragment<EyeDiscoverSearchResultFragmentBinding, EyeDiscoverSearchResultVm>() {

   private lateinit var query:String

    private val mFragments = mutableListOf<EyeDiscoverSearchResultItemFragment>()
   // private val mTitileDatas = mutableListOf<String>()

    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun initObserver() {

    }

    override fun initRequestData() {
         mViewModel.getSearchResultByQuery(query).observerKt {
            LogUtils.d(it.item_list.size.toString()+"sddsd")
            var  mNavDatas = it.item_list.map { it.nav } .toMutableList()
             for (index  in 0 until  it.item_list.size) {
                 mFragments.add(EyeDiscoverSearchResultItemFragment().also {
                     it.arguments = bundleOf("type" to mNavDatas[index].type)
                 })
             }
             ViewInitUtils.setViewPager2Init(requireActivity(),mBinding.searchResultViewPager,mFragments,
                 isOffscreenPageLimit = true,
                 isUserInputEnabled = false
             )

             TabLayoutMediator(mBinding.searchResultItemIndicator, mBinding.searchResultViewPager) { tab, pos ->
                 tab.text = mNavDatas[pos].title }.attach()

             mBinding.searchResultItemIndicator.setSelectedTabIndicatorColor(CacheUtils.getThemeColor())
             mBinding.searchResultItemIndicator.setTabTextColors(Color.parseColor("#CC000000"),CacheUtils.getThemeColor())
         }
    }

    override fun reLoadData() {

    }

    override fun EyeDiscoverSearchResultFragmentBinding.initView() {
          query = arguments?.getString("query","") ?: ""
    }
}