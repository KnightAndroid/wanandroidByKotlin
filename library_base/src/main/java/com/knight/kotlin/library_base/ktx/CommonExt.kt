package com.knight.kotlin.library_base.ktx

import android.view.View
import kotlin.reflect.KClass

/**
 * Author:Knight
 * Time:2021/12/29 11:46
 * Description:CommonExt
 *
 */


/**
 * 设置点击事件
 * @param views 需要设置点击事件的view
 * @param onClick 点击触发的方法
// */
fun setOnClick(vararg views: View?, onClick: (View) -> Unit) {
    views.forEach {
        it?.setOnClickListener { view ->
            onClick.invoke(view)
        }
    }
}


/**
 * 设置点击事件
 * @param view 需要设置点击事件的view
 * @param onClick 点击触发的方法
 */
inline fun setOnClick(view: View?, crossinline action: () -> Unit) {
    view?.setOnClickListener {
        action.invoke()
    }
}

/**
 * 扩展函数 捕获多个异常
 *
 *
 */
fun (()->Unit).catch(vararg exceptions: KClass<out Throwable>, catchBlock:(Throwable)->Unit) {
    try {
        this()
    }catch (e:Throwable) {
        if(e::class in exceptions) catchBlock(e) else throw e
    }
}



