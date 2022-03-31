package com.knight.kotlin.library_util

import android.widget.Toast
import com.alibaba.android.arouter.launcher.ARouter
import com.knight.kotlin.library_util.toast.ToastUtils

/**
 * Author:Knight
 * Time:2021/12/21 14:52
 * Description:Utils
 * 一些扩展方法
 */


/**
 * toast
 * @param msg String 文本
 * @param duration Int 时间
 */
fun toast(msg:String,duration: Int = Toast.LENGTH_SHORT) {
    ToastUtils.show(msg)
}


/**
 * toast
 * @param msgId Int String资源ID
 * @param duration Int 时间
 */
fun toast(msgId:Int,duration:Int = Toast.LENGTH_SHORT) {
    ToastUtils.show(msgId)
}


/**
 * 跳转activity
 *
 */
fun startPage(activity:String) {
    ARouter.getInstance().build(activity).navigation()

}