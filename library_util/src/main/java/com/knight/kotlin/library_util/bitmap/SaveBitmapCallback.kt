package com.knight.kotlin.library_util.bitmap

import java.io.File


/**
 * @author created by luguian
 * @organize
 * @Date 2024/8/23 9:21
 * @descript:保存图片回调
 */
/**
 * 保存Bitmap回调
 * Created by lishilin on 2020/03/16
 */
abstract class SaveBitmapCallback {
    /**
     * 成功
     *
     * @param file 保存的文件
     */
    open fun onSuccess(file: File) {
    }

    /**
     * 失败
     *
     * @param e Exception
     */
    open fun onFail(e: Exception) {
    }
}