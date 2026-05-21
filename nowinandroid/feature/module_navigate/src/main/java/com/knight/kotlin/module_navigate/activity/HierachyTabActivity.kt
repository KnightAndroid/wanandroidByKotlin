package com.knight.kotlin.module_navigate.activity

import androidx.fragment.app.Fragment
import com.core.library_base.contact.EmptyContract
import com.core.library_base.ktx.setOnClick
import com.core.library_base.route.RouteActivity
import com.core.library_base.vm.EmptyMviViewModel
import com.knight.kotlin.library_base.activity.BaseMviActivity
import com.knight.kotlin.library_util.ViewInitUtils
import com.knight.kotlin.library_util.bindWechatViewPager2
import com.knight.kotlin.module_navigate.databinding.NavigateHierachyTabActivityBinding
import com.knight.kotlin.module_navigate.fragment.HierachyTabArticleFragment
import com.wyjson.router.annotation.Param
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint

/**
 * Author:Knight
 * Time:2022/5/6 15:19
 * Description:HierachyTabActivity
 */
@AndroidEntryPoint
@Route(path = RouteActivity.Navigate.HierachyTabActivity)
class HierachyTabActivity :
    BaseMviActivity<
            NavigateHierachyTabActivityBinding,
            EmptyMviViewModel,
            EmptyContract.Event,
            EmptyContract.State,
            EmptyContract.Effect>() {

    @JvmField
    @Param(name = "titleName")
    var titleName: String = ""

    @JvmField
    @Param(name = "cids")
    var cids: ArrayList<Int>? = null

    @JvmField
    @Param(name = "childrenNames")
    var childrenNames: ArrayList<String>? = null

    /**
     * Fragment集合
     */
    private val hierachyTabFragments by lazy(LazyThreadSafetyMode.NONE) {
        buildFragments()
    }

    /**
     * Tab标题
     */
    private val titleDatas by lazy(LazyThreadSafetyMode.NONE) {
        childrenNames ?: arrayListOf()
    }

    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun NavigateHierachyTabActivityBinding.initView() {

        initToolbar()

        initViewPager()

        initTab()
    }

    /**
     * 初始化Toolbar
     */
    private fun NavigateHierachyTabActivityBinding.initToolbar() {
        title = titleName
        includeTabarticleToolbar.apply {



            baseIvBack.setOnClick {
                finish()
            }
        }
    }

    /**
     * 初始化ViewPager
     */
    private fun NavigateHierachyTabActivityBinding.initViewPager() {

        if (hierachyTabFragments.isEmpty()) {
            return
        }

        ViewInitUtils.setViewPager2Init(
            activity = this@HierachyTabActivity,
            viewPager2 = hierachyViewPager,
            fragments = hierachyTabFragments,
            isOffscreenPageLimit = true,
            isUserInputEnabled = false
        )
    }

    /**
     * 初始化Tab
     */
    private fun NavigateHierachyTabActivityBinding.initTab() {

        if (titleDatas.isEmpty()) {
            return
        }

        hierachyIndicator.bindWechatViewPager2(
            hierachyViewPager,
            titleDatas
        )
    }

    /**
     * 构建Fragment
     */
    private fun buildFragments(): MutableList<Fragment> {

        val fragments = mutableListOf<Fragment>()

        val cidList = cids
        val nameList = childrenNames

        if (cidList.isNullOrEmpty() || nameList.isNullOrEmpty()) {
            return fragments
        }

        val size = minOf(cidList.size, nameList.size)

        repeat(size) { index ->

            fragments.add(
                HierachyTabArticleFragment.newInstance(
                    cidList[index]
                )
            )
        }

        return fragments
    }

    override fun initObserver() {

    }

    override fun initRequestData() {

    }

    /**
     * 渲染State
     */
    override fun renderState(state: EmptyContract.State) {

    }

    /**
     * 处理副作用
     */
    override fun handleEffect(effect: EmptyContract.Effect) {

    }

    override fun reLoadData() {

    }
}