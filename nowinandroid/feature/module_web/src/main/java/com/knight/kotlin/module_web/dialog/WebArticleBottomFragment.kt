package com.knight.kotlin.module_web.dialog

import android.content.Intent
import android.net.Uri
import android.view.Gravity
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.core.library_base.event.MessageEvent
import com.knight.kotlin.library_base.fragment.BaseDialogFragment
import com.core.library_base.util.EventBusUtils
import com.flyjingfish.android_aop_core.annotations.SingleClick
import com.knight.kotlin.library_aop.loginintercept.LoginCheck
import com.knight.kotlin.library_base.fragment.BaseMviDialogFragment
import com.knight.kotlin.library_util.SystemUtils
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.library_util.toast.ToastUtils
import com.knight.kotlin.module_web.R
import com.knight.kotlin.module_web.contact.WebContract
import com.knight.kotlin.module_web.databinding.WebArticleBottomDialogBinding
import com.knight.kotlin.module_web.vm.WebVm
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

/**
 * Author:Knight
 * Time:2022/1/13 10:29
 * Description:WebArticleBottomFragment
 */

@AndroidEntryPoint
class WebArticleBottomFragment constructor(
    private val articleUrl: String,
    private val articleTitle: String,
    private val articleId: Int,
    private val collect: Boolean
) : BaseMviDialogFragment<
        WebArticleBottomDialogBinding,
        WebVm,
        WebContract.Event,
        WebContract.State,
        WebContract.Effect>() {

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

    override fun cancelOnTouchOutSide(): Boolean = true

    override fun WebArticleBottomDialogBinding.initView() {
        webTvCollectArticle.visibility = if (collect) View.GONE else View.VISIBLE
        setOnClickListener(webTvCopyUrl, webTvShareArticle, webTvCollectArticle, webTvOpenBrowser)
    }

    override fun initObserver() {

    }

    override fun initRequestData() {

    }

    override fun reLoadData() {

    }

    @SingleClick
    override fun onClick(v: View) {
        when (v) {
            mBinding.webTvCollectArticle -> {
                sendEvent(WebContract.Event.CollectArticle(articleId))
            }
            mBinding.webTvCopyUrl -> {
                activity?.let { SystemUtils.copyContent(it, articleUrl) }
                toast(R.string.web_success_copyurl)
                dismiss()
            }
            mBinding.webTvOpenBrowser -> {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(articleUrl))
                startActivity(intent)
                dismiss()
            }
            mBinding.webTvShareArticle -> {
                if (!articleUrl.isNullOrEmpty() && (articleUrl.startsWith("http"))) {
                    val intent = Intent(Intent.ACTION_SEND).putExtra(
                        Intent.EXTRA_TEXT,
                        getString(
                            R.string.web_share_article_url,
                            getString(com.core.library_base.R.string.base_app_name),
                            articleTitle,
                            articleUrl
                        )
                    )
                    intent.type = "text/plain"
                    startActivity(Intent.createChooser(intent, getString(R.string.web_share_article)))
                    dismiss()
                }
            }
        }
    }

    override fun renderState(state: WebContract.State) {
        // 根据需要实现，无则留空
    }

    override fun handleEffect(effect: WebContract.Effect) {
        when (effect) {
            is WebContract.Effect.CollectSuccess -> {
                collectSuccess()
            }
            is WebContract.Effect.ShowToast -> {
                toast(effect.msg)
            }
        }
    }

    private fun collectSuccess() {
        ToastUtils.show(R.string.web_success_collect)
        EventBusUtils.postEvent(MessageEvent(MessageEvent.MessageType.CollectSuccess))
        dismiss()
    }
}