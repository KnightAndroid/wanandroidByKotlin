package com.knight.kotlin.module_web.enum

import android.webkit.WebView.HitTestResult


/**
 * Author:Knight
 * Time:2022/1/6 14:23
 * Description:WebViewHitResult
 */
class WebViewHitResult constructor(mHitTestResult: HitTestResult) {
    private val mHitTestResult: HitTestResult = mHitTestResult
    fun getResult(): String? {
        return mHitTestResult.extra
    }
    fun getType(): WebViewHitResultEnum {
        return when (mHitTestResult.type) {
            HitTestResult.ANCHOR_TYPE -> WebViewHitResultEnum.ANCHOR_TYPE
            HitTestResult.PHONE_TYPE -> WebViewHitResultEnum.PHONE_TYPE
            HitTestResult.GEO_TYPE -> WebViewHitResultEnum.GEO_TYPE
            HitTestResult.EMAIL_TYPE -> WebViewHitResultEnum.EMAIL_TYPE
            HitTestResult.IMAGE_TYPE -> WebViewHitResultEnum.IMAGE_TYPE
            HitTestResult.IMAGE_ANCHOR_TYPE -> WebViewHitResultEnum.IMAGE_ANCHOR_TYPE
            HitTestResult.SRC_ANCHOR_TYPE -> WebViewHitResultEnum.SRC_ANCHOR_TYPE
            HitTestResult.SRC_IMAGE_ANCHOR_TYPE -> WebViewHitResultEnum.SRC_IMAGE_ANCHOR_TYPE
            HitTestResult.EDIT_TEXT_TYPE -> WebViewHitResultEnum.EDIT_TEXT_TYPE
            else -> WebViewHitResultEnum.UNKNOWN_TYPE
        }
    }
}