package com.knight.kotlin.module_navigate.fragment

import androidx.fragment.app.Fragment
import com.core.library_base.contact.EmptyContract
import com.core.library_base.route.RouteFragment
import com.core.library_base.vm.EmptyMviViewModel
import com.knight.kotlin.library_base.fragment.BaseMviFragment
import com.knight.kotlin.library_util.ViewInitUtils
import com.knight.kotlin.library_util.bindWechatViewPager2
import com.knight.kotlin.module_navigate.R
import com.knight.kotlin.module_navigate.databinding.NavigateHomeFragmentBinding
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint

/**
 * Author:Knight
 * Time:2021/12/23 17:28
 * Description:NavigateHomeFragment
 */
@AndroidEntryPoint
@Route(path = RouteFragment.Navigate.NavigateHomeFragment)
class NavigateHomeFragment :
    BaseMviFragment<
            NavigateHomeFragmentBinding,
            EmptyMviViewModel,
            EmptyContract.Event,
            EmptyContract.State,
            EmptyContract.Effect>() {

    /**
     * Fragment 集合
     */
    private val mFragments: MutableList<Fragment> =
        mutableListOf()

    /**
     * tab 标题
     */
    private val mTitleList: MutableList<String> =
        mutableListOf()

    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun NavigateHomeFragmentBinding.initView() {

        initViewPager()
    }

    override fun initObserver() {

    }

    override fun initRequestData() {

    }

    override fun reLoadData() {

    }

    override fun renderState(state: EmptyContract.State) {

    }

    override fun handleEffect(effect: EmptyContract.Effect) {

    }

    /**
     * 初始化 ViewPager
     */
    private fun initViewPager() {

        mTitleList.apply {

            clear()

            add(
                getString(
                    R.string.navigate_hierachy_tab_navigate
                )
            )

            add(
                getString(
                    R.string.navigate_hierachy_tab_hierachy
                )
            )
        }

        mFragments.apply {

            clear()

            add(NavigateFragment())

            add(HierachyFragment())
        }

        ViewInitUtils.setViewPager2Init(
            requireActivity(),
            mBinding.hierachyNavigateViewpager,
            mFragments,
            isOffscreenPageLimit = true,
            isUserInputEnabled = false
        )

        mBinding.hierachyNavigateIndicator
            .bindWechatViewPager2(
                mBinding.hierachyNavigateViewpager,
                mTitleList
            )
    }
}