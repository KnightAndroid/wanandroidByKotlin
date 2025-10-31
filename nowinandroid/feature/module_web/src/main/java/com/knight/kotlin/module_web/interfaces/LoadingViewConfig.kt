package com.peakmain.webview.interfaces

import android.content.Context
import android.view.View

/**
 * author ：knight
 * createTime：2023/04/20
 * mail:15015706912@163.com
 * describe：
 */
interface LoadingViewConfig {

    fun isShowLoading(): Boolean
    fun getLoadingView(context: Context): View?
    fun hideLoading()
    fun showLoading(context: Context?)
    fun setProgress(progress: Int){

    }
    fun onDestroy()
}