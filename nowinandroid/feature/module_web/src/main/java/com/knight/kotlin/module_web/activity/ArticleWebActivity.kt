package com.knight.kotlin.module_web.activity

import android.graphics.Bitmap
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.core.library_base.event.MessageEvent
import com.core.library_base.route.RouteActivity
import com.core.library_base.util.EventBusUtils
import com.knight.kotlin.library_aop.loginintercept.LoginCheck
import com.knight.kotlin.library_base.activity.BaseMviActivity
import com.knight.kotlin.library_base.entity.WebDataEntity
import com.knight.kotlin.library_common.ktx.getUser
import com.knight.kotlin.library_database.entity.HistoryReadRecordsEntity
import com.knight.kotlin.library_database.util.DataBaseUtils
import com.knight.kotlin.library_util.toast.ToastUtils
import com.knight.kotlin.library_widget.LoveAnimatorRelativeLayout
import com.knight.kotlin.module_web.R
import com.knight.kotlin.module_web.contract.WebContract
import com.knight.kotlin.module_web.manager.WebViewCacheHolder
import com.knight.kotlin.module_web.databinding.WebArticleActivityBinding
import com.knight.kotlin.module_web.dialog.WebArticleBottomFragment
import com.knight.kotlin.module_web.view.WanWebView
import com.knight.kotlin.module_web.view.WebViewListener
import com.knight.kotlin.module_web.vm.WebVm
import com.wyjson.router.annotation.Param
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint
import github.leavesczy.robustwebview.loading.HorizontalProgressBarLoadingConfigImpl
import java.util.Date


/**
 * @author created by luguian
 * @organize
 * @Date 2025/10/30 11:30
 * @descript:文章浏览页面
 */

@AndroidEntryPoint
@Route(path = RouteActivity.Web.WebArticleWebPager)
class ArticleWebActivity :
    BaseMviActivity<
            WebArticleActivityBinding,
            WebVm,
            WebContract.Event,
            WebContract.State,
            WebContract.Effect>(),
    LoveAnimatorRelativeLayout.onCollectListener {

    @JvmField
    @Param(name = "webDataEntity")
    var webDataEntity: WebDataEntity? = null

    private lateinit var webView: WanWebView
    private val loadingConfig = HorizontalProgressBarLoadingConfigImpl()



    private val webViewListener = object : WebViewListener {

        override fun onPageStarted(
            webView: WebView,
            url: String?,
            favicon: Bitmap?
        ) {
            loadingConfig.showLoading(this@ArticleWebActivity)
        }

        override fun onProgressChanged(
            webView: WanWebView,
            progress: Int
        ) {
            loadingConfig.setProgress(progress)
            if (progress >= 100) {
                loadingConfig.hideLoading()
            }
        }

        override fun onReceivedTitle(
            webView: WanWebView,
            title: String
        ) {
            // 可选：更新标题
        }

        override fun onPageFinished(
            webView: WanWebView,
            url: String
        ) {
            // 可选：页面完成逻辑
        }
    }
    // ========================
    // initView
    // ========================
    override fun WebArticleActivityBinding.initView() {
        webToolbar.baseIvRight.visibility = View.VISIBLE
        title = webDataEntity?.title

        webLikeRl.setOnCollectListener(this@ArticleWebActivity)

        initLoadingView()
        initWebView()

        webToolbar.baseIvBack.setOnClickListener { exit() }
        webToolbar.baseIvRight.setOnClickListener {
            webDataEntity?.let {
                WebArticleBottomFragment
                    .newInstance(
                        it.webUrl,
                        it.title,
                        it.articleId,
                        it.isCollect
                    )
                    .show(supportFragmentManager, "dialog_web")
            }
        }

        webView.loadUrl(webDataEntity?.webUrl ?: "https://wanandroid.com/")
        DataBaseUtils.saveHistoryRecord(setHistoryReadRecord())
    }

    // ========================
    // Observer
    // ========================
    override fun initObserver() {
        // 不需要 State
    }

    // ========================
    // 首次请求
    // ========================
    override fun initRequestData() {
        // Web 页面无需首次请求
    }

    override fun setThemeColor(isDarkMode: Boolean) {

    }

    // ========================
    // State（占位）
    // ========================
    override fun renderState(state: WebContract.State) {}

    // ========================
    // Effect（重点）
    // ========================
    override fun handleEffect(effect: WebContract.Effect) {
        when (effect) {
            WebContract.Effect.CollectSuccess -> {
                collectSuccess()
            }
            is WebContract.Effect.ShowToast -> {
                ToastUtils.show(effect.msg)
            }
        }
    }

    // ========================
    // 收藏（🔥 改造点）
    // ========================
    @LoginCheck
    override fun onCollect() {
        val data = webDataEntity ?: return
        if (!data.isCollect && data.articleId > 0) {
            mViewModel.setEvent(
                WebContract.Event.CollectArticle(data.articleId)
            )
        }
    }

    // ========================
    // 收藏成功 UI 处理
    // ========================
    private fun collectSuccess() {
        webDataEntity?.isCollect = true
        ToastUtils.show(R.string.web_success_collect)
        EventBusUtils.postEvent(
            MessageEvent(MessageEvent.MessageType.CollectSuccess)
        )
    }

    // ========================
    // WebView 初始化（原逻辑）
    // ========================
    private fun initWebView() {
        webView = WebViewCacheHolder.acquireWebViewInternal(this)
        webView.webViewListener = webViewListener

        val layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        mBinding.webLl.addView(webView, layoutParams)

        webView.apply {
            isHorizontalScrollBarEnabled = false
            isVerticalScrollBarEnabled = false
        }
    }

    private fun initLoadingView() {
        val parent = mBinding.root as ConstraintLayout
        val loadingView = loadingConfig.getLoadingView(this)
        val lp = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        )
        lp.topToBottom = mBinding.webToolbar.baseCompatToolbar.id
        parent.addView(loadingView, lp)
    }

    // ========================
    // WebView 回退
    // ========================
    private fun exit() {
        if (!canGoBack()) {
            finish()
        } else {
            webView.goBack()
        }
    }

    private fun canGoBack() = webView.canGoBack()

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (canGoBack()) {
                webView.goBack()
                true
            } else {
                finish()
                true
            }
        } else {
            super.onKeyDown(keyCode, event)
        }
    }

    // ========================
    // History Record
    // ========================
    private fun setHistoryReadRecord(): HistoryReadRecordsEntity {
        val data = webDataEntity
        return if (data != null) {
            HistoryReadRecordsEntity(
                0,
                getUser()?.id ?: 0,
                data.isCollect,
                data.webUrl,
                data.articleId,
                data.title,
                data.envelopePic,
                Date(),
                data.author,
                data.chapterName,
                data.articledesc
            )
        } else {
            HistoryReadRecordsEntity(
                0, 0, false, "",
                0, "", "", Date(),
                "", "", ""
            )
        }
    }
}