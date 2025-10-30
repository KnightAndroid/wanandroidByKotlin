package com.knight.kotlin.module_web.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.KeyEvent
import android.view.View
import android.webkit.WebView
import com.core.library_base.route.RouteActivity
import com.core.library_base.vm.EmptyViewModel
import com.flyjingfish.android_aop_core.annotations.SingleClick
import com.knight.kotlin.library_base.activity.BaseActivity
import com.knight.kotlin.library_base.config.WebViewConstants
import com.knight.kotlin.library_util.StringUtils
import com.knight.kotlin.module_web.R
import com.knight.kotlin.module_web.databinding.WebActivityBinding
import com.knight.kotlin.module_web.dialog.WebBottomFragment
import com.knight.kotlin.module_web.fragment.WebViewFragment
import com.peakmain.webview.manager.H5UtilsParams
import com.peakmain.webview.manager.WebViewPool
import com.wyjson.router.annotation.Param
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint


/**
 * @author created by luguian
 * @organize
 * @Date 2025/10/29 10:52
 * @descript:基类webview页面
 */
@AndroidEntryPoint
@Route(path = RouteActivity.Web.NewWebPager)
class NewWebViewActivity : BaseActivity<WebActivityBinding, EmptyViewModel>(){

    private var isNotifyTitle: Boolean = true
    @JvmField
    @Param(name = "webUrl")
    var webUrl:String = ""

    @JvmField
    @Param(name = "webTitle")
    var webTitle:String = ""
    var mWebViewFragment: WebViewFragment? = null
    private val mH5UtilsParams = H5UtilsParams.instance




    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun WebActivityBinding.initView() {
        mBinding.includeWebToolbar.baseIvRight.visibility = View.VISIBLE
        setOnClickListener(mBinding.includeWebToolbar.baseIvBack,mBinding.includeWebToolbar.baseIvRight)
        initFragment()
    }

    private fun initFragment() {
        val bundle = Bundle()
        bundle.putString(WebViewConstants.WEB_URL, webUrl)
        mWebViewFragment = WebViewFragment()
        mWebViewFragment!!.arguments = bundle
        supportFragmentManager.beginTransaction().replace(R.id.web_ll, mWebViewFragment!!)
            .commitAllowingStateLoss()
    }

    @SingleClick
    override fun onClick(v: View) {
        when(v) {
            mBinding.includeWebToolbar.baseIvBack -> {
                exit()
            }
            mBinding.includeWebToolbar.baseIvRight -> {
              //  WebBottomFragment.newInstance(webUrl,WebViewPool.instance.getWebView(this@NewWebViewActivity)).show(supportFragmentManager,"dialog_webnormal")
                val dialog = WebBottomFragment.newInstance(webUrl,WebViewPool.instance.getWebView(this@NewWebViewActivity))
                dialog.setOnWebActionListener(object : WebBottomFragment.OnWebActionListener {
                    override fun onRefreshUrl() {
                        // 这里执行外部的刷新逻辑
                        mWebViewFragment?.reloadUrl(WebViewPool.instance.getWebView(this@NewWebViewActivity),webUrl)
                    }
                })
                dialog.show(supportFragmentManager, "WebBottomDialog")

            }
        }
    }


    private fun exit() {
        if (!canGoBack()) {
            finish()
            return
        }
        webViewGoBack()
    }
    private fun canGoBack(): Boolean {
        return mWebViewFragment != null && mWebViewFragment!!.canGoBack()
    }

    private fun webViewGoBack() {
        mWebViewFragment?.webViewPageGoBack()
    }
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (canGoBack()) {
                webViewGoBack()
                true
            } else {
                finish()
                true
            }
        } else super.onKeyDown(keyCode, event)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mWebViewFragment?.onActivityResult(requestCode, resultCode, data)
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
        mH5UtilsParams.clear()
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