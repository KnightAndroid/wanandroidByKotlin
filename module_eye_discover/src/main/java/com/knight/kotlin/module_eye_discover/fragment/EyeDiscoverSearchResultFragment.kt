package com.knight.kotlin.module_eye_discover.fragment

import com.knight.kotlin.library_base.fragment.BaseFragment
import com.knight.kotlin.library_base.route.RouteFragment
import com.knight.kotlin.library_base.vm.EmptyViewModel
import com.knight.kotlin.library_util.LogUtils
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




    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun initObserver() {

    }

    override fun initRequestData() {
         mViewModel.getSearchResultByQuery(query).observerKt {
            LogUtils.d(it.item_list.size.toString()+"sddsd")
         }
    }

    override fun reLoadData() {

    }

    override fun EyeDiscoverSearchResultFragmentBinding.initView() {
          query = arguments?.getString("query","") ?: ""
    }
}