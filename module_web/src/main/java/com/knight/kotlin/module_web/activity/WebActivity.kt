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
import androidx.activity.viewModels
import com.flyjingfish.android_aop_core.annotations.SingleClick
import com.just.agentweb.AgentWeb
import com.just.agentweb.DefaultWebClient
import com.just.agentweb.WebChromeClient
import com.just.agentweb.WebViewClient
import com.knight.kotlin.library_base.activity.BaseActivity
import com.knight.kotlin.library_base.route.RouteActivity
import com.knight.kotlin.library_base.util.CacheUtils
import com.knight.kotlin.library_base.vm.EmptyViewModel
import com.knight.kotlin.module_web.databinding.WebActivityBinding
import com.knight.kotlin.module_web.dialog.WebBottomFragment
import com.knight.kotlin.module_web.utils.ViewBindUtils
import com.knight.kotlin.module_web.widget.WebLayout
import com.wyjson.router.annotation.Param
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
@Route(path = RouteActivity.Web.WebPager)
class WebActivity : BaseActivity<WebActivityBinding,EmptyViewModel>() {
    override val mViewModel: EmptyViewModel by viewModels()

    @JvmField
    @Param(name = "webUrl")
    var webUrl:String = ""

    @JvmField
    @Param(name = "webTitle")
    var webTitle:String = ""

    private lateinit var mAgentWeb:AgentWeb

    private lateinit var mWebView:WebView

    private val mWebChromeClient: WebChromeClient = object : WebChromeClient() {
        override fun onReceivedTitle(view: WebView?, title: String?) {
            super.onReceivedTitle(view, title)
        }
    }

    private val mWebViewClient: WebViewClient = object : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            return super.shouldOverrideUrlLoading(view, request)
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {}
    }

    override fun WebActivityBinding.initView() {
        mBinding.includeWebToolbar.baseIvRight.visibility = View.VISIBLE
        mAgentWeb = AgentWeb.with(this@WebActivity)
              .setAgentWebParent(mBinding.webLl,LinearLayout.LayoutParams(-1,-1))
            .useDefaultIndicator(CacheUtils.getThemeColor(),2)
            .setWebChromeClient(mWebChromeClient)
            .setWebViewClient(mWebViewClient)
            .setMainFrameErrorView(com.just.agentweb.R.layout.agentweb_error_page,-1)
            .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
            .setWebLayout(WebLayout(this@WebActivity))
            //打开其他应用时，弹窗咨询用户是否前往
            .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK)
            .interceptUnkownUrl() //拦截找不到相关页面的Scheme
            .createAgentWeb()
            .ready()
            .go(webUrl)

        mWebView = mAgentWeb.webCreator.webView
        initWebView(mWebView)
        ViewBindUtils.previewWebViewPhoto(mWebView)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mBinding.includeWebToolbar.baseTvTitle.text = Html.fromHtml(webTitle,Html.FROM_HTML_MODE_LEGACY)
        } else {
            mBinding.includeWebToolbar.baseTvTitle.text = Html.fromHtml(webTitle)
        }
        setOnClickListener(mBinding.includeWebToolbar.baseIvBack,mBinding.includeWebToolbar.baseIvRight)
    }

    /**
     *
     * 初始化webView
     *
     */

    private fun initWebView(webView:WebView) {
        val settings = webView.settings
        webView.overScrollMode = WebView.OVER_SCROLL_NEVER
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

    override fun initObserver() {

    }

    override fun initRequestData() {

    }

    @SingleClick
    override fun onClick(v: View) {
        when(v) {
            mBinding.includeWebToolbar.baseIvBack -> {
               finish()
            }
            mBinding.includeWebToolbar.baseIvRight -> {
                WebBottomFragment.newInstance(webUrl,mWebView).show(supportFragmentManager,"dialog_webnormal")
            }
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
        if (mAgentWeb != null) {
            if (!mAgentWeb.back()) {
                super.onBackPressed()
            }
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

    override fun reLoadData() {

    }

    override fun setThemeColor(isDarkMode: Boolean) {

    }


}