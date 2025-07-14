package com.core.library_base.ktx

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.text.Html
import android.text.Spanned
import android.view.View
import androidx.annotation.MainThread
import androidx.annotation.Px
import com.core.library_base.R
import com.core.library_base.util.ActivityManagerUtils
import com.core.library_base.widget.loadcircleview.ProgressHud
import com.core.library_common.util.dp2px
import java.lang.reflect.ParameterizedType
import kotlin.math.min
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
 * @param action 点击触发的方法
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
 * 转换html字符串
 *
 */
fun String.toHtml(@SuppressLint("InlinedApi") flag: Int = Html.FROM_HTML_MODE_LEGACY): Spanned {
    return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
        Html.fromHtml(this, flag)
    } else {
        Html.fromHtml(this)
    }
}




/**
 *
 * 返回app字符串
 */
fun appStr(id:Int):String {
    return ActivityManagerUtils.getInstance()?.getTopActivity()?.getString(id) ?: ""
}




var loadingDialog: ProgressHud? = null

/**
 * 弹窗
 *
 *
 * @param msg
 * @return
 */
@MainThread
fun showLoadingDialog(msg:String = appStr(R.string.base_loading)) {
    loadingDialog = ActivityManagerUtils.getInstance()?.getTopActivity()?.let { ProgressHud(it) }
    loadingDialog?.run {
        show(msg)
    }
}

/**
 * 消失弹窗
 *
 */
fun dimissLoadingDialog() {
    loadingDialog?.dismiss()
}



/**
 * 获取当前类绑定的泛型ViewModel-clazz
 */
@Suppress("UNCHECKED_CAST")
fun <VM> getVmClazz(obj: Any): VM {
    return (obj.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1] as VM
}

/**
 *
 * 是否横屏
 */
val Context.isLandscape: Boolean
    get() = this.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

val Context.isTabletDevice: Boolean
    get() = (
            this.resources.configuration.screenLayout
                    and Configuration.SCREENLAYOUT_SIZE_MASK
            ) >= Configuration.SCREENLAYOUT_SIZE_LARGE

@Px
fun Context.getTabletListAdaptiveWidth(@Px width: Int): Int {
    return if (!this.isTabletDevice && !this.isLandscape) {
        width
    } else {
        min(
            width.toFloat(),

                if (this.isTabletDevice) {
                    MAX_TABLET_ADAPTIVE_LIST_WIDTH_DIP_TABLET
                } else {
                    MAX_TABLET_ADAPTIVE_LIST_WIDTH_DIP_PHONE
                }.dp2px().toFloat()

        ).toInt()
    }
}

private const val MAX_TABLET_ADAPTIVE_LIST_WIDTH_DIP_PHONE = 512
private const val MAX_TABLET_ADAPTIVE_LIST_WIDTH_DIP_TABLET = 600





