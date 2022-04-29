package com.knight.kotlin.module_square.activity

import android.text.TextUtils
import androidx.activity.viewModels
import com.alibaba.android.arouter.facade.annotation.Route
import com.knight.kotlin.library_aop.loginintercept.LoginCheck
import com.knight.kotlin.library_base.activity.BaseActivity
import com.knight.kotlin.library_base.event.MessageEvent
import com.knight.kotlin.library_base.ktx.observeLiveData
import com.knight.kotlin.library_base.ktx.setOnClick
import com.knight.kotlin.library_base.route.RouteActivity
import com.knight.kotlin.library_base.util.EventBusUtils
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.library_util.toast.ToastUtils
import com.knight.kotlin.module_square.R
import com.knight.kotlin.module_square.databinding.SquareShareArticleActivityBinding
import com.knight.kotlin.module_square.vm.SquareShareArticleVm
import dagger.hilt.android.AndroidEntryPoint

/**
 * Author:Knight
 * Time:2022/4/6 17:48
 * Description:SquareShareArticleActivity
 */
@AndroidEntryPoint
@Route(path = RouteActivity.Square.SquareShareArticleActivity)
class SquareShareArticleActivity :BaseActivity<SquareShareArticleActivityBinding,SquareShareArticleVm>() {

    private var title = ""
    private var link = ""
    override val mViewModel: SquareShareArticleVm by viewModels()
    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun SquareShareArticleActivityBinding.initView() {
        squareSharearticleToolbar.baseTvTitle.setText(R.string.square_sharearticle)
        squareSharearticleToolbar.baseIvBack.setOnClick { finish() }
        squareTvArticle.setOnClick {
            submitArticle()
        }



    }

    override fun initObserver() {
        observeLiveData(mViewModel.shareArticleSuccess,::successShareArticle)
    }

    override fun initRequestData() {

    }

    override fun reLoadData() {

    }

    private fun successShareArticle(successShare:Boolean){
        toast(R.string.square_share_success)
        EventBusUtils.postEvent(MessageEvent(MessageEvent.MessageType.ShareArticleSuccess))
        finish()
    }

    /**
     *
     * 校验文章信息和链接
     * @return
     */
    private fun validateArticleMessage():Boolean {
        var validFlag = true
        title = mBinding.squareSharearticleEt.text.toString().trim()
        link = mBinding.squareSharearticleLink.text.toString().trim()
        if (TextUtils.isEmpty(title)) {
            ToastUtils.show(R.string.square_title_noempty)
            validFlag = false
        } else if (TextUtils.isEmpty(link)) {
            ToastUtils.show(R.string.square_link_noempty)
            validFlag = false
        } else if (!link.startsWith("http://") && !link.startsWith("https://")) {
            ToastUtils.show(R.string.square_linkstart_rule)
            validFlag = false
        }
        return validFlag
    }


    @LoginCheck
    private fun submitArticle() {
        if (validateArticleMessage()) {
            mViewModel.shareArticle(title, link)
        }
    }
}