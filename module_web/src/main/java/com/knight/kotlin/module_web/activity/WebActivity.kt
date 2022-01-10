package com.knight.kotlin.module_web.activity

import android.graphics.Bitmap
import android.os.Build
import android.view.KeyEvent
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.LinearLayout
import androidx.activity.viewModels
import com.alibaba.android.arouter.facade.annotation.Route
import com.just.agentweb.AgentWeb
import com.just.agentweb.DefaultWebClient
import com.just.agentweb.WebChromeClient
import com.just.agentweb.WebViewClient
import com.knight.kotlin.library_base.activity.BaseActivity
import com.knight.kotlin.library_base.route.RouteActivity
import com.knight.kotlin.library_base.util.CacheUtils
import com.knight.kotlin.library_base.vm.EmptyViewModel
import com.knight.kotlin.module_web.R
import com.knight.kotlin.module_web.databinding.WebActivityBinding
import com.knight.kotlin.module_web.widget.WebLayout
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
@Route(path = RouteActivity.Web.WebPager)
class WebActivity : BaseActivity<WebActivityBinding,EmptyViewModel>() {
    override val mViewModel: EmptyViewModel by viewModels()

   // @Autowired(name = "webUrl")
    var webUrl:String = ""

   // @Autowired(name = "webTitle")
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
        mAgentWeb = AgentWeb.with(this@WebActivity)
              .setAgentWebParent(mBinding.webLl,LinearLayout.LayoutParams(-1,-1))
            .useDefaultIndicator(CacheUtils.getThemeColor(),2)
            .setWebChromeClient(mWebChromeClient)
            .setWebViewClient(mWebViewClient)
            .setMainFrameErrorView(R.layout.agentweb_error_page,-1)
            .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
            .setWebLayout(WebLayout(this@WebActivity))
            //打开其他应用时，弹窗咨询用户是否前往
            .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK)
            .interceptUnkownUrl() //拦截找不到相关页面的Scheme
            .createAgentWeb()
            .ready()
            .go(webUrl)

        mWebView = mAgentWeb.webCreator.webView



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










}