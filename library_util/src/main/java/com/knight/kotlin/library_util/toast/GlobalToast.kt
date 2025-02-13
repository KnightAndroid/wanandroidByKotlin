package com.knight.kotlin.library_util.toast

import android.app.Application




/**
 * @author created by luguian
 * @organize
 * @Date 2025/2/13 15:08
 * @descript:利用悬浮窗权限弹出全局 Toast
 */
class GlobalToast(application: Application) : CustomToast() {
    /** Toast 实现类  */
    private val mToastImpl = ToastImpl(application, this)

    override fun show() {
        // 替换成 WindowManager 来显示
        mToastImpl.show()
    }

    override fun cancel() {
        // 取消 WindowManager 的显示
        mToastImpl.cancel()
    }
}