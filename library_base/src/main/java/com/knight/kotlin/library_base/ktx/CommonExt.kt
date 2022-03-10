package com.knight.kotlin.library_base.ktx

import android.content.Context
import android.text.Html
import android.text.Spanned
import android.view.View
import com.knight.kotlin.library_base.config.CacheKey
import com.knight.kotlin.library_base.entity.UserInfoEntity
import com.knight.kotlin.library_base.util.CacheUtils
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

//点击本身防止短时间内重复点击
var lastClickTime = 0L
fun View.setOnClick(interval:Long = 1000,action:() -> Unit) {
    setOnClickListener{
        val currentTime = System.currentTimeMillis()
        if (lastClickTime != 0L && (currentTime -lastClickTime < interval)) {
            return@setOnClickListener
        }
        lastClickTime = currentTime
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

/**
 * 获取屏幕宽度
 */
val Context.screenWidth
    get() = resources.displayMetrics.widthPixels

/**
 * 获取屏幕高度
 */
val Context.screenHeight
    get() = resources.displayMetrics.heightPixels

/**
 * 转换html字符串
 *
 */
fun String.toHtml(flag: Int = Html.FROM_HTML_MODE_LEGACY): Spanned {
    return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
        Html.fromHtml(this, flag)
    } else {
        Html.fromHtml(this)
    }
}


/**
 * 返回用户信息
 */
fun getUser():UserInfoEntity {
    return CacheUtils.getDataInfo(CacheKey.USER, UserInfoEntity::class.java)
}



