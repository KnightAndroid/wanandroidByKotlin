package com.knight.kotlin.library_base.ktx

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData

/**
 * 对LiveData订阅的简化封装
 *
 * 使用示例
 * ```
 *  override fun initObserve() {
 *      observeLiveData(mViewModel.stateViewLD, ::stateViewLivaDataHandler)
 *  }
 *
 *  private fun stateViewLivaDataHandler(data: StateLayoutEnum) {
 *      ...
 *  }
 * ```
 *
 * @receiver LifecycleOwner
 * @param liveData LiveData<T> 需要进行订阅的LiveData
 * @param action action: (t: T) -> Unit 处理订阅内容的方法
 * @return Unit
 */
inline fun <T> LifecycleOwner.observeLiveData(
    liveData: LiveData<T>,
    crossinline action: (t: T) -> Unit,
) {
    liveData.observe(this) { it?.let { t -> action(t) } }
}


/**
 * @receiver LifecycleOwner
 * @param liveData LiveData<T> 需要进行订阅的LiveData
 * @param successFlag LiveData<Boolean> 是否请求返回成功
 * @param action action: (t: T) -> Unit 处理订阅内容的方法
 * @param errorAction errorAction 错误回调方法
 * @return Unit
 */
inline fun <T> LifecycleOwner.observeLiveDataWithError(
    liveData: LiveData<T>,
    successFlag: LiveData<Boolean>,
    crossinline action: (t: T) -> Unit,
    crossinline errorAction: () -> Unit
) {
    successFlag.observe(this) { it ->
        if (it == false) {
            errorAction()
        } else {
            liveData.observe(this) { it?.let { t -> action(t) } }
        }
    }
}

/**
 *
 * 对值监听
 */
inline fun <T> LifecycleOwner.observeEventData(liveData: LiveData<T>,crossinline observer: () -> Unit) {
    liveData.observe(this) {
        observer()
    }
}
