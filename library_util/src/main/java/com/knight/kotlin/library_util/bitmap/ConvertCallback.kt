package com.knight.kotlin.library_util.bitmap

import android.graphics.Bitmap


/**
 * @author created by luguian
 * @organize
 * @Date 2024/8/23 9:15
 * @descript:
 */
interface ConvertCallback {
    /**
     * 成功
     *
     * @param bitmap 拼图后的Bitmap
     */
    fun onSuccess(bitmap: Bitmap) {
    }

    /**
     * 失败
     *
     * @param e Exception
     */
    fun onFail(e: Exception) {
    }

}