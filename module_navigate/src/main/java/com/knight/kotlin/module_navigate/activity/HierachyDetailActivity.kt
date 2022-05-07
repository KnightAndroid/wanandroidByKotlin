package com.knight.kotlin.module_navigate.activity

import androidx.activity.viewModels
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.knight.kotlin.library_base.activity.BaseActivity
import com.knight.kotlin.library_base.ktx.setOnClick
import com.knight.kotlin.library_base.route.RouteActivity
import com.knight.kotlin.library_base.vm.EmptyViewModel
import com.knight.kotlin.module_navigate.R
import com.knight.kotlin.module_navigate.databinding.NavigateHierachyDetailActivityBinding
import com.knight.kotlin.module_navigate.fragment.HierachyTabArticleFragment
import dagger.hilt.android.AndroidEntryPoint

/**
 * Author:Knight
 * Time:2022/5/7 11:06
 * Description:HierachyDetailActivity
 */
@AndroidEntryPoint
@Route(path = RouteActivity.Navigate.HierachyDetailActivity)
class HierachyDetailActivity : BaseActivity<NavigateHierachyDetailActivityBinding,EmptyViewModel>(){
    override val mViewModel: EmptyViewModel by viewModels()

    @JvmField
    @Autowired(name = "cid")
    var cid = 0

    private lateinit var mHierachyTabArticleFragment: HierachyTabArticleFragment

    @JvmField
    @Autowired(name = "titleName")
    var titleName: String = ""

    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun NavigateHierachyDetailActivityBinding.initView() {
        includeHierachyToolbar.baseTvTitle.setText(titleName)
        includeHierachyToolbar.baseIvBack.setOnClick { finish() }
        createFragment()

    }

    override fun initObserver() {

    }

    override fun initRequestData() {

    }

    override fun reLoadData() {

    }

    fun createFragment() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        mHierachyTabArticleFragment = HierachyTabArticleFragment.newInstance(cid)
        fragmentTransaction.add(R.id.hierachy_detail_fr, mHierachyTabArticleFragment)
        fragmentTransaction.commit()
    }
}