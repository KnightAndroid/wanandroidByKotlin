package com.knight.kotlin.module_web.activity

import android.graphics.Bitmap
import android.os.Build
import android.text.Html
import android.view.KeyEvent
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.LinearLayout
import com.just.agentweb.AgentWeb
import com.just.agentweb.DefaultWebClient
import com.just.agentweb.WebChromeClient
import com.just.agentweb.WebViewClient
import com.knight.kotlin.library_aop.loginintercept.LoginCheck
import com.knight.kotlin.library_base.activity.BaseActivity
import com.knight.kotlin.library_base.entity.WebDataEntity
import com.knight.kotlin.library_base.event.MessageEvent
import com.knight.kotlin.library_base.ktx.getUser
import com.knight.kotlin.library_base.route.RouteActivity
import com.knight.kotlin.library_base.util.CacheUtils
import com.knight.kotlin.library_base.util.EventBusUtils
import com.knight.kotlin.library_database.entity.HistoryReadRecordsEntity
import com.knight.kotlin.library_util.DataBaseUtils
import com.knight.kotlin.library_util.toast.ToastUtils
import com.knight.kotlin.library_widget.LoveAnimatorRelativeLayout
import com.knight.kotlin.module_web.R
import com.knight.kotlin.module_web.databinding.WebArticleActivityBinding
import com.knight.kotlin.module_web.dialog.WebArticleBottomFragment
import com.knight.kotlin.module_web.utils.ViewBindUtils
import com.knight.kotlin.module_web.vm.WebVm
import com.knight.kotlin.module_web.widget.WebLayout
import com.wyjson.router.annotation.Param
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint
import java.util.Date

/**
 * Author:Knight
 * Time:2022/2/21 18:03
 * Description:WebArticleActivity
 */
@AndroidEntryPoint
@Route(path = RouteActivity.Web.WebArticlePager)
class WebArticleActivity :BaseActivity<WebArticleActivityBinding,WebVm>(),LoveAnimatorRelativeLayout.onCollectListener{



    @JvmField
    @Param(name="webDataEntity")
    var webDataEntity:WebDataEntity?=null

    private lateinit var mAgentWeb: AgentWeb

    private lateinit var mWebView: WebView
    private val mWebViewClient: WebViewClient = object : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
            return super.shouldOverrideUrlLoading(view, request)
        }

        override fun onPageStarted(webView: WebView, url: String, favicon: Bitmap?) {}
        override fun onPageFinished(webView: WebView, url: String) {
            super.onPageFinished(webView, url)
        }
    }

    private val mWebChromeClient: WebChromeClient = object : WebChromeClient() {
        override fun onReceivedTitle(view: WebView, title: String) {
            super.onReceivedTitle(view, title)
        }
    }
    override fun WebArticleActivityBinding.initView() {
        webToolbar.baseIvRight.visibility = View.VISIBLE
        title = webDataEntity?.title
        webLikeRl.setOnCollectListener(this@WebArticleActivity)
        mAgentWeb = AgentWeb.with(this@WebArticleActivity)
            .setAgentWebParent(webLl, LinearLayout.LayoutParams(-1,-1))
            .useDefaultIndicator(CacheUtils.getThemeColor(),2)
            .setWebChromeClient(mWebChromeClient)
            .setWebViewClient(mWebViewClient)
            .setMainFrameErrorView(com.just.agentweb.R.layout.agentweb_error_page,-1)
            .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
            .setWebLayout(WebLayout(this@WebArticleActivity))
            .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK)//打开其他应用时，弹窗咨询用户是否前往其他应用
            .interceptUnkownUrl()//拦截找不到相关页面的Scheme
            .createAgentWeb()
            .ready()
            .go(webDataEntity?.webUrl)
        mWebView = mAgentWeb.webCreator.webView
        mWebView.settings.domStorageEnabled = true
        initWebView(mWebView)
        ViewBindUtils.previewWebViewPhoto(mWebView)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            webToolbar.baseTvTitle.setText(Html.fromHtml(webDataEntity?.title, Html.FROM_HTML_MODE_LEGACY))
        } else {
            webToolbar.baseTvTitle.setText(Html.fromHtml(webDataEntity?.title))
        }

        webToolbar.baseIvBack.setOnClickListener { finish() }
        webToolbar.baseIvRight.setOnClickListener {
            webDataEntity?.let {
                WebArticleBottomFragment.newInstance(it.webUrl,it.title,it.articleId,it.isCollect).show(supportFragmentManager,"dialog_web")
            }

        }
        DataBaseUtils.saveHistoryRecord(setHistoryReadRecord())
    }


    private fun initWebView(webView:WebView) {
        val settings = webView.settings
        webView.overScrollMode = WebView.OVER_SCROLL_ALWAYS
        settings.domStorageEnabled = true
        settings.javaScriptEnabled = true
        settings.loadsImagesAutomatically = true
        settings.useWideViewPort = true
        settings.loadWithOverviewMode = true
        settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }
    }

    override fun onPause() {
        super.onPause()
        mAgentWeb.webLifeCycle.onPause()
    }

    override fun onResume() {
        super.onResume()
        mAgentWeb.webLifeCycle.onResume()
    }


    override fun onBackPressed() {
        if (!mAgentWeb.back()) {
            super.onBackPressed()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if (mAgentWeb.handleKeyEvent(keyCode, event)) {
            true
        } else super.onKeyDown(keyCode, event)
    }

    override fun onDestroy() {
        super.onDestroy()
        mAgentWeb.webLifeCycle.onDestroy()
    }


    override fun initObserver() {

    }

    override fun initRequestData() {

    }

    @LoginCheck
    override fun onCollect() {
        if (webDataEntity?.isCollect == false) {
            webDataEntity?.let {
                if (it.articleId > 1000) {
                    mViewModel.collectArticle(it.articleId).observerKt {
                        collectSuccess()
                    }
                }

            }
        }
    }

    /**
     *
     * 收藏成功
     */
    private fun collectSuccess() {
         webDataEntity?.isCollect = true
         ToastUtils.show(R.string.web_success_collect)
         EventBusUtils.postEvent(MessageEvent(MessageEvent.MessageType.CollectSuccess))
    }

    override fun reLoadData() {

    }

    override fun setThemeColor(isDarkMode: Boolean) {

    }



    private fun setHistoryReadRecord() : HistoryReadRecordsEntity {
        lateinit var historyReadRecordsEntity:HistoryReadRecordsEntity
        webDataEntity?.let {
            historyReadRecordsEntity = HistoryReadRecordsEntity(0,
            getUser()?.id ?: 0,it.isCollect,it.webUrl,it.articleId,it.title,it.envelopePic, Date(),it.author,it.chapterName,it.articledesc)

        } ?: run {
            historyReadRecordsEntity = HistoryReadRecordsEntity(0,0,false,"",
                0,"","",Date(),
                "","","")
        }
        return historyReadRecordsEntity

    }
}