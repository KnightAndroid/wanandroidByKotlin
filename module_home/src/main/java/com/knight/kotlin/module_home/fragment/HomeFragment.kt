package com.knight.kotlin.module_home.fragment

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.knight.kotlin.library_base.event.MessageEvent
import com.knight.kotlin.library_base.fragment.BaseFragment
import com.knight.kotlin.library_base.ktx.observeLiveData
import com.knight.kotlin.library_base.route.RouteFragment
import com.knight.kotlin.library_base.util.EventBusUtils
import com.knight.kotlin.library_common.entity.AppUpdateBean
import com.knight.kotlin.library_common.fragment.UpdateAppDialogFragment
import com.knight.kotlin.library_util.SystemUtils
import com.knight.kotlin.library_util.ViewInitUtils
import com.knight.kotlin.module_home.databinding.HomeFragmentBinding
import com.knight.kotlin.module_home.view.TwoLevelTransformer
import com.knight.kotlin.module_home.vm.HomeVm
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint


/**
 * Author:Knight
 * Time:2021/12/22 19:33
 * Description:HomeFragment
 */

@AndroidEntryPoint
@Route(path = RouteFragment.Home.HomeFragment)
class HomeFragment : BaseFragment<HomeFragmentBinding, HomeVm>() {


    /**
     * 首页Fragment
     */
    private var mFragments = mutableListOf<Fragment>()



    override fun HomeFragmentBinding.initView() {
        initMagicIndicator()

        //,homeIvLabelmore


    }

    /**
     *
     * 订阅LiveData
     */
    override fun initObserver() {
        observeLiveData(mViewModel.appUpdateMessage,::checkAppMessage)
    }

    override fun initRequestData() {
        mViewModel.checkAppUpdateMessage()
    }

    override fun setThemeColor(isDarkMode: Boolean) {

    }



    /**
     *
     * 检查APP更新
     */
    private fun checkAppMessage(data: AppUpdateBean) {
        //如果本地安装包大于远端 证明本地安装的说测试包 无需更新
        if (SystemUtils.getAppVersionCode(requireActivity())  < data.versionCode ) {
            if (data.versionName != activity?.let { SystemUtils.getAppVersionName(it) }) {
                UpdateAppDialogFragment.newInstance(data).showAllowingStateLoss(
                    parentFragmentManager, "dialog_update")

            }
        }
    }



    /**
     * 初始化指示器
     */
    private fun initMagicIndicator() {
//        knowledgeLabelList = CacheUtils.getDataInfo(
//            "knowledgeLabel",
//            object : TypeToken<List<String>>() {}.type
//        )
//        if (knowledgeLabelList.isNullOrEmpty()) {
//            val type: Type = object : TypeToken<List<String>>() {}.type
//            val jsonData = getJson(requireActivity(), "searchkeywords.json")
//            knowledgeLabelList = GsonUtils.getList(jsonData, type)
//        }
//        mFragments.clear()
//        for (i in knowledgeLabelList.indices) {
//            if (i == 0) {
//                mFragments.add(HomeRecommendFragment())
//            } else {
//                mFragments.add(HomeArticleFragment())
//            }
//        }

        mFragments.add(HomeRecommendFragment())
        mFragments.add(HomeClassifyFragment())
        ViewInitUtils.setViewPager2Init(requireActivity(),mBinding.viewPager,mFragments,
            isOffscreenPageLimit = false,
            isUserInputEnabled = true
        )

//        mBinding.magicIndicator.bindViewPager2(mBinding.viewPager,knowledgeLabelList) {
//            HomeConstants.ARTICLE_TYPE = knowledgeLabelList[it]
//        }
        initViewPagerListener()



    }


    private fun initViewPagerListener() {
        val transformer = TwoLevelTransformer(mBinding.viewPager, mBinding.headerView)
        val lp = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        mBinding.viewPager.setRotation(180f)
        mBinding.viewPager.setOverScrollMode(View.OVER_SCROLL_NEVER)
        mBinding.viewPager.setOrientation(ViewPager2.ORIENTATION_VERTICAL)
        mBinding.viewPager.setPageTransformer(transformer)
        mBinding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                if (state == ViewPager2.SCROLL_STATE_IDLE) {
                    if (mBinding.viewPager.getCurrentItem() == 1) {
                        EventBusUtils.postEvent(MessageEvent(MessageEvent.MessageType.ChangeBottomNavigate).put(true))
                    } else if (mBinding.viewPager.getCurrentItem() == 0) {

                        transformer.setFromFloorPage(true)
                        EventBusUtils.postEvent(MessageEvent(MessageEvent.MessageType.ChangeBottomNavigate).put(false))
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

}