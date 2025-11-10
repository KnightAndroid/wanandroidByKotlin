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
import com.knight.kotlin.library_base.activity.BaseActivity
import com.knight.kotlin.library_base.entity.WebDataEntity
import com.knight.kotlin.library_common.ktx.getUser
import com.knight.kotlin.library_database.entity.HistoryReadRecordsEntity
import com.knight.kotlin.library_database.util.DataBaseUtils
import com.knight.kotlin.library_util.toast.ToastUtils
import com.knight.kotlin.library_widget.LoveAnimatorRelativeLayout
import com.knight.kotlin.module_web.R
import com.knight.kotlin.module_web.WebViewCacheHolder
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
class ArticleWebActivity: BaseActivity<WebArticleActivityBinding, WebVm>(),LoveAnimatorRelativeLayout.onCollectListener {

    @JvmField
    @Param(name="webDataEntity")
    var webDataEntity: WebDataEntity?=null
    private lateinit var webView: WanWebView
    private val loadingConfig = HorizontalProgressBarLoadingConfigImpl()



    private val webViewListener = object : WebViewListener {


        override fun onPageStarted(webView: WebView, url: String?, favicon: Bitmap?) {
            loadingConfig.showLoading(this@ArticleWebActivity)
        }
        override fun onProgressChanged(webView: WanWebView, progress: Int) {
            //  tvProgress.text = progress.toString()
            loadingConfig.setProgress(progress)
            if (progress >= 100) {
                loadingConfig.hideLoading()
            } else {
              //  loadingConfig.showLoading(this@ArticleWebActivity)
            }
        }

        override fun onReceivedTitle(webView: WanWebView, title: String) {

        }

        override fun onPageFinished(webView: WanWebView, url: String) {

        }
    }

    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun WebArticleActivityBinding.initView() {
        webToolbar.baseIvRight.visibility = View.VISIBLE
        title = webDataEntity?.title
        webLikeRl.setOnCollectListener(this@ArticleWebActivity)

        // 强转成 ConstraintLayout
        val parent = mBinding.root as ConstraintLayout

        val loadingView = loadingConfig.getLoadingView(this@ArticleWebActivity)
        val lp = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        )
        lp.topToBottom = mBinding.webToolbar.baseCompatToolbar.id

        // 添加到 ConstraintLayout 中
        parent.addView(loadingView, lp)


        webView = WebViewCacheHolder.acquireWebViewInternal(this@ArticleWebActivity)
        webView.webViewListener = webViewListener
        val layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        webLl.addView(webView, layoutParams)
        webView?.apply {
            //不显示滚动条
            isHorizontalScrollBarEnabled = false
            isVerticalScrollBarEnabled = false
            blackMonitorCallback = {

            }
        }
        webToolbar.baseIvBack.setOnClickListener { exit() }
        webToolbar.baseIvRight.setOnClickListener {
            webDataEntity?.let {
                WebArticleBottomFragment.newInstance(it.webUrl,it.title,it.articleId,it.isCollect).show(supportFragmentManager,"dialog_web")
            }

        }
        webView.loadUrl(webDataEntity?.webUrl ?: "https://wanandroid.com/")
        DataBaseUtils.saveHistoryRecord(setHistoryReadRecord())
    }




    override fun initObserver() {

    }

    override fun initRequestData() {

    }

    override fun reLoadData() {

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



    }


    fun shouldOverrideUrlLoading(view: WebView, url: String) {
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