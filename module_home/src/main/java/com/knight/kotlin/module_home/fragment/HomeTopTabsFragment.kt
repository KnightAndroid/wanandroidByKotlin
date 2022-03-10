package com.knight.kotlin.module_home.fragment

import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.fragment.app.viewModels
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.knight.kotlin.library_aop.clickintercept.SingleClick
import com.knight.kotlin.library_base.config.Appconfig
import com.knight.kotlin.library_base.fragment.BaseFragment
import com.knight.kotlin.library_base.route.RouteActivity
import com.knight.kotlin.library_base.route.RouteFragment
import com.knight.kotlin.library_base.util.CacheUtils
import com.knight.kotlin.library_base.util.ColorUtils
import com.knight.kotlin.library_base.util.dp2px
import com.knight.kotlin.library_base.vm.EmptyViewModel
import com.knight.kotlin.library_widget.pagetransformer.DragLayout
import com.knight.kotlin.module_home.databinding.HomeToptabsFragmentBinding
import com.knight.kotlin.module_home.entity.TopArticleBean
import dagger.hilt.android.AndroidEntryPoint

/**
 * Author:Knight
 * Time:2022/3/8 10:51
 * Description:HomeTopTabsFragment
 */
@AndroidEntryPoint
@Route(path = RouteFragment.Home.HomeTopArticleFragment)
class HomeTopTabsFragment : BaseFragment<HomeToptabsFragmentBinding, EmptyViewModel>(),
    DragLayout.GotoDetailListener {
    override val mViewModel: EmptyViewModel by viewModels()

    private lateinit var mTopArticleEntity: TopArticleBean
    private var cardBgColor: Int = 0

    companion object {
        fun newInstance(topArticleEntity: TopArticleBean): HomeTopTabsFragment {
            val projectViewpagerFragment = HomeTopTabsFragment()
            val args = Bundle()
            args.putParcelable("topdata", topArticleEntity)
            projectViewpagerFragment.arguments = args
            return projectViewpagerFragment
        }
    }


    override fun setThemeColor(isDarkMode: Boolean) {
        val gradientChapterNameDrawable = GradientDrawable()
        gradientChapterNameDrawable.shape = GradientDrawable.RECTANGLE
        gradientChapterNameDrawable.setStroke(1.dp2px(), CacheUtils.getThemeColor())

        val gradientAuthorDrawable = GradientDrawable()
        gradientAuthorDrawable.shape = GradientDrawable.RECTANGLE
        gradientAuthorDrawable.setStroke(1.dp2px(), CacheUtils.getThemeColor())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mBinding.homeToparticleSuperchaptername.background = gradientChapterNameDrawable
            mBinding.homeToparticleAuthor.background = gradientAuthorDrawable
        } else {
            mBinding.homeToparticleSuperchaptername.setBackgroundDrawable(
                gradientChapterNameDrawable
            )
            mBinding.homeToparticleAuthor.setBackgroundDrawable(gradientAuthorDrawable)
        }

        themeColor?.let {
            mBinding.homeToparticleAuthor.setTextColor(it)
            mBinding.homeToparticleSuperchaptername.setTextColor(it)
        }


    }

    override fun HomeToptabsFragmentBinding.initView() {
        arguments?.getParcelable<TopArticleBean>("topdata")?.let {
            mTopArticleEntity = it
        }
        mBinding.homeToparticleDrawlayout.setGotoDetailListener(this@HomeTopTabsFragment)
        val gradientDrawable = GradientDrawable();
        cardBgColor = ColorUtils.getRandColorCode()
        gradientDrawable.setColor(cardBgColor)
        mBinding.homeToparticleIv.background = gradientDrawable
        initData()
    }


    fun initData() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            with(mTopArticleEntity) {
                mBinding.homeTvToparticleTitle.text =
                    Html.fromHtml(title, Html.FROM_HTML_MODE_LEGACY)
                mBinding.homeToparticleDesc.text = Html.fromHtml(desc, Html.FROM_HTML_MODE_LEGACY)
            }
        } else {
            mBinding.homeTvToparticleTitle.setText(Html.fromHtml(mTopArticleEntity.title))
            mBinding.homeToparticleDesc.setText(Html.fromHtml(mTopArticleEntity.desc))
        }

        if (mTopArticleEntity.desc.isNullOrEmpty()) {
             mBinding.homeToparticleDesc.visibility = View.GONE
        } else {
             mBinding.homeToparticleDesc.visibility = View.VISIBLE
        }

        with(mTopArticleEntity) {
            mBinding.homeToparticleSuperchaptername.setText(superChapterName)
            mBinding.homeToparticleAuthor.setText(author)
            mBinding.homeToparticleDate.setText(niceDate)
        }

    }

    override fun initObserver() {

    }

    override fun initRequestData() {

    }

    @SingleClick
    override fun gotoDetail() {
        //跳转
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
            requireActivity(),
            Pair<View, String>(mBinding.homeToparticleIv, Appconfig.IMAGE_TRANSITION_NAME),
            Pair<View, String>(mBinding.homeToparticleAuthor, Appconfig.TEXT_AUTHOR_NAME),
            Pair<View, String>(
                mBinding.homeToparticleSuperchaptername,
                Appconfig.TEXT_CHAPTERNAME_NAME
            )

        )
        ARouter.getInstance().build(RouteActivity.Web.WebTransitionPager)
            .withInt("cardBgColor",cardBgColor)
            .withString("webUrl",mTopArticleEntity.link)
            .withString("title",mTopArticleEntity.title)
            .withString("author",mTopArticleEntity.author)
            .withString("chapterName",mTopArticleEntity.chapterName)
            .withOptionsCompat(options)
            .navigation(getActivity());
    }
}