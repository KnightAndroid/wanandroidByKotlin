package com.knight.kotlin.library_util

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import com.knight.kotlin.library_base.util.CacheUtils

/**
 * Author:Knight
 * Time:2022/3/30 15:53
 * Description:DialogUtils
 */
object DialogUtils {
    /**
     *
     * 获取Dialog
     * @param context
     * @return
     */
    fun getDialog(context: Context): AlertDialog.Builder {
        return AlertDialog.Builder(context)
    }

    /**
     *
     * 确定和取消都有点击事件
     * @param context
     * @param message
     * @param okClickListener
     * @param cancelClickListener
     * @return
     */
    fun getConfirmDialog(
        context: Context,
        message: String?,
        okClickListener: DialogInterface.OnClickListener?,
        cancelClickListener: DialogInterface.OnClickListener?
    ) {
        val builder = getDialog(context)
        builder.setMessage(message)
        builder.setPositiveButton(context.getString(R.string.util_confim), okClickListener)
        builder.setNegativeButton(context.getString(R.string.util_cancel), cancelClickListener)
        val dialog = builder.create()
        dialog.show()
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(CacheUtils.getThemeColor())
    }
}