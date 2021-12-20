package com.knight.kotlin.library_util.toast

import android.annotation.SuppressLint
import android.app.Application
import android.os.Handler
import android.widget.Toast
import java.lang.reflect.Field


/**
 * Author:Knight
 * Time:2021/12/20 10:22
 * Description:SafeToast
 */
@SuppressLint("SoonBlockedPrivateApi")
class SafeToast constructor(application: Application):SystemToast(application) {
    init {
        // 反射 Toast 中的字段
        try {
            // 获取 mTN 字段对象
            val mTNField: Field = Toast::class.java.getDeclaredField("mTN")
            mTNField.setAccessible(true)
            val mTN: Any = mTNField.get(this)

            // 获取 mTN 中的 mHandler 字段对象
            val mHandlerField: Field = mTNField.getType().getDeclaredField("mHandler")
            mHandlerField.setAccessible(true)
            val mHandler: Handler = mHandlerField.get(mTN) as Handler

            // 偷梁换柱
            mHandlerField.set(mTN, SafeHandler(mHandler))
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