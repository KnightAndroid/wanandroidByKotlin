package com.knight.kotlin.module_web.activity

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.view.KeyEvent
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import com.core.library_base.ktx.toHtml
import com.core.library_base.route.RouteActivity
import com.core.library_base.vm.EmptyViewModel
import com.core.library_common.util.dp2px
import com.google.android.material.appbar.AppBarLayout
import com.just.agentweb.AgentWeb
import com.just.agentweb.DefaultWebClient
import com.just.agentweb.WebChromeClient
import com.just.agentweb.WebViewClient
import com.knight.kotlin.library_base.activity.BaseActivity
import com.knight.kotlin.library_common.config.Appconfig
import com.knight.kotlin.library_common.util.CacheUtils
import com.knight.kotlin.module_web.databinding.WebTransitionActivityBinding
import com.knight.kotlin.module_web.utils.ViewBindUtils
import com.knight.kotlin.module_web.widget.WebLayout
import com.wyjson.router.annotation.Param
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint

/**
 * Author:Knight
 * Time:2022/3/10 11:17
 * Description:WebTransitionActivity
 */
@AndroidEntryPoint
@Route(path = RouteActivity.Web.WebTransitionPager)
class WebTransitionActivity: BaseActivity<WebTransitionActivityBinding, EmptyViewModel>() {
    @JvmField
    @Param(name = "cardBgColor")
    var cardBgColor:Int = 0

    @JvmField
    @Param(name = "webUrl")
    var webUrl:String = ""

    @JvmField
    @Param(name = "title")
    var title:String = ""

    @JvmField
    @Param(name = "author")
    var author = ""

    @JvmField
    @Param(name = "chapterName")
    var chapterName = ""

    private lateinit var mAgentWeb: AgentWeb

    private lateinit var mWebView: WebView

    private var alphaMaxOffset: Float = 100.dp2px().toFloat()





    override fun WebTransitionActivityBinding.initView() {
        webTransitionToolbar.baseCompatToolbar.setBackgroundColor(
            ContextCompat.getColor(
                this@WebTransitionActivity,
                com.core.library_base.R.color.base_color_transparent
            ))
        webTransitionToolbar.baseIvBack.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                finishAfterTransition()
            } else {
                finish()
            }
        }
        initTranstionData()
        initWebView()
        webAppbar.addOnOffsetChangedListener(object:AppBarLayout.OnOffsetChangedListener{
            override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
                // 设置 toolbar 背景
               webImage.getBackground()
                    .setAlpha(255 - (255 * -verticalOffset / alphaMaxOffset ).toInt())
                if (255 - (255 * -verticalOffset / alphaMaxOffset).toInt() > 127) {
                    webTransitionToolbar.baseTvTitle.setTextColor(ContextCompat.getColor(this@WebTransitionActivity,
                        com.just.agentweb.R.color.white))
                    webTransitionToolbar.baseIvBack.setBackgroundResource(com.core.library_base.R.drawable.base_right_whitearrow)
                } else {
                    webTransitionToolbar.baseTvTitle.setTextColor(ContextCompat.getColor(this@WebTransitionActivity,
                        com.just.agentweb.R.color.black))
                    webTransitionToolbar.baseIvBack.setBackgroundResource(com.core.library_base.R.drawable.base_iv_left_arrow)
                }
            }
        })

        webCollbarlayout.post {
            alphaMaxOffset =
                (webCollbarlayout.getHeight() - webTransitionToolbar.baseCompatToolbar.getHeight()).toFloat()
        }

    }
    private val mWebViewClient: WebViewClient = object : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
            return super.shouldOverrideUrlLoading(view, request)
        }

        override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {}
    }

    private val mWebChromeClient: WebChromeClient = object : WebChromeClient() {
        override fun onReceivedTitle(view: WebView, title: String) {
            super.onReceivedTitle(view, title)
        }
    }

    private fun initTranstionData(){
        val gradientDrawable = GradientDrawable()
        gradientDrawable.setColor(cardBgColor)
        mBinding.webImage.setBackground(gradientDrawable)
        ViewCompat.setTransitionName(mBinding.webImage, Appconfig.IMAGE_TRANSITION_NAME)
        ViewCompat.setTransitionName(mBinding.webArticleAuthor, Appconfig.TEXT_AUTHOR_NAME)
        ViewCompat.setTransitionName(mBinding.webChapterName, Appconfig.TEXT_CHAPTERNAME_NAME)

        mBinding.webArticleAuthor.setText(author)
        mBinding.webChapterName.setText(chapterName)
        if (CacheUtils.getNormalDark()) {
            mBinding.webTransitionToolbar.baseTvTitle.setTextColor(Color.BLACK)
        } else {
            mBinding.webTransitionToolbar.baseTvTitle.setTextColor(ContextCompat.getColor(this,
                com.just.agentweb.R.color.white))
        }
        mBinding.webTransitionToolbar.baseTvTitle.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
        mBinding.webTransitionToolbar.baseTvTitle.text = title.toHtml()
        mBinding.webImage.background.alpha = 255
    }


    private fun initWebView() {
        mAgentWeb = AgentWeb.with(this)
            .setAgentWebParent(mBinding.webTransitionLl, LinearLayout.LayoutParams(-1,-1))
            .useDefaultIndicator(CacheUtils.getThemeColor(),2)
            .setWebChromeClient(mWebChromeClient)
            .setWebViewClient(mWebViewClient)
            .setMainFrameErrorView(com.just.agentweb.R.layout.agentweb_error_page, -1)
            .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
            .setWebLayout(WebLayout(this))
            .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK)//打开其他应用时，弹窗咨询用户是否前往其他应用
            .interceptUnkownUrl() //拦截找不到相关页面的Scheme
            .createAgentWeb()
            .ready()
            .go(webUrl)
        mWebView = mAgentWeb.webCreator.webView

        ViewBindUtils.previewWebViewPhoto(mWebView)

        val settings = mWebView.getSettings()
        mWebView.setOverScrollMode(WebView.OVER_SCROLL_NEVER)
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
        mAgentWeb?.let {
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