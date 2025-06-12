package com.knight.kotlin.library_base.ktx

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.text.Html
import android.text.Spanned
import android.view.View
import android.view.WindowManager
import androidx.annotation.MainThread
import androidx.annotation.Px
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.baidu.location.BDLocation
import com.knight.kotlin.library_base.BaseApp
import com.knight.kotlin.library_base.R
import com.knight.kotlin.library_base.activity.BaseActivity
import com.knight.kotlin.library_base.config.Appconfig
import com.knight.kotlin.library_base.config.CacheKey
import com.knight.kotlin.library_base.entity.UserInfoEntity
import com.knight.kotlin.library_base.fragment.BaseDialogFragment
import com.knight.kotlin.library_base.fragment.BaseFragment
import com.knight.kotlin.library_base.util.ActivityManagerUtils
import com.knight.kotlin.library_base.util.CacheUtils
import com.knight.kotlin.library_base.util.StatusBarUtils
import com.knight.kotlin.library_base.util.dp2px
import com.knight.kotlin.library_base.vm.BaseViewModel
import com.knight.kotlin.library_base.widget.loadcircleview.ProgressHud
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
 * 获取屏幕高度
 *
 * @return
 */
fun getScreenHeight():Int {
    val wm = BaseApp.application.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
        wm.currentWindowMetrics.bounds.height()
    } else {
        wm.defaultDisplay.height
    }
}

/**
 * 获取屏幕宽度
 *
 * @return
 */
fun getScreenWidth():Int {
    val wm = BaseApp.application.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
        wm.currentWindowMetrics.bounds.width()
    } else {
        wm.defaultDisplay.width
    }
}


/**
 *
 * 获取屏幕高度(包含状态栏)
 */
val Context.screenHeightWithStatus
    get() = resources.displayMetrics.heightPixels + StatusBarUtils.getStatusBarHeight(this)


/**
 *
 * 状态栏高度
 */
val Context.statusHeight
    get() = StatusBarUtils.getStatusBarHeight(this)


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
 * 返回维度
 */
fun getLatitude():Double {
    Appconfig.location?.let {
        return it.latitude
    } ?:run {
       return 4.9E-324
    }


}

/**
 *
 * 返回经纬度信息
 */
fun getLocation():BDLocation? {
    Appconfig.location ?.let {
        return it
    } ?:run {
        Appconfig.location= CacheUtils.getDataInfo(CacheKey.CURRENTLOCATION, BDLocation::class.java)
        return Appconfig.location
    }
}

/**
 * 返回经度
 */
fun getLongitude():Double {
    Appconfig.location?.let {
        return it.longitude
    } ?:run {
        return 4.9E-324
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
    observeEventData(mViewModel.showLoading) {

        showLoading(msg = mViewModel.showLoading.value ?:"")
    }
    observeEventData(mViewModel.dismissLoading) {
        dismissLoading()
    }

}

internal fun BaseFragment<*, *>.subscribeData() {
    mViewModel.showLoading.observe(this) {

        showLoading(it)
    }

    mViewModel.dismissLoading.observe(this){
        dismissLoading()
    }

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



/**
 * 创建viewModel
 */
fun <VM: BaseViewModel,VB: ViewBinding> BaseActivity<VB, VM>.createViewModel(): VM {
    return ViewModelProvider(this)[getVmClazz(this)]
}

/**
 * 创建viewModel
 */
fun <VM: BaseViewModel,VB: ViewBinding> BaseFragment<VB,VM>.createViewModel(): VM {
    return ViewModelProvider(this)[getVmClazz(this)]
}
/**
 * 创建viewModel
 */
fun <VB: ViewBinding,VM: BaseViewModel,> BaseDialogFragment<VB, VM>.createViewModel(): VM {
    return ViewModelProvider(this)[getVmClazz(this)]
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





