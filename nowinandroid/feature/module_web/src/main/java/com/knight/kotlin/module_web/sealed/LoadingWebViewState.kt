package com.peakmain.webview.sealed

/**
 * author ：knight
 * createTime：2023/04/20
 * mail:15015706912@163.com
 * describe：
 */
sealed class LoadingWebViewState {
    object NotLoading : LoadingWebViewState()
    object HorizontalProgressBarLoadingStyle : LoadingWebViewState()
    object ProgressBarLoadingStyle : LoadingWebViewState()
    object CustomLoadingStyle : LoadingWebViewState()
}