package com.knight.kotlin.module_realtime.activity

import com.core.library_base.annotation.EventBusRegister
import com.core.library_base.event.MessageEvent
import com.core.library_base.ktx.setOnClick
import com.core.library_base.route.RouteActivity
import com.knight.kotlin.library_base.activity.BaseMviActivity
import com.knight.kotlin.library_base.config.EventBusKeys
import com.knight.kotlin.library_base.ktx.statusHeight
import com.knight.kotlin.module_realtime.contract.RealTimeContract
import com.knight.kotlin.module_realtime.databinding.RealtimeMainActivityBinding
import com.knight.kotlin.module_realtime.dialog.RealTimeRankRuleFragment
import com.knight.kotlin.module_realtime.fragment.RealTimeHomeFragment
import com.knight.kotlin.module_realtime.vm.RealTimeViewModel
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

@EventBusRegister
@AndroidEntryPoint
@Route(path = RouteActivity.RealTime.RealTimeMainActivity)
class RealTimeActivity : BaseMviActivity<
        RealtimeMainActivityBinding,
        RealTimeViewModel,
        RealTimeContract.Event,
        RealTimeContract.State,
        RealTimeContract.Effect>() {

    override fun setThemeColor(isDarkMode: Boolean) {}

    override fun initEvent() {
        // 如果有初始化事件，这里发
    }

    override fun renderState(state: RealTimeContract.State) {
        // ✅ 唯一UI更新入口

        mBinding.layoutParentTabSegment.setBackgroundColor(state.backgroundColor)

        mBinding.imgMore.setColorFilter(state.iconColor)

        mBinding.tvRealtimeHomeTitle.alpha = state.titleAlpha
    }

    override fun handleEffect(effect: RealTimeContract.Effect) {
        // 当前页面没有Effect
    }

    override fun RealtimeMainActivityBinding.initView() {
        ivRealtimeHomeBack.setOnClickListener { finish() }

        imgMore.setOnClick {
            RealTimeRankRuleFragment().show(supportFragmentManager, "dialog_rank_rule")
        }

        layoutParentTabSegment.setPadding(0, statusHeight, 0, 0)

        val fragment = RealTimeHomeFragment()
        supportFragmentManager.beginTransaction()
            .replace(fragmentContainerView.id, fragment)
            .commitNowAllowingStateLoss()
    }

    override fun initObserver() {

    }

    override fun initRequestData() {}

    override fun reLoadData() {}

    /**
     * 关键点：EventBus → 转 Event，不直接操作UI
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: MessageEvent) {
        when (event.type) {
            MessageEvent.MessageType.AppBarOffsetChanged -> {
                mViewModel.setEvent(
                    RealTimeContract.Event.OnAppBarOffsetChanged(
                        offset = event.getInt(EventBusKeys.OFFSET),
                        orientation = event.getInt(EventBusKeys.REALTIMESCROLLORIENTATION)
                    )
                )
            }
            else->{}
        }
    }
}