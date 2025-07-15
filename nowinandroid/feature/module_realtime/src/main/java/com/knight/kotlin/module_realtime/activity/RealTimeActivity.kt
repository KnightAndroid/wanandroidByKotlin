package com.knight.kotlin.module_realtime.activity

import android.animation.ArgbEvaluator
import android.graphics.Color
import androidx.fragment.app.Fragment
import com.core.library_base.annotation.EventBusRegister
import com.core.library_base.event.MessageEvent
import com.core.library_base.ktx.setOnClick
import com.core.library_base.route.RouteActivity
import com.core.library_base.vm.EmptyViewModel
import com.core.library_common.util.dp2px
import com.knight.kotlin.library_base.activity.BaseActivity
import com.knight.kotlin.library_base.config.EventBusKeys
import com.knight.kotlin.library_base.ktx.statusHeight
import com.knight.kotlin.library_common.util.CacheUtils
import com.knight.kotlin.module_realtime.databinding.RealtimeMainActivityBinding
import com.knight.kotlin.module_realtime.dialog.RealTimeRankRuleFragment
import com.knight.kotlin.module_realtime.fragment.RealTimeHomeFragment
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import kotlin.math.abs

@EventBusRegister
@AndroidEntryPoint
@Route(path = RouteActivity.RealTime.RealTimeMainActivity)
class RealTimeActivity : BaseActivity<RealtimeMainActivityBinding, EmptyViewModel>() {
    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun initObserver() {

    }

    override fun initRequestData() {

    }

    override fun reLoadData() {

    }

    override fun RealtimeMainActivityBinding.initView() {
        ivRealtimeHomeBack.setOnClickListener { finish() }
        imgMore.setOnClick {
            RealTimeRankRuleFragment().show(supportFragmentManager,"dialog_rank_rule")
        }
        mBinding.layoutParentTabSegment.apply {
            layoutParams.apply {
                setPadding(0, statusHeight, 0, 0)


            }
        }
        val fm = supportFragmentManager
        val ft = fm.beginTransaction()

        val fragment: Fragment =  RealTimeHomeFragment()
        if (fragment.isAdded) {
            ft.show(fragment)
        } else {
            ft.add(fragmentContainerView.id, fragment)
        }
        ft.commitNowAllowingStateLoss()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: MessageEvent) {
        when (event.type) {
             MessageEvent.MessageType.AppBarOffsetChanged -> {
                 val mHeight: Float = 320.dp2px().toFloat()
                 val mOffsetY = abs(event.getInt(EventBusKeys.OFFSET)).toFloat()
                 val scale: Float = if (mOffsetY / mHeight > 1) 1.toFloat() else mOffsetY / mHeight
                 val alpha: Int =(scale * 255).toInt()
                 if (CacheUtils.getNormalDark()) {
                     mBinding.layoutParentTabSegment.setBackgroundColor(Color.argb(alpha, 186,5,2))
                 } else {
                     mBinding.layoutParentTabSegment.setBackgroundColor(Color.argb(alpha, 254, 79, 76))
                 }
                 if (event.getInt(EventBusKeys.REALTIMESCROLLORIENTATION) == 0) {
                     val color = ArgbEvaluator().evaluate(scale, Color.argb(255, 255, 255, 255), Color.argb(255, 34, 22, 19)) as Int
                     mBinding.imgMore.setColorFilter(color)
                 } else {
                     mBinding.tvRealtimeHomeTitle.alpha = alpha.toFloat()
                 }
             }
              else ->{

              }
        }
    }
}