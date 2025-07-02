package com.knight.kotlin.module_web.dialog

import android.content.Intent
import android.net.Uri
import android.view.Gravity
import android.view.View
import android.webkit.WebView
import com.knight.kotlin.library_base.fragment.BaseDialogFragment
import com.core.library_base.vm.EmptyViewModel
import com.flyjingfish.android_aop_core.annotations.SingleClick
import com.knight.kotlin.library_util.SystemUtils
import com.knight.kotlin.library_util.toast.ToastUtils
import com.knight.kotlin.module_web.R
import com.knight.kotlin.module_web.databinding.WebBottomDialogBinding


/**
 * Author:Knight
 * Time:2022/1/13 14:15
 * Description:WebBottomFragment
 */
class WebBottomFragment constructor(url: String?, mWebView: WebView?) :
    BaseDialogFragment<WebBottomDialogBinding, EmptyViewModel>() {



    private var url: String? = null
    private var mWebView: WebView? = null

    init {
        this.url = url
        this.mWebView = mWebView
    }


    companion object {
        fun newInstance(url: String?, mWebView: WebView?): WebBottomFragment {
            return WebBottomFragment(url, mWebView)
        }
    }

    override fun getGravity() = Gravity.BOTTOM
    override fun cancelOnTouchOutSide(): Boolean {
        return true
    }

    override fun WebBottomDialogBinding.initView() {
        setOnClickListener(webTvCopyUrl,webTvRefreshUrl,webTvOpenBrowser)
    }

    override fun initObserver() {
    }

    override fun initRequestData() {
    }


    @SingleClick
    override fun onClick(v: View) {
        when (v) {
            //复制网址
            mBinding.webTvCopyUrl -> {
                activity?.let { it ->
                    SystemUtils.copyContent(it, url)
                    ToastUtils.show(R.string.web_success_copyurl)
                    dismiss()
                }
            }
            //重新刷新
            mBinding.webTvRefreshUrl -> {
                mWebView?.reload()
                dismiss()
            }

            //用外部浏览器打开
            mBinding.webTvOpenBrowser -> {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                startActivity(intent)
                dismiss()
            }

        }
    }

    override fun reLoadData() {
    }
}