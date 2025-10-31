package com.knight.kotlin.module_web.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.ValueCallback
import android.webkit.WebView
import android.webkit.WebViewClient.ERROR_CONNECT
import android.webkit.WebViewClient.ERROR_HOST_LOOKUP
import android.webkit.WebViewClient.ERROR_IO
import android.webkit.WebViewClient.ERROR_TIMEOUT
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.knight.kotlin.library_base.config.WebViewConstants
import com.knight.kotlin.library_base.entity.WebDataEntity
import com.knight.kotlin.library_util.StringUtils
import com.knight.kotlin.module_web.utils.ViewBindUtils
import com.knight.kotlin.module_web.vm.WebViewFragmentViewModel
import com.peakmain.webview.WebViewJsUtils
import com.peakmain.webview.annotation.CacheMode
import com.peakmain.webview.implement.loading.HorizontalProgressBarLoadingConfigImpl
import com.peakmain.webview.implement.loading.ProgressLoadingConfigImpl
import com.peakmain.webview.interfaces.LoadingViewConfig
import com.peakmain.webview.manager.H5UtilsParams
import com.peakmain.webview.manager.WebViewController
import com.peakmain.webview.manager.WebViewHandle
import com.peakmain.webview.manager.WebViewManager
import com.peakmain.webview.manager.WebViewPool
import com.peakmain.webview.sealed.HandleResult
import com.peakmain.webview.sealed.LoadingWebViewState
import com.peakmain.webview.utils.WebViewUtils
import com.peakmain.webview.view.WanWebView


/**
 * @author created by luguian
 * @organize
 * @Date 2025/10/29 11:02
 * @descript:webViewFragment
 */
open class WebViewFragment : Fragment() {
    protected var mFileUploadCallbackFirst: ValueCallback<Uri>? = null
    protected var mFileUploadCallbackSecond: ValueCallback<Array<Uri>>? = null
    public var mWebView: WanWebView? = null
    private var mEndTime: Long = 0L
    private var mGroup: FrameLayout? = null
    private var mLoadingViewConfig: LoadingViewConfig? = null
    private var mLoadingView: View? = null
    private lateinit var mLoadingWebViewState: LoadingWebViewState
    private val mH5UtilsParams = H5UtilsParams.instance
    private val mStartTime = System.currentTimeMillis()
    //字符串参数
    private val webUrl by lazy {
        arguments?.getString(WebViewConstants.WEB_URL,"")
    }

