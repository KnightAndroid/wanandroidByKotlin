package com.knight.kotlin.module_web.utils


import android.view.View
import android.webkit.WebView
import com.core.library_base.route.RouteActivity
import com.knight.kotlin.module_web.R
import com.knight.kotlin.module_web.enum.WebViewHitResult
import com.knight.kotlin.module_web.enum.WebViewHitResultEnum
import com.wyjson.router.GoRouter


/**
 * Author:Knight
 * Time:2022/1/6 14:19
 * Description:ViewBindUtils
 */
object ViewBindUtils {


    /**
     *
     * 查看网页图片
     * @param mWebView
     */
    fun previewWebViewPhoto(mWebView: WebView) {
        mWebView.setOnLongClickListener(object : View.OnLongClickListener {
            override fun onLongClick(v: View?): Boolean {
                val hitTestResult = mWebView.hitTestResult
                val webViewHitResult = WebViewHitResult(hitTestResult)
                return when (webViewHitResult.getType()) {
                    WebViewHitResultEnum.IMAGE_TYPE, WebViewHitResultEnum.IMAGE_ANCHOR_TYPE, WebViewHitResultEnum.SRC_IMAGE_ANCHOR_TYPE -> {
                        GoRouter.getInstance().build(RouteActivity.Web.WebPreviewPhotoPager)
                            .withString("photoUri", webViewHitResult.getResult())
                            .withTransition(R.anim.web_fade_out_anim, R.anim.web_fade_in_anim)
                            .go(mWebView.context)
                        true
                    }
                    else -> false
                }
            }
        })
    }

}