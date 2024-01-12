package com.knight.kotlin.module_home.dialog

import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.fragment.app.Fragment
import com.flyjingfish.android_aop_core.annotations.SingleClick
import com.knight.kotlin.library_base.fragment.BaseDialogFragment
import com.knight.kotlin.library_base.vm.EmptyViewModel
import com.knight.kotlin.library_database.entity.EveryDayPushEntity
import com.knight.kotlin.library_util.ViewInitUtils
import com.knight.kotlin.module_home.databinding.HomePusharticleDialogBinding
import com.knight.kotlin.module_home.view.CardTransformer
import com.knight.kotlin.module_home.view.UtilShowAnim

/**
 * Author:Knight
 * Time:2022/2/10 11:42
 * Description:HomePushArticleFragment
 */
class HomePushArticleFragment : BaseDialogFragment<HomePusharticleDialogBinding,EmptyViewModel>() {

    private var mEveryDayPushEntities: List<EveryDayPushEntity>? = null
    private val mPushCardFragments = mutableListOf<Fragment>()

    companion object {
        fun newInstance(mEveryDayPushEntityies:List<EveryDayPushEntity>) : HomePushArticleFragment {
            val mHomePushArticleFragment = HomePushArticleFragment()
            val args = Bundle()
            args.putParcelableArrayList("articles", ArrayList(mEveryDayPushEntityies))
            mHomePushArticleFragment.arguments = args
            return mHomePushArticleFragment
        }
    }

    override fun getGravity() = Gravity.CENTER

    override fun HomePusharticleDialogBinding.initView() {
        mEveryDayPushEntities = arguments?.getParcelableArrayList("articles")
        initViewPager()
        setOnClickListener(homePushArticleClose)
    }


    private fun initViewPager() {
        mPushCardFragments.clear()
        val mUtilAnim = UtilShowAnim(mBinding.homePushArticleVp)
        mEveryDayPushEntities?.let {
            for (everyDayPushEntity in it) {
                mPushCardFragments.add(HomePushCardFragment.newInstance(everyDayPushEntity))
            }
        }

        //实例化ViewPager切换动画类
        val mTransformer = CardTransformer()
        mBinding.homePushArticleVp.setPageTransformer(mTransformer)
        //设置切换动画为风车，并获取预加载数量
        val offscreen = mTransformer.setTransformerType(CardTransformer.ANIM_TYPE_WINDMILL)
        mBinding.homePushArticleVp.offscreenPageLimit = offscreen
        ViewInitUtils.setViewPager2Init(requireActivity(),mBinding.homePushArticleVp,mPushCardFragments,
            isOffscreenPageLimit = false,
            isUserInputEnabled = true
        )


    }


    override fun initObserver() {

    }

    override fun initRequestData() {

    }

    @SingleClick
    override fun onClick(v: View) {
        when(v) {
            mBinding.homePushArticleClose -> {
                dismiss()
            }
        }
    }

    override fun reLoadData() {

    }
}