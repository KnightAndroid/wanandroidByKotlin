package com.knight.kotlin.module_web.activity

import android.os.Build
import android.text.Html
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.core.library_base.route.RouteActivity
import com.core.library_base.vm.EmptyViewModel
import com.flyjingfish.android_aop_core.annotations.SingleClick
import com.knight.kotlin.library_base.activity.BaseActivity
import com.knight.kotlin.library_util.StringUtils
import com.knight.kotlin.module_web.WebViewCacheHolder
import com.knight.kotlin.module_web.databinding.WebActivityBinding
import com.knight.kotlin.module_web.dialog.WebBottomFragment
import com.knight.kotlin.module_web.view.WanWebView
import com.knight.kotlin.module_web.view.WebViewListener
import com.wyjson.router.annotation.Param
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint
import github.leavesczy.robustwebview.loading.HorizontalProgressBarLoadingConfigImpl


/**
 * @author created by luguian
 * @organize
 * @Date 2025/10/29 10:52
 * @descript:基类webview页面
 */
@AndroidEntryPoint
@Route(path = RouteActivity.Web.WebPager)
class WebViewActivity : BaseActivity<WebActivityBinding, EmptyViewModel>(){

    private var isNotifyTitle: Boolean = true
    @JvmField
    @Param(name = "webUrl")
    var webUrl:String = ""

    @JvmField
    @Param(name = "webTitle")
    var webTitle:String = ""
    private lateinit var webView: WanWebView

    private val loadingConfig = HorizontalProgressBarLoadingConfigImpl()
    private val webViewListener = object : WebViewListener {
        override fun onProgressChanged(webView: WanWebView, progress: Int) {
          //  tvProgress.text = progress.toString()
            loadingConfig.setProgress(progress)
            if (progress >= 100) {
                loadingConfig.hideLoading()
            } else {
                loadingConfig.showLoading(this@WebViewActivity)
            }
        }

        override fun onReceivedTitle(webView: WanWebView, title: String) {
            if (!StringUtils.isEmpty(title)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    mBinding.includeWebToolbar.baseTvTitle.text = Html.fromHtml(title,Html.FROM_HTML_MODE_LEGACY)
                } else {
                    mBinding.includeWebToolbar.baseTvTitle.text = Html.fromHtml(title)
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    mBinding.includeWebToolbar.baseTvTitle.text = Html.fromHtml(webTitle,Html.FROM_HTML_MODE_LEGACY)
                } else {
                    mBinding.includeWebToolbar.baseTvTitle.text = Html.fromHtml(webTitle)
                }
            }
        }

        override fun onPageFinished(webView: WanWebView, url: String) {

        }
    }



    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun WebActivityBinding.initView() {
        mBinding.includeWebToolbar.baseIvRight.visibility = View.VISIBLE

        // 强转成 ConstraintLayout
        val parent = mBinding.root as ConstraintLayout

        val loadingView = loadingConfig.getLoadingView(this@WebViewActivity)
        val lp = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        )
        lp.topToBottom = mBinding.includeWebToolbar.baseCompatToolbar.id

        // 添加到 ConstraintLayout 中
        parent.addView(loadingView, lp)


        webView = WebViewCacheHolder.acquireWebViewInternal(this@WebViewActivity)
        webView.webViewListener = webViewListener
        val layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        webLl.addView(webView, layoutParams)
        setOnClickListener(mBinding.includeWebToolbar.baseIvBack,mBinding.includeWebToolbar.baseIvRight)
        webView?.apply {
            //不显示滚动条
            isHorizontalScrollBarEnabled = false
            isVerticalScrollBarEnabled = false
            blackMonitorCallback = {

            }
        }

        webView.loadUrl(webUrl)
    }



    @SingleClick
    override fun onClick(v: View) {
        when(v) {
            mBinding.includeWebToolbar.baseIvBack -> {
                finish()
            }
            mBinding.includeWebToolbar.baseIvRight -> {
              //  WebBottomFragment.newInstance(webUrl,WebViewPool.instance.getWebView(this@NewWebViewActivity)).show(supportFragmentManager,"dialog_webnormal")
                val dialog = WebBottomFragment.newInstance(webUrl,webView)
                dialog.show(supportFragmentManager, "WebBottomDialog")

            }
        }
    }


    private fun exit() {
        if (!canGoBack()) {
            finish()
            return
        }
        webViewPageGoBack()
    }
    fun canGoBack(): Boolean {
        return webView?.canGoBack() ?: false
    }

    fun webViewPageGoBack() {
        if (canGoBack()) {
            webView?.goBack()
        }
    }
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (canGoBack()) {
                webViewPageGoBack()
                true
            } else {
                finish()
                true
            }
        } else super.onKeyDown(keyCode, event)
    }



    fun onReceivedTitle(title: String) {
        if (!StringUtils.isEmpty(title)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mBinding.includeWebToolbar.baseTvTitle.text = Html.fromHtml(title,Html.FROM_HTML_MODE_LEGACY)
            } else {
                mBinding.includeWebToolbar.baseTvTitle.text = Html.fromHtml(title)
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mBinding.includeWebToolbar.baseTvTitle.text = Html.fromHtml(webTitle,Html.FROM_HTML_MODE_LEGACY)
            } else {
                mBinding.includeWebToolbar.baseTvTitle.text = Html.fromHtml(webTitle)
            }
        }


    }

    override fun onDestroy() {
        super.onDestroy()
    }


    override fun initObserver() {

    }

    override fun initRequestData() {

    }

    override fun reLoadData() {

    }

    fun shouldOverrideUrlLoading(view: WebView, url: String) {
    }
}