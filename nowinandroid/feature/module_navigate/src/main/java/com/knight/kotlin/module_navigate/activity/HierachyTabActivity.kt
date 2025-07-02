package com.knight.kotlin.module_navigate.activity

import androidx.fragment.app.Fragment
import com.knight.kotlin.library_base.activity.BaseActivity
import com.core.library_base.ktx.setOnClick
import com.core.library_base.route.RouteActivity
import com.core.library_base.vm.EmptyViewModel
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
class HierachyTabActivity : BaseActivity<NavigateHierachyTabActivityBinding, EmptyViewModel>() {


    @JvmField
    @Param(name = "titleName")
    var titleName:String = ""


    @JvmField
    @Param(name = "cids")
    var cids:ArrayList<Int>?=null

    @JvmField
    @Param(name = "childrenNames")
    var childrenNames:ArrayList<String>?=null


    var hierachyTabFragments: MutableList<Fragment> = mutableListOf()

    //顶部导航栏
    var titleDatas: MutableList<String> = mutableListOf()


    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun NavigateHierachyTabActivityBinding.initView() {
        title = titleName
        includeTabarticleToolbar.baseIvBack.setOnClick { finish() }
        hierachyTabFragments.clear()
        titleDatas.clear()
        childrenNames?.let {
            for (i in it.indices) {
                hierachyTabFragments.add(HierachyTabArticleFragment.newInstance(cids?.get(i) ?: 0))
                titleDatas.add(it[i])
            }
        }
        ViewInitUtils.setViewPager2Init(this@HierachyTabActivity,hierachyViewPager,hierachyTabFragments,
            isOffscreenPageLimit = true,
            isUserInputEnabled = false
        )
        hierachyIndicator.bindWechatViewPager2(hierachyViewPager,titleDatas)




    }

    override fun initObserver() {

    }

    override fun initRequestData() {

    }

    override fun reLoadData() {

    }
}