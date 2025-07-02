package com.knight.kotlin.module_home.dialog

import android.os.Bundle
import android.view.View
import com.knight.kotlin.library_base.fragment.BaseFragment
import com.core.library_base.route.RouteActivity
import com.core.library_base.vm.EmptyViewModel
import com.flyjingfish.android_aop_core.annotations.SingleClick
import com.knight.kotlin.library_database.entity.EveryDayPushEntity
import com.knight.kotlin.library_util.image.ImageLoader
import com.knight.kotlin.module_home.databinding.HomePushcardDialogBinding
import com.wyjson.router.GoRouter

/**
 * Author:Knight
 * Time:2022/2/10 14:39
 * Description:HomePushCardFragment
 */
class HomePushCardFragment: BaseFragment<HomePushcardDialogBinding, EmptyViewModel>() {


    private var mEveryDayPushEntity:EveryDayPushEntity?=null
    companion object {
        fun newInstance(mEveryDayPushEntity:EveryDayPushEntity):HomePushCardFragment {
            val mHomePushCardFragment:HomePushCardFragment = HomePushCardFragment()
            val args = Bundle()
            args.putParcelable("mEveryDayPushEntity",mEveryDayPushEntity)
            mHomePushCardFragment.arguments = args
            return mHomePushCardFragment
        }
    }

    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun HomePushcardDialogBinding.initView() {
        mEveryDayPushEntity = arguments?.getParcelable("mEveryDayPushEntity")
        mEveryDayPushEntity?.let {
            //设置图像
            ImageLoader.loadStringPhoto(requireActivity(),it.articlePicture,homeIvEverydayPushpicture)
            //设置作者
            homeTvArticleAuthor.text = it.author
            //标题
            homeTvArticleTitle.text = it.articleTitle
            //描述
            homeTvArticleDesc.text = it.articledesc
        }
        setOnClickListener(cvArticle)

    }

    override fun initObserver() {

    }

    override fun initRequestData() {

    }

    @SingleClick
    override fun onClick(v: View) {
        when(v) {
            mBinding.cvArticle -> {
                GoRouter.getInstance().build(RouteActivity.Web.WebPager)
                    .withString("webUrl",mEveryDayPushEntity?.articleLink)
                    .withString("webTitle",mEveryDayPushEntity?.articleTitle)
                    .go()
            }

        }
    }

    override fun reLoadData() {

    }

}