package com.knight.kotlin.module_web.activity

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.webkit.WebView
import com.core.library_base.event.MessageEvent
import com.core.library_base.route.RouteActivity
import com.core.library_base.util.EventBusUtils
import com.knight.kotlin.library_aop.loginintercept.LoginCheck
import com.knight.kotlin.library_base.activity.BaseActivity
import com.knight.kotlin.library_base.config.WebViewConstants
import com.knight.kotlin.library_base.entity.WebDataEntity
import com.knight.kotlin.library_common.ktx.getUser
import com.knight.kotlin.library_database.entity.HistoryReadRecordsEntity
import com.knight.kotlin.library_database.util.DataBaseUtils
import com.knight.kotlin.library_util.toast.ToastUtils
import com.knight.kotlin.library_widget.LoveAnimatorRelativeLayout
import com.knight.kotlin.module_web.R
import com.knight.kotlin.module_web.databinding.WebArticleActivityBinding
import com.knight.kotlin.module_web.dialog.WebArticleBottomFragment
import com.knight.kotlin.module_web.fragment.WebViewFragment
import com.knight.kotlin.module_web.vm.WebVm
import com.peakmain.webview.manager.H5UtilsParams
import com.wyjson.router.annotation.Param
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint
import java.util.Date


/**
 * @author created by luguian
 * @organize
 * @Date 2025/10/30 11:30
 * @descript:文章浏览页面
 */

@AndroidEntryPoint
@Route(path = RouteActivity.Web.NewWebArticleWebPager)
class NewArticleWebActivity: BaseActivity<WebArticleActivityBinding, WebVm>(),LoveAnimatorRelativeLayout.onCollectListener {

    @JvmField
    @Param(name="webDataEntity")
    var webDataEntity: WebDataEntity?=null

    var mWebViewFragment: WebViewFragment? = null
    private val mH5UtilsParams = H5UtilsParams.instance

    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun WebArticleActivityBinding.initView() {
        webToolbar.baseIvRight.visibility = View.VISIBLE
        title = webDataEntity?.title
        webLikeRl.setOnCollectListener(this@NewArticleWebActivity)
        initFragment()

        webToolbar.baseIvBack.setOnClickListener { exit() }
        webToolbar.baseIvRight.setOnClickListener {
            webDataEntity?.let {
                WebArticleBottomFragment.newInstance(it.webUrl,it.title,it.articleId,it.isCollect).show(supportFragmentManager,"dialog_web")
            }

        }
        DataBaseUtils.saveHistoryRecord(setHistoryReadRecord())
    }


    private fun initFragment() {
        val bundle = Bundle()
        bundle.putParcelable(WebViewConstants.WEB_PARAMS, webDataEntity)
        mWebViewFragment = WebViewFragment()
        mWebViewFragment!!.arguments = bundle
        supportFragmentManager.beginTransaction().replace(R.id.web_ll, mWebViewFragment!!)
            .commitAllowingStateLoss()
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



    }


    fun shouldOverrideUrlLoading(view: WebView, url: String) {
    }
    override fun onDestroy() {
        super.onDestroy()
        mH5UtilsParams.clear()
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