    //业务模型参数
    private val webData by lazy  {
       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
           arguments ?.getParcelable(WebViewConstants.WEB_PARAMS, WebDataEntity::class.java)
       } else {
           arguments ?.getParcelable(WebViewConstants.WEB_PARAMS)
       }
    }
    private val mViewModel by viewModels<WebViewFragmentViewModel>()
    private var mWebViewHandle: WebViewHandle? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        context?.let {
            val frameLayout = FrameLayout(it)
            frameLayout.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            initView(frameLayout)
            addLoadingView(frameLayout)
            return frameLayout
        }
        return null
    }

    private fun addLoadingView(frameLayout: FrameLayout) {
        mGroup = frameLayout
        val webViewParams = mWebView?.getWebViewParams() ?: return
        mWebViewHandle = WebViewHandle(mWebView, mH5UtilsParams.mHandleUrlParamsCallback,mWebView?.getWebViewParams()?.mHandleUrlParamsCallback)
        mLoadingWebViewState =
            mH5UtilsParams.mLoadingWebViewState ?: webViewParams.mLoadingWebViewState
        if (mLoadingWebViewState == LoadingWebViewState.HorizontalProgressBarLoadingStyle) {
            webViewParams.mLoadingViewConfig = HorizontalProgressBarLoadingConfigImpl()
        } else if (webViewParams.mLoadingWebViewState == LoadingWebViewState.ProgressBarLoadingStyle) {
            webViewParams.mLoadingViewConfig = ProgressLoadingConfigImpl()
        }
        mViewModel.addLoadingView(mLoadingWebViewState) {
            mLoadingViewConfig =
                mH5UtilsParams.mLoadingViewConfig ?: webViewParams.mLoadingViewConfig
            mLoadingView = mLoadingViewConfig?.getLoadingView(frameLayout.context)
            mLoadingView?.visibility = View.VISIBLE
            mLoadingView?.let { loadingView ->
                val parent = loadingView.parent
                if (parent is ViewGroup) {
                    parent.removeView(loadingView)
                }
                frameLayout.addView(loadingView, -1)
            }
        }

    }
    fun executeJs(method: String, data: String): Boolean {
        return executeJs(mWebView,method,data)
    }


    fun executeJs(webView: WebView?, method: String, data: String): Boolean {
        return WebViewJsUtils.getInstance().executeJs(webView, method, data)
    }

    fun executeJs(webView: WebView?, method: String, vararg datas: String): Boolean {
        return WebViewJsUtils.getInstance().executeJs(webView, method, datas = datas)
    }

    private fun initView(fragmentView: FrameLayout?) {
        mWebView = WebViewPool.instance.getWebView(context)
        mWebView?.let {
            ViewBindUtils.previewWebViewPhoto(it)
        }
        WebViewManager.instance.register(this)
        mWebView?.apply {

            //不显示滚动条
            isHorizontalScrollBarEnabled = false
            isVerticalScrollBarEnabled = false
            if (mH5UtilsParams.mCacheMode != CacheMode.LOAD_NO_CACHE) {
                settings.cacheMode = mH5UtilsParams.mCacheMode
            }
            mViewModel.addWebView(fragmentView, this)
            blackMonitorCallback = {

            }
        }




        loadUrl2WebView()

    }

    private fun loadUrl2WebView() {
        val curUrl = getWebViewUrl()
        if (!TextUtils.isEmpty(curUrl)) {
            mWebView?.loadUrl(curUrl!!)
        } else {
            //LogUtils.e("WebView is empty page not found!")
        }
    }

    private fun getWebViewUrl(): String? {
        if (!StringUtils.isEmpty(webUrl)) {
            return webUrl
        } else {
            return webData?.webUrl
        }

    }


    fun canGoBack(): Boolean {
        return mWebView?.canGoBack() ?: false
    }

    fun webViewPageGoBack() {
        if (canGoBack()) {
            mWebView?.goBack()
        }
    }

    override fun onDestroy() {
        mViewModel.onDestroy(mWebView)
        mWebView = null
        mLoadingViewConfig?.onDestroy()
        super.onDestroy()
    }

    fun onPageStarted(view: WebView?, url: String?) {
        mViewModel.showLoading(view, mLoadingWebViewState, mLoadingViewConfig)
        mWebView?.postBlankMonitorRunnable()
    }

    fun onPageFinished(view: WebView, url: String) {
        mEndTime = System.currentTimeMillis()
        mViewModel.hideLoading(mLoadingWebViewState, mLoadingViewConfig)
        mH5UtilsParams.mExecuteJsPair?.also {
            executeJs(mWebView, it.first, it.second)
            it.third?.invoke(mWebView, this)
        }
        mWebView?.removeBlankMonitorRunnable()
    }

    fun shouldOverrideUrlLoading(view: WebView, url: String): HandleResult {
        mViewModel.shouldOverrideUrlLoading(activity, view, url)
        return mWebViewHandle?.handleUrl(url) ?: HandleResult.NotConsume
    }

    fun onReceivedError(view: WebView?, err: Int, des: String?, failingUrl: String?) {
        val context = context ?: return
        val webViewParams = mWebView?.getWebViewParams()
        WebViewUtils.instance.checkNetworkInfo(context.applicationContext, {
            showNoNetwork(webViewParams, failingUrl)
        }) {
            when (err) {
                ERROR_HOST_LOOKUP, ERROR_CONNECT, ERROR_TIMEOUT, ERROR_IO ->
                    showNoNetwork(webViewParams, failingUrl)
            }
        }
    }

    private fun showNoNetwork(
        webViewParams: WebViewController.WebViewParams?,
        failingUrl: String?,
    ) {
        mViewModel.showNoNetWorkView(webViewParams, activity, failingUrl, mWebView, mGroup) {
            mWebView?.reload()
        }
    }


    fun onReceivedTitle(view: WebView?, title: String) {
        mViewModel.onReceivedTitle(activity, view, title)
    }

    fun onProgressChanged(view: WebView?, newProgress: Int) {
        if (mLoadingWebViewState ==
            LoadingWebViewState.HorizontalProgressBarLoadingStyle ||
            mLoadingWebViewState == LoadingWebViewState.CustomLoadingStyle
        ) {
            mLoadingViewConfig?.setProgress(newProgress)
        }
        if (mLoadingWebViewState != LoadingWebViewState.NotLoading) {
            mLoadingViewConfig?.let {
                if (!it.isShowLoading() && view?.context != null) {
                    it.showLoading(view.context)
                }
                if (newProgress == 100 && it.isShowLoading()) {
                    it.hideLoading()
                }
            }
        }

    }

    fun loadUrl(url: String) {
        mWebView?.loadUrl(url)
    }


    fun reloadUrl(view: WebView?,url: String) {
       // mViewModel.showLoading(view, mLoadingWebViewState, mLoadingViewConfig)
        mWebView?.loadUrl(url)
    }


    fun openFileInput(
        fileUploadCallbackFirst: ValueCallback<Uri>?,
        fileUploadCallbackSecond: ValueCallback<Array<Uri>>?,
        acceptType: String?,
    ) {
        if (mFileUploadCallbackFirst != null) {
            mFileUploadCallbackFirst!!.onReceiveValue(null)
        }
        mFileUploadCallbackFirst = fileUploadCallbackFirst

        if (mFileUploadCallbackSecond != null) {
            mFileUploadCallbackSecond!!.onReceiveValue(null)
        }
        mFileUploadCallbackSecond = fileUploadCallbackSecond
        val i = Intent(Intent.ACTION_GET_CONTENT)
        i.addCategory(Intent.CATEGORY_OPENABLE)
        var type = acceptType
        if (TextUtils.isEmpty(acceptType)) {
            type = "image/*"
        }
        i.type = type
        activity?.startActivityForResult(
            Intent.createChooser(i, "File Chooser!"),
            REQUEST_CODE_FILE_PICKER
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        if (requestCode == REQUEST_CODE_FILE_PICKER) {
            if (resultCode == Activity.RESULT_OK) {
                if (intent != null) {
                    if (mFileUploadCallbackFirst != null) {
                        mFileUploadCallbackFirst!!.onReceiveValue(intent.data)
                        mFileUploadCallbackFirst = null
                    } else if (mFileUploadCallbackSecond != null) {
                        val dataUris: Array<Uri>? = try {
                            arrayOf(Uri.parse(intent.dataString))
                        } catch (e: Exception) {
                            null
                        }
                        mFileUploadCallbackSecond!!.onReceiveValue(dataUris)
                        mFileUploadCallbackSecond = null
                    }
                }
            } else {
                if (mFileUploadCallbackFirst != null) {
                    mFileUploadCallbackFirst!!.onReceiveValue(null)
                    mFileUploadCallbackFirst = null
                } else if (mFileUploadCallbackSecond != null) {
                    mFileUploadCallbackSecond!!.onReceiveValue(null)
                    mFileUploadCallbackSecond = null
                }
            }
        }
    }


    companion object {
        private const val REQUEST_CODE_FILE_PICKER = 51426

    }

    override fun onResume() {
        super.onResume()
    }

    fun getStartTime(): Long {
        return mStartTime
    }
}