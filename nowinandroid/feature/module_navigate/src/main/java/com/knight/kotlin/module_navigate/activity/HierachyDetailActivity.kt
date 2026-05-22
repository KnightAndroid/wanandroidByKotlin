package com.knight.kotlin.module_navigate.activity

import com.core.library_base.contact.EmptyContract
import com.core.library_base.ktx.setOnClick
import com.core.library_base.route.RouteActivity
import com.core.library_base.vm.EmptyMviViewModel
import com.knight.kotlin.library_base.activity.BaseMviActivity
import com.knight.kotlin.module_navigate.R
import com.knight.kotlin.module_navigate.databinding.NavigateHierachyDetailActivityBinding
import com.knight.kotlin.module_navigate.fragment.HierachyTabArticleFragment
import com.wyjson.router.annotation.Param
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint

/**
 * Author:Knight
 * Time:2022/5/7 11:06
 * Description:HierachyDetailActivity
 */
@AndroidEntryPoint
@Route(path = RouteActivity.Navigate.HierachyDetailActivity)
class HierachyDetailActivity :
    BaseMviActivity<
            NavigateHierachyDetailActivityBinding,
            EmptyMviViewModel,
            EmptyContract.Event,
            EmptyContract.State,
            EmptyContract.Effect>() {

    @JvmField
    @Param(name = "cid")
    var cid = 0

    @JvmField
    @Param(name = "titleName")
    var titleName: String = ""

    private lateinit var mHierachyTabArticleFragment: HierachyTabArticleFragment

    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun NavigateHierachyDetailActivityBinding.initView() {

        includeHierachyToolbar.title = titleName

        includeHierachyToolbar.baseIvBack.setOnClick {
            finish()
        }

        createFragment()
    }

    override fun initObserver() {

    }

    override fun initEvent() {

    }

    override fun renderState(state: EmptyContract.State) {

    }

    override fun handleEffect(effect: EmptyContract.Effect) {

    }

    override fun initRequestData() {

    }

    override fun reLoadData() {

    }

    /**
     * 创建 Fragment
     */
    private fun createFragment() {

        mHierachyTabArticleFragment =
            HierachyTabArticleFragment.newInstance(cid)

        supportFragmentManager.beginTransaction()
            .add(
                R.id.hierachy_detail_fr,
                mHierachyTabArticleFragment
            )
            .commit()
    }
}