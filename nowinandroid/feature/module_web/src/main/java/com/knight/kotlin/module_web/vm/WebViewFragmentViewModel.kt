package com.knight.kotlin.module_web.vm

import android.app.Activity
import android.view.View
import android.webkit.WebView
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.lifecycle.ViewModel
import com.knight.kotlin.module_web.R
import com.knight.kotlin.module_web.activity.ArticleWebActivity
import com.knight.kotlin.module_web.activity.WebViewActivity
import com.peakmain.webview.interfaces.LoadingViewConfig
import com.peakmain.webview.manager.WebViewController
import com.peakmain.webview.manager.WebViewManager
import com.peakmain.webview.manager.WebViewPool
import com.peakmain.webview.sealed.LoadingWebViewState
import com.peakmain.webview.view.WanWebView

/**
 * author ：knight
 * createTime：2025/7/25
 * mail:15015706912@163.com
 * describe：
 */
class WebViewFragmentViewModel() : ViewModel() {
    fun addWebView(fragmentView: FrameLayout?, pkWebView: WanWebView) {
        if (fragmentView != null && fragmentView.childCount <= 0) {
            fragmentView.addView(pkWebView)
        }
    }

    fun onDestroy(view: WanWebView?) {
        WebViewPool.instance.releaseWebView(view)
        WebViewManager.instance.unRegister()
    }


    fun onReceivedTitle(activity: Activity?, view: WebView?, title: String) {
        if (isWebViewActivity(activity)) {
            val webViewActivity = activity as WebViewActivity
            webViewActivity.onReceivedTitle(title)
        } else if (isWebArticleActivity(activity)) {
            val webArticleActivity = activity as ArticleWebActivity
            webArticleActivity.onReceivedTitle(title)
        }
    }

    fun shouldOverrideUrlLoading(activity: Activity?, view: WebView, url: String) {
        if (isWebViewActivity(activity)) {
            val webViewActivity = activity as WebViewActivity
            webViewActivity.shouldOverrideUrlLoading(view, url)
        } else if (isWebArticleActivity(activity)) {
            val webArticleActivity = activity as ArticleWebActivity
            webArticleActivity.shouldOverrideUrlLoading(view, url)
        }


    }

    private fun isWebViewActivity(activity: Activity?): Boolean {
        return activity != null && activity is WebViewActivity
    }



    private fun isWebArticleActivity(activity: Activity?) : Boolean {
        return activity != null && activity is ArticleWebActivity
    }

    fun hideLoading(
        loadingWebViewState: LoadingWebViewState,
        loadingViewConfig: LoadingViewConfig?,
    ) {
        if (loadingWebViewState != LoadingWebViewState.NotLoading) {
            loadingViewConfig?.let {
                if (it.isShowLoading()) {
                    it.hideLoading()
                }
            }
        }
    }

    fun showLoading(
        view: WebView?,
        loadingWebViewState: LoadingWebViewState,
        loadingViewConfig: LoadingViewConfig?,
    ) {
        if (loadingWebViewState != LoadingWebViewState.NotLoading
            && loadingViewConfig?.isShowLoading() == false
        ) {
            loadingViewConfig.showLoading(view?.context)
        }
    }

    fun addLoadingView(loadingWebViewState: LoadingWebViewState, block: (() -> Unit)? = null) {
        when (loadingWebViewState) {
            is LoadingWebViewState.NotLoading -> {
            }

            is LoadingWebViewState.ProgressBarLoadingStyle,
            LoadingWebViewState.CustomLoadingStyle,
            LoadingWebViewState.HorizontalProgressBarLoadingStyle,
                -> {
                block?.invoke()
            }
        }

    }

    fun showNoNetWorkView(
        webViewParams: WebViewController.WebViewParams?,
        activity: Activity?,
        failingUrl: String?,
        webView: WanWebView?,
        frameLayout: FrameLayout?,
        retryClickListener: ((String?) -> Unit)? = null,
    ) {
        if (webViewParams == null)
            showNoNetworkViewById(
                activity,
                failingUrl,
                R.layout.web_no_network,
                frameLayout,
                null,
                retryClickListener
            )
        else
            showNoNetworkViewByParams(
                activity,
                failingUrl,
                webView,
                webViewParams,
                frameLayout,
                retryClickListener
            )
    }

    private fun showNoNetworkViewByParams(
        activity: Activity?,
        failingUrl: String?,
        webView: WanWebView?,
        webViewParams: WebViewController.WebViewParams,
        frameLayout: FrameLayout?,
        retryClickListener: ((String?) -> Unit)? = null,
    ) {
        var noNetWorkView = webViewParams.mNoNetWorkView
        val noNetWorkViewId = webViewParams.mNoNetWorkViewId
        val netWorkViewBlock = webViewParams.mNetWorkViewBlock
        if (noNetWorkView == null && noNetWorkViewId != 0) {
            noNetWorkView = showNoNetworkViewById(
                activity,
                failingUrl,
                noNetWorkViewId,
                frameLayout,
                null,
                retryClickListener

            )
            netWorkViewBlock?.invoke(noNetWorkView, webView, failingUrl)
        } else if (noNetWorkView != null && noNetWorkViewId == 0) {
            showNoNetworkViewById(
                activity,
                failingUrl,
                0,
                frameLayout,
                noNetWorkView,
                retryClickListener
            )
        }
    }

    private fun showNoNetworkViewById(
        activity: Activity?,
        failingUrl: String?,
        @LayoutRes layoutId: Int,
        frameLayout: FrameLayout?,
        noNetWorkView: View?,
        retryClickListener: ((String?) -> Unit)? = null,
    ): View? {
        var mNoNetworkView = noNetWorkView
        if (mNoNetworkView == null && layoutId != 0) {
            mNoNetworkView = View.inflate(activity, layoutId, null)
        }
        addStatusView(mNoNetworkView, frameLayout)
        mNoNetworkView?.setOnClickListener {
            retryClickListener?.invoke(failingUrl)
        }
        mNoNetworkView?.visibility = View.VISIBLE
        if (activity == null) return mNoNetworkView
        return mNoNetworkView
    }

    private fun addStatusView(statusView: View?, frameLayout: FrameLayout?) {
        if (statusView == null) return
        if (statusView.parent != frameLayout) {
            statusView.layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
            frameLayout?.addView(statusView)
        }

    }

}