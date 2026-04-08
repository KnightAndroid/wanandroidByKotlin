package com.knight.kotlin.module_realtime.fragment


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.core.library_base.route.RouteFragment
import com.google.android.material.tabs.TabLayoutMediator
import com.knight.kotlin.library_base.fragment.BaseMviFragment
import com.knight.kotlin.library_util.ViewInitUtils
import com.knight.kotlin.module_realtime.R
import com.knight.kotlin.module_realtime.contract.RealtimeNovelContract
import com.knight.kotlin.module_realtime.databinding.RealtimeNovelFragmentBinding
import com.knight.kotlin.module_realtime.vm.RealTimeNovelVm
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint


/**
 * @author created by luguian
 * @organize
 * @Date 2025/3/20 9:30
 * @descript:小说界面
 */
@AndroidEntryPoint
@Route(path = RouteFragment.RealTime.RealTimeNovelFragment)
class RealTimeNovelFragment :
    BaseMviFragment<
            RealtimeNovelFragmentBinding,
            RealTimeNovelVm,
            RealtimeNovelContract.Event,
            RealtimeNovelContract.State,
            RealtimeNovelContract.Effect>() {

    private val fragments = mutableListOf<Fragment>()

    override fun setThemeColor(isDarkMode: Boolean) {}

    override fun initRequestData() {
        sendEvent(RealtimeNovelContract.Event.Init)
    }

    override fun reLoadData() {
        sendEvent(RealtimeNovelContract.Event.Init)
    }

    override fun RealtimeNovelFragmentBinding.initView() {
        requestLoading(realtimeNovelViewPager)
    }

    override fun initObserver() {

    }

    // =========================
    // 渲染核心
    // =========================
    override fun renderState(state: RealtimeNovelContract.State) {

        if (state.isLoading) {
            requestLoading(mBinding.realtimeNovelViewPager)
            return
        }

        if (state.tabs.isEmpty()) {
            requestEmptyData()
            return
        }

        requestSuccess()

        // ❗关键：防止重复 add
        if (fragments.isNotEmpty()) return

        fragments.clear()

        state.tabs.forEach { category ->
            fragments.add(
                RealTimeNovelChildFragment().apply {
                    arguments = bundleOf("category" to category)
                }
            )
        }

        initViewPager(state.tabs)
    }

    override fun handleEffect(effect: RealtimeNovelContract.Effect) {}

    // =========================
    // ViewPager 初始化
    // =========================
    private fun initViewPager(tabs: List<String>) {

        ViewInitUtils.setViewPager2Init(
            requireActivity(),
            mBinding.realtimeNovelViewPager,
            fragments,
            isOffscreenPageLimit = true,
            isUserInputEnabled = false
        )

        TabLayoutMediator(
            mBinding.realtimeNovelTabLayout,
            mBinding.realtimeNovelViewPager
        ) { tab, pos ->
            tab.text = tabs[pos]
        }.attach()

        // 自定义 Tab
        for (i in tabs.indices) {
            mBinding.realtimeNovelTabLayout.getTabAt(i)
                ?.setCustomView(createTabView(requireContext(), tabs[i]))
        }
    }

    // =========================
    // TabView
    // =========================
    private fun createTabView(context: Context, title: String): View {
        return LayoutInflater.from(context)
            .inflate(R.layout.realtime_novel_tab_indicator, null)
            .apply {
                findViewById<TextView>(R.id.tab_text).text = title
            }
    }
}