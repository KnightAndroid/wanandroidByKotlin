package com.knight.kotlin.library_util.toast

import android.app.Activity


/**
 * Author:Knight
 * Time:2021/12/17 14:21
 * Description:ActivityToast
 */
class ActivityToast(activity: Activity) : CustomToast() {
    /** Toast 实现类  */
    private val mToastImpl = ToastImpl(activity, this)

    override fun show() {
        // 替换成 WindowManager 来显示
        mToastImpl.show()
    }

    override fun cancel() {
        // 取消 WindowManager 的显示
        mToastImpl.cancel()
    }
}