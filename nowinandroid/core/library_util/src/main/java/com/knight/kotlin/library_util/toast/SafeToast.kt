package com.knight.kotlin.library_util.toast

import android.app.Application
import android.os.Handler
import android.widget.Toast


/**
 * Author:Knight
 * Time:2021/12/20 10:22
 * Description:SafeToast
 */
class SafeToast(application: Application) : NotificationToast(application) {
    /** 是否已经 Hook 了一次 TN 内部类  */
    private var mHookTN = false

    override fun show() {
        hookToastTN()
        super.show()
    }

    private fun hookToastTN() {
        if (mHookTN) {
            return
        }
        mHookTN = true

        try {
            // 获取 Toast.mTN 字段对象
            val mTNField = Toast::class.java.getDeclaredField("mTN")
            mTNField.isAccessible = true
            val mTN = mTNField[this]

            // 获取 mTN 中的 mHandler 字段对象
            val mHandlerField = mTNField.type.getDeclaredField("mHandler")
            mHandlerField.isAccessible = true
            val mHandler = mHandlerField[mTN] as Handler

            // 如果这个对象已经被反射替换过了
            if (mHandler is SafeHandler) {
                return
            }

            // 偷梁换柱
            mHandlerField[mTN] = SafeHandler(mHandler)
        } catch (e: IllegalAccessException) {
            // Android 9.0 上反射会出现报错
            // Accessing hidden field Landroid/widget/Toast;->mTN:Landroid/widget/Toast$TN;
            // java.lang.NoSuchFieldException: No field mTN in class Landroid/widget/Toast;
            e.printStackTrace()
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        }
    }
}