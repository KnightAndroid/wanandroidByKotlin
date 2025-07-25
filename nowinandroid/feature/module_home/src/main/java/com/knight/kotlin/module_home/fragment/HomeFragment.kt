package com.knight.kotlin.module_home.fragment

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.core.library_base.event.MessageEvent
import com.knight.kotlin.library_base.fragment.BaseFragment
import com.core.library_base.route.RouteFragment
import com.core.library_base.util.EventBusUtils
import com.core.library_base.vm.EmptyViewModel
import com.knight.kotlin.library_util.ViewInitUtils
import com.knight.kotlin.module_home.databinding.HomeFragmentBinding
import com.knight.kotlin.module_home.view.TwoLevelTransformer
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint


/**
 * Author:Knight
 * Time:2021/12/22 19:33
 * Description:HomeFragment
 */

@AndroidEntryPoint
@Route(path = RouteFragment.Home.HomeFragment)
class HomeFragment : BaseFragment<HomeFragmentBinding, EmptyViewModel>(),HomeRecommendFragment.SlideMenuOpenListener {


    /**
     * 首页Fragment
     */
    private var mFragments = mutableListOf<Fragment>()
    private val mHomeRecommendFragment by lazy { HomeRecommendFragment() }
    private val mHomeClassifyFragment by lazy { HomeEyeClassifyFragment() }
    private var mSlideMenuOpen :Boolean = false

    override fun HomeFragmentBinding.initView() {
        initMagicIndicator()
        mHomeRecommendFragment.mSlideMenuOpenListener = this@HomeFragment
//        viewPager.getChildAt(0)?.setOnTouchListener { _, event ->
//            if (mSlideMenuOpen) {
//                true  // 禁止滑动
//            } else {
//                false // 允许滑动
//            }
//        }
    }

    /**
     *
     * 订阅LiveData
     */
    override fun initObserver() {
    }

    override fun initRequestData() {

    }

    override fun setThemeColor(isDarkMode: Boolean) {

    }







    /**
     * 初始化指示器
     */
    private fun initMagicIndicator() {
        mFragments.add(mHomeRecommendFragment)
        mFragments.add(mHomeClassifyFragment)


        ViewInitUtils.setViewPager2Init(requireActivity(),mBinding.viewPager,mFragments,
            isOffscreenPageLimit = false,
            isUserInputEnabled = true
        )
        initViewPagerListener()



    }


    private fun initViewPagerListener() {
        val transformer = TwoLevelTransformer(mBinding.viewPager, mBinding.headerView)
        val lp = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        mBinding.viewPager.setRotation(180f)
        mBinding.viewPager.setOrientation(ViewPager2.ORIENTATION_VERTICAL)
        mBinding.viewPager.setPageTransformer(transformer)
        mBinding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                if (state == ViewPager2.SCROLL_STATE_IDLE) {
                    if (mBinding.viewPager.getCurrentItem() == 0) {
                        transformer.setFromFloorPage(true)
                        EventBusUtils.postEvent(
                            MessageEvent(MessageEvent.MessageType.OpenEyepetizer).put(
                                false
                            )
                        )
                    } else if (mBinding.viewPager.getCurrentItem() == 1) {
                        EventBusUtils.postEvent(
                            MessageEvent(MessageEvent.MessageType.OpenEyepetizer).put(
                                true
                            )
                        )
                    }
                }
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                //小程序页面出现：偏移值有0->1 偏移像素有0->页面高度
                //小程序页面消失：偏移值->0 偏移像素页面高度->0
                if (position == 1) {
                    transformer.setFromFloorPage(false)

                }
                lp.height = positionOffsetPixels
                mBinding.headerView.setLayoutParams(lp)
                mBinding.headerView.setPercent(positionOffset)
            }
        })



    }


    override fun reLoadData() {

    }

    override fun onOpenStatus(open: Boolean) {
        mSlideMenuOpen = open
        mBinding.viewPager.isUserInputEnabled = !mSlideMenuOpen
    }

}