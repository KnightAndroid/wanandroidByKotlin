package com.knight.kotlin.module_web.dialog

import android.content.Intent
import android.net.Uri
import android.view.Gravity
import android.view.View
import com.core.library_base.event.MessageEvent
import com.core.library_base.fragment.BaseDialogFragment
import com.core.library_base.util.EventBusUtils
import com.flyjingfish.android_aop_core.annotations.SingleClick
import com.knight.kotlin.library_aop.loginintercept.LoginCheck
import com.knight.kotlin.library_util.SystemUtils
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.library_util.toast.ToastUtils
import com.knight.kotlin.module_web.R
import com.knight.kotlin.module_web.databinding.WebArticleBottomDialogBinding
import com.knight.kotlin.module_web.vm.WebVm
import dagger.hilt.android.AndroidEntryPoint

/**
 * Author:Knight
 * Time:2022/1/13 10:29
 * Description:WebArticleBottomFragment
 */

@AndroidEntryPoint
class WebArticleBottomFragment constructor(
    articleUrl: String,
    articleTitle: String,
    articleId: Int,
    collect: Boolean
) : BaseDialogFragment<WebArticleBottomDialogBinding, WebVm>() {



    /**
     * 文章url
     */
    private var articleUrl: String? = null

    /**
     * 文章标题
     */
    private var articleTitle: String? = null

    /**
     *
     * 文章id
     */
    private var articleId: Int = 0


    /**
     *
     * 是否收藏
     */
    private var collect: Boolean? = false

    init {
        this.articleUrl = articleUrl
        this.articleTitle = articleTitle
        this.articleId = articleId
        this.collect = collect
    }
    companion object {
        fun newInstance(
            url: String,
            title: String,
            articleId: Int,
            collect: Boolean
        ): WebArticleBottomFragment {
            return WebArticleBottomFragment(url, title, articleId, collect)
        }
    }



    override fun getGravity() = Gravity.BOTTOM
    override fun cancelOnTouchOutSide(): Boolean {
        return true
    }

    override fun WebArticleBottomDialogBinding.initView() {
        webTvCollectArticle.visibility = if (collect == true) View.GONE else View.VISIBLE
        setOnClickListener(webTvCopyUrl, webTvShareArticle, webTvCollectArticle, webTvOpenBrowser)

    }

    @SingleClick
    override fun onClick(v: View) {
        when (v) {
            mBinding.webTvCollectArticle -> {
                collectArticle()
            }

            mBinding.webTvCopyUrl -> {
                activity?.let { it -> SystemUtils.copyContent(it, articleUrl) }
                toast(R.string.web_success_copyurl)
                dismiss()
            }
            mBinding.webTvOpenBrowser -> {
                val intent = Intent("android.intent.action.VIEW")
                intent.data = Uri.parse(articleUrl)
                startActivity(intent)
                dismiss()
            }
            mBinding.webTvShareArticle -> {
                if (!articleUrl.isNullOrEmpty() && (articleUrl?.startsWith("http") == true || articleUrl?.startsWith(
                        "http"
                    ) == true)
                ) {
                    val intent = Intent(Intent.ACTION_SEND).putExtra(
                        Intent.EXTRA_TEXT,
                        getString(
                            R.string.web_share_article_url,
                            getString(com.knight.kotlin.library_base.R.string.base_app_name),
                            articleTitle,
                            articleUrl
                        )
                    )
                    intent.setType("text/plain")
                    startActivity(Intent.createChooser(intent, getString(R.string.web_share_article)))
                    dismiss()
                }
            }
        }
    }

    override fun initObserver() {

    }


    /**
     *
     * 收藏成功
     */
    private fun collectSuccess() {
        ToastUtils.show(R.string.web_success_collect)
        EventBusUtils.postEvent(MessageEvent(MessageEvent.MessageType.CollectSuccess))
    }

    @LoginCheck
    private fun collectArticle() {
        mViewModel.collectArticle(articleId).observe(this) {
            collectSuccess()
        }
    }

    override fun initRequestData() {

    }

    override fun reLoadData() {

    }
}