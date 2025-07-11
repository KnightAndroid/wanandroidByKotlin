package com.knight.kotlin.library_permiss.start

import android.content.Intent



/**
 * @Description startActivity 委托接口
 * @Author knight
 * @Time 2025/6/8 17:02
 *
 */

interface IStartActivityDelegate {
    /** 跳转 Activity  */
    fun startActivity(intent: Intent)

    /** 跳转 Activity（需要返回结果）  */
    fun startActivityForResult(intent: Intent, requestCode: Int)
}