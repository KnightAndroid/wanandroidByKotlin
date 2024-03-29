package com.knight.kotlin.library_base.ktx

import android.content.Context
import android.text.Html
import android.text.Spanned
import android.view.View
import androidx.annotation.MainThread
import com.knight.kotlin.library_base.R
import com.knight.kotlin.library_base.activity.BaseActivity
import com.knight.kotlin.library_base.config.Appconfig
import com.knight.kotlin.library_base.config.CacheKey
import com.knight.kotlin.library_base.entity.UserInfoEntity
import com.knight.kotlin.library_base.fragment.BaseFragment
import com.knight.kotlin.library_base.util.ActivityManagerUtils
import com.knight.kotlin.library_base.util.CacheUtils
import com.knight.kotlin.library_base.util.StatusBarUtils
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
 *
 * 获取屏幕高度(包含状态栏)
 */
val Context.screenHeightWithStatus
    get() = resources.displayMetrics.heightPixels + StatusBarUtils.getStatusBarHeight(this)
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
fun getUser():UserInfoEntity? {
    Appconfig.user?.let {
        return it
    } ?: run {
        Appconfig.user = CacheUtils.getDataInfo(CacheKey.USER, UserInfoEntity::class.java)
        return Appconfig.user
    }

}

/**
 *
 * 返回app字符串
 */
fun appStr(id:Int):String {
    return ActivityManagerUtils.getInstance()?.getTopActivity()?.getString(id) ?: ""
}


internal fun BaseActivity<*,*>.subscribeData() {
    observeEventData(mViewModel._showLoading) {

        showLoading(msg = mViewModel._showLoading.value ?:"")
    }
    observeEventData(mViewModel._dismissLoading) {
        dismissLoading()
    }

}

internal fun BaseFragment<*, *>.subscribeData() {
    mViewModel._showLoading.observe(this) {

        showLoading(it)
    }

    mViewModel._dismissLoading.observe(this){
        dismissLoading()
    }

}


/**
 *
 * 显示请求框
 */
fun BaseActivity<*,*>.showLoading(msg:String = appStr(R.string.base_loading)) {
    loadingDialog.show(msg)
}

/**
 * 隐藏加载框
 */
fun BaseActivity<*,*>.dismissLoading() {
    loadingDialog.dismiss()
}


/**
 *
 * 显示请求框
 */
@MainThread
fun BaseFragment<*,*>.showLoading(msg:String = appStr(R.string.base_loading)) {
    loadingDialog.show(msg)
}

/**
 * 隐藏显示卡
 */
fun BaseFragment<*,*>.dismissLoading() {
    loadingDialog.dismiss()
}
















