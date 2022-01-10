package com.knight.kotlin.library_util

import android.graphics.Color
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View


/**
 * Author:Knight
 * Time:2021/12/31 15:44
 * Description:TextClickUtils
 */
class TextClickUtils :ClickableSpan() {

    interface OnClickToWebListener {
        fun goWeb()
    }

    private var mOnClickToWebListener: OnClickToWebListener? = null

    fun setOnClickWebListener(mOnClickToWebListener: OnClickToWebListener?): TextClickUtils {
        this.mOnClickToWebListener = mOnClickToWebListener
        return this
    }
    override fun onClick(widget: View) {
        TODO("Not yet implemented")
    }

    override fun updateDrawState(ds: TextPaint) {
        ds.color = Color.parseColor("#55aff4")
    }
}