package com.knight.kotlin.library_util.bitmap

import java.io.File




/**
 * @author created by luguian
 * @organize
 * @Date 2024/8/23 9:20
 * @descript:压缩图片回调
 */
/**
 * 压缩回调
 * Created by lishilin on 2020/03/17
 */
abstract class CompressCallback {
    /**
     * 开始
     */
    fun onStart() {
    }

    /**
     * 成功
     *
     * @param file 保存的文件
     */
    fun onSuccess(file: File) {
    }

    /**
     * 失败
     *
     * @param e Throwable
     */
    fun onFail(e: Throwable) {
    }
}