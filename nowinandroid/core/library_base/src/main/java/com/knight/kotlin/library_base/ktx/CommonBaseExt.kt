package com.knight.kotlin.library_base.ktx

import android.content.Context
import android.hardware.SensorManager
import android.view.WindowManager
import androidx.annotation.MainThread
import androidx.core.content.getSystemService
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.core.library_base.ktx.appStr
import com.core.library_base.ktx.getVmClazz
import com.core.library_base.ktx.observeEventData
import com.core.library_base.vm.BaseViewModel
import com.knight.kotlin.library_base.BaseApp
import com.knight.kotlin.library_base.activity.BaseActivity
import com.knight.kotlin.library_base.fragment.BaseDialogFragment
import com.knight.kotlin.library_base.fragment.BaseFragment
import com.knight.kotlin.library_base.utils.StatusBarUtils


/**
 * @author created by luguian
 * @organize
 * @Date 2025/7/2 14:28
 * @descript:
 */


internal fun BaseActivity<*, *>.subscribeData() {
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


/**
 *
 * 显示请求框
 */
fun BaseActivity<*, *>.showLoading(msg:String = appStr(com.core.library_base.R.string.base_loading)) {
    loadingDialog.show(msg)
}

/**
 * 隐藏加载框
 */
fun BaseActivity<*, *>.dismissLoading() {
    loadingDialog.dismiss()
}


/**
 *
 * 显示请求框
 */
@MainThread
fun BaseFragment<*, *>.showLoading(msg:String = appStr(com.core.library_base.R.string.base_loading)) {
    loadingDialog.show(msg)
}

/**
 *
 * 更新请求内容
 */
@MainThread
fun BaseFragment<*, *>.updateText(msg:String) {
    loadingDialog.setText(msg)
}


/**
 * 隐藏显示卡
 */
fun BaseFragment<*, *>.dismissLoading() {
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
fun <VM: BaseViewModel,VB: ViewBinding> BaseFragment<VB, VM>.createViewModel(): VM {
    return ViewModelProvider(this)[getVmClazz(this)]
}
/**
 * 创建viewModel
 */
fun <VB: ViewBinding,VM: BaseViewModel,> BaseDialogFragment<VB, VM>.createViewModel(): VM {
    return ViewModelProvider(this)[getVmClazz(this)]
}

val Context.sensorManager: SensorManager?
    get() = if (SettingsManager.getInstance(this).isGravitySensorEnabled) {
        getSystemService()
    } else {
        null
    }

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
