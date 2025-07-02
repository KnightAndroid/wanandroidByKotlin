package com.knight.kotlin.module_home.activity

import android.view.KeyEvent
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.knight.kotlin.library_base.activity.BaseActivity
import com.core.library_base.route.RouteActivity
import com.core.library_base.util.dp2px
import com.core.library_base.vm.EmptyViewModel
import com.flyjingfish.android_aop_core.annotations.SingleClick
import com.knight.kotlin.library_util.BlurBuilderUtils
import com.knight.kotlin.library_util.ViewInitUtils
import com.knight.kotlin.library_widget.ktx.init
import com.knight.kotlin.library_widget.ktx.setSafeOnItemClickListener
import com.knight.kotlin.module_home.adapter.TopArticleAroundAdapter
import com.knight.kotlin.module_home.databinding.HomeArticlesTabActivityBinding
import com.knight.kotlin.module_home.entity.TopArticleBean
import com.knight.kotlin.module_home.fragment.HomeTopTabsFragment
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint

/**
 * Author:Knight
 * Time:2022/3/8 17:15
 * Description:HomeArticlesTabActivity
 */

@AndroidEntryPoint
@Route(path = RouteActivity.Home.HomeArticlesTabActivity)
class HomeArticlesTabActivity : BaseActivity<HomeArticlesTabActivityBinding, EmptyViewModel>() {

    private val mHomeTopTabsFragments = mutableListOf<Fragment>()

    private var datas = mutableListOf<TopArticleBean>()

    //圆圈适配器
    private val mTopArticleAroundAdapter: TopArticleAroundAdapter by lazy {
        TopArticleAroundAdapter()
    }

    private val mCompositePageTransformer: CompositePageTransformer = CompositePageTransformer()
    override fun HomeArticlesTabActivityBinding.initView() {
        setOnClickListener(homeIvCircleClose)
        intent.getParcelableArrayListExtra<TopArticleBean>("toparticles")?.let {
            datas = it
        }
        homeIv.setImageBitmap(BlurBuilderUtils.blur(homeIv))
        if (BlurBuilderUtils.isBlurFlag()) {
            homeIv.visibility = View.VISIBLE
        }
        for (topArticleEntity in datas) {
            mHomeTopTabsFragments.add(HomeTopTabsFragment.newInstance(topArticleEntity))
        }
        mTopArticleAroundAdapter.submitList(datas)
        mTopArticleAroundAdapter.run {
            setSafeOnItemClickListener { adapter, view, position ->
                setSelectItem(position)
                notifyDataSetChanged()
                homeArticleViewpager.setCurrentItem(position)
            }

        }

        homeArticleViewpager.init(mCompositePageTransformer, 30.dp2px(), 0.dp2px())
        homeRvToparticles.init(
            LinearLayoutManager(
                this@HomeArticlesTabActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            ), mTopArticleAroundAdapter, true
        )
        ViewInitUtils.setViewPager2Init(
            this@HomeArticlesTabActivity, homeArticleViewpager, mHomeTopTabsFragments,
            isOffscreenPageLimit = true,
            isUserInputEnabled = true
        )

        homeArticleViewpager.registerOnPageChangeCallback(object :
            OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                mTopArticleAroundAdapter.setSelectItem(position)
                mTopArticleAroundAdapter.notifyDataSetChanged()
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
            }
        })
    }

    override fun initObserver() {

    }

    override fun initRequestData() {

    }

    //安卓重写返回键事件
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish()
            overridePendingTransition(com.core.library_base.R.anim.base_scalealpha_out, com.core.library_base.R.anim.base_scalealpha_slient)
        }
        return true
    }

    @SingleClick
    override fun onClick(v: View) {
        when (v) {
            mBinding.homeIvCircleClose ->{
                finish()
                overridePendingTransition(com.core.library_base.R.anim.base_scalealpha_out, com.core.library_base.R.anim.base_scalealpha_slient)
            }
        }
    }


    override fun finish() {
        super.finish()
        BlurBuilderUtils.recycle()
    }

    override fun reLoadData() {

    }

    override fun setThemeColor(isDarkMode: Boolean) {

    }
}