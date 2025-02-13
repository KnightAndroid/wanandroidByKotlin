package com.knight.kotlin.library_util.toast

import android.app.Application
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.knight.kotlin.library_util.toast.callback.IToast


/**
 * Author:Knight
 * Time:2021/12/20 10:01
 * Description:SystemToast
 */
open class SystemToast(application: Application) : Toast(application),
    IToast {
    /** 吐司消息 View  */
    private var mMessageView: TextView? = null

    override fun setView(view: View) {
        super.setView(view)
        if (view == null) {
            mMessageView = null
            return
        }
        mMessageView = findMessageView(view)
    }

    override fun setText(text: CharSequence) {
        super.setText(text)
        if (mMessageView == null) {
            return
        }
        mMessageView!!.text = text
    }
}
