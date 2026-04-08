package com.knight.kotlin.module_realtime.fragment

import android.graphics.Color
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.core.library_base.annotation.EventBusRegister
import com.core.library_base.event.MessageEvent
import com.core.library_base.route.RouteFragment
import com.core.library_base.util.EventBusUtils
import com.core.library_common.util.dp2px
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.knight.kotlin.library_base.config.EventBusKeys
import com.knight.kotlin.library_base.entity.BaiduTabBoard
import com.knight.kotlin.library_base.fragment.BaseMviFragment
import com.knight.kotlin.library_base.ktx.statusHeight
import com.knight.kotlin.library_util.ThreadUtils
import com.knight.kotlin.library_util.ViewInitUtils
import com.knight.kotlin.module_realtime.contract.RealTimeHomeContract
import com.knight.kotlin.module_realtime.databinding.RealtimeHomeFragmentBinding
import com.knight.kotlin.module_realtime.enum.HotListEnum
import com.knight.kotlin.module_realtime.vm.RealTimeHomeVm
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import kotlin.math.abs


/**
 * @author created by luguian
 * @organize
 * @Date 2025/3/11 16:02
 * @descript:热搜首页
 */
@EventBusRegister
@AndroidEntryPoint
@Route(path = RouteFragment.RealTime.RealTimeHomeFragment)
class RealTimeHomeFragment :
    BaseMviFragment<
            RealtimeHomeFragmentBinding,
            RealTimeHomeVm,
            RealTimeHomeContract.Event,
            RealTimeHomeContract.State,
            RealTimeHomeContract.Effect>(),
    OnRefreshListener,
    RealTimeMainFragment.OnSelectRankListener {

    private val mFragments = mutableListOf<Fragment>()

    // =========================
    // 初始化
    // =========================
    override fun RealtimeHomeFragmentBinding.initView() {

        realtimeHomeSmartfresh.setOnRefreshListener(this@RealTimeHomeFragment)

        hideToolbar.setPadding(0, requireActivity().statusHeight + 48.dp2px(), 0, 0)

        appbar.addOnOffsetChangedListener { appbarLayout, offset ->
            EventBusUtils.postEvent(
                MessageEvent(MessageEvent.MessageType.AppBarOffsetChanged)
                    .put(
                        EventBusKeys.REALTIMESCROLLORIENTATION,
                        if (offset >= appbarLayout.totalScrollRange) 0 else 1
                    )
                    .put(EventBusKeys.OFFSET, offset)
            )
        }

        // ✅ 触发加载
        sendEvent(RealTimeHomeContract.Event.Init)
    }

    override fun initObserver() {

    }

    override fun initRequestData() {

    }

    override fun reLoadData() {

    }

    override fun setThemeColor(isDarkMode: Boolean) {}

    // =========================
    // MVI 渲染
    // =========================
    override fun renderState(state: RealTimeHomeContract.State) {

        when {
            state.isLoading -> {
                // 可加 loading UI
            }

            state.isError -> {
                // 可加错误 UI
            }

            state.tabList.isNotEmpty() -> {
                setupViewPager(state.tabList)
            }
        }
    }

    override fun handleEffect(effect: RealTimeHomeContract.Effect) {}

    // =========================
    // ViewPager + TabLayout
    // =========================
    private fun setupViewPager(tabList: List<BaiduTabBoard>) {

        mFragments.clear()

        tabList.forEach { tab ->
            mFragments.add(createFragmentByType(tab.typeName))
        }

        ViewInitUtils.setViewPager2Init(
            requireActivity(),
            mBinding.realtimeViewPager,
            mFragments,
            isOffscreenPageLimit = true,
            isUserInputEnabled = false
        )

        TabLayoutMediator(
            mBinding.realtimeTabLayout,
            mBinding.realtimeViewPager
        ) { tab, pos ->
            tab.text = tabList[pos].text
        }.attach()

        initTabLayoutStyle()
        initTabListener(tabList)
    }

    /**
     * 🔥 核心优化：替代 if-else
     */
    private fun createFragmentByType(type: String): Fragment {
        return when (type) {

            "homepage" -> RealTimeMainFragment().setSelectRankListener(this)

            HotListEnum.REALTIME.name.lowercase(),
            HotListEnum.FINANCE.name.lowercase(),
            HotListEnum.PHRASE.name.lowercase(),
            HotListEnum.LIVELIHOOD.name.lowercase() ->
                RealTimeTextFragment().apply {
                    arguments = bundleOf("typeName" to type.uppercase())
                }

            HotListEnum.NOVEL.name.lowercase() -> RealTimeNovelFragment()
            HotListEnum.MOVIE.name.lowercase() -> RealTimeMovieFragment()
            HotListEnum.TELEPLAY.name.lowercase() -> RealTimeTeleplayFragment()
            HotListEnum.CAR.name.lowercase() -> RealTimeCarFragment()
            HotListEnum.GAME.name.lowercase() -> RealTimeGameFragment()

            else -> RealTimeMainFragment()
        }
    }

    // =========================
    // Tab 样式
    // =========================
    private fun initTabLayoutStyle() {
        mBinding.realtimeTabLayout.setTabTextColors(
            Color.parseColor("#BBABAB"),
            Color.parseColor("#F84C48")
        )

        mBinding.realtimeTabLayout.setSelectedTabIndicatorColor(
            Color.parseColor("#F84C48")
        )
    }

    // =========================
    // Tab 监听（优化版）
    // =========================
    private fun initTabListener(tabList: List<BaiduTabBoard>) {

        mBinding.realtimeTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab ?: return

                val type = tabList.getOrNull(tab.position)?.typeName

                if (type == HotListEnum.MOVIE.name.lowercase() ||
                    type == HotListEnum.TELEPLAY.name.lowercase()
                ) {
                    mBinding.appbar.setExpanded(false, true)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    // =========================
    // EventBus（保留你原逻辑）
    // =========================
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: MessageEvent) {
        if (event.type == MessageEvent.MessageType.AppBarOffsetChanged) {

            val mHeight = 320.dp2px().toFloat()
            val offset = abs(event.getInt(EventBusKeys.OFFSET)).toFloat()

            val scale = if (offset / mHeight > 1) 1f else offset / mHeight
            val alpha = (scale * 255).toInt()

            mBinding.realtimeTabLayout.setBackgroundColor(
                Color.argb(alpha, 254, 79, 76)
            )

            if (offset != 0f) {
                mBinding.realtimeTabLayout.setTabTextColors(
                    Color.argb(255, 254, 165, 164),
                    Color.argb(255, 255, 255, 255)
                )
                mBinding.realtimeTabLayout.setSelectedTabIndicatorColor(
                    Color.argb(255, 255, 255, 255)
                )
            } else {
                mBinding.realtimeTabLayout.setTabTextColors(
                    Color.argb(255, 254, 165, 164),
                    Color.argb(255, 248, 76, 72)
                )
                mBinding.realtimeTabLayout.setSelectedTabIndicatorColor(
                    Color.argb(255, 248, 76, 72)
                )
            }
        }
    }

    // =========================
    // 刷新
    // =========================
    override fun onRefresh(refreshLayout: RefreshLayout) {
        ThreadUtils.postMainDelayed({
            mBinding.realtimeHomeSmartfresh.finishRefresh()
        }, 2000)
    }

    // =========================
    // 外部切换 Tab（优化版）
    // =========================
    override fun onChipClick(enum: HotListEnum) {
        val index = currentState.tabList
            .indexOfFirst { it.typeName == enum.name.lowercase() }

        if (index != -1) {
            mBinding.realtimeViewPager.setCurrentItem(index, true)
        }
    }
}