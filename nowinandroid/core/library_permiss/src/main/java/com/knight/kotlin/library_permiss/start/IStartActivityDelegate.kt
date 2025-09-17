package com.knight.kotlin.library_permiss.start

import android.content.Intent
import androidx.annotation.IntRange


/**
 * @Description startActivity 委托接口
 * @Author knight
 * @Time 2025/6/8 17:02
 *
 */

interface IStartActivityDelegate {
    /** 跳转 Activity  */
    fun startActivity(intent: Intent)

    /**
     * 跳转 Activity（需要返回结果）
     */
    open fun startActivityForResult(intent: Intent, @IntRange(from = 1, to = 65535) requestCode: Int)
}