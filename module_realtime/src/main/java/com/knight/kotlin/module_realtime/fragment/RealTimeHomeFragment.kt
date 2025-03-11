package com.knight.kotlin.module_realtime.fragment

import android.graphics.Color
import com.google.android.material.tabs.TabLayoutMediator
import com.knight.kotlin.library_base.fragment.BaseFragment
import com.knight.kotlin.library_base.route.RouteFragment
import com.knight.kotlin.library_base.util.CacheUtils
import com.knight.kotlin.library_util.ViewInitUtils
import com.knight.kotlin.module_realtime.databinding.RealtimeHomeFragmentBinding
import com.knight.kotlin.module_realtime.vm.RealTimeHomeVm
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint


/**
 * @author created by luguian
 * @organize
 * @Date 2025/3/11 16:02
 * @descript:热搜首页
 */

@AndroidEntryPoint
@Route(path = RouteFragment.RealTime.RealTimeHomeFragment)
class RealTimeHomeFragment : BaseFragment<RealtimeHomeFragmentBinding, RealTimeHomeVm>(){

    private val mFragments = mutableListOf<RealTimeMainFragment>()

    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun initObserver() {

    }

    override fun initRequestData() {
        mViewModel.getMainBaiduRealTime().observerKt {
            var  mNavDatas = it.tabBoard

            for (index  in 0 until  it.tabBoard.size) {

                mFragments.add(RealTimeMainFragment())

//                try {
//                    CacheUtils.saveCacheValue(it.item_list[index].nav.type, Json.encodeToString(ListSerializer(EyeMetroCard.serializer(JsonObject.serializer())),it.item_list[index].card_list.get(0).card_data?.body?.metro_list!!))
//                    api_request = it.item_list[index].card_list.last().card_data?.body?.api_request
//                } catch (e: NullPointerException) {
//                    e.printStackTrace()
//                }
//                mFragments.add(RealTimeMainFragment().also {
//                    it.arguments = bundleOf("type" to mNavDatas[index].type,"api_Request" to Json.encodeToString(EyeApiRequest.serializer(), api_request?.let { it } ?: run{ EyeApiRequest() }))
//                })
            }

            if (mFragments.size > 0) {
                ViewInitUtils.setViewPager2Init(requireActivity(),mBinding.realtimeViewPager,mFragments,
                    isOffscreenPageLimit = true,
                    isUserInputEnabled = false
                )

                TabLayoutMediator(mBinding.realtimeTabLayout, mBinding.realtimeViewPager) { tab, pos ->
                    tab.text = mNavDatas[pos].text}.attach()

                mBinding.realtimeTabLayout.setSelectedTabIndicatorColor(CacheUtils.getThemeColor())
                mBinding.realtimeTabLayout.setTabTextColors(Color.parseColor("#CC000000"), CacheUtils.getThemeColor())
            }





        }
    }

    override fun reLoadData() {

    }

    override fun RealtimeHomeFragmentBinding.initView() {

    }

}