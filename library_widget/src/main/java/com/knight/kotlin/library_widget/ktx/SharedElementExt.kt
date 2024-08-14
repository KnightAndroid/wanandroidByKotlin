package com.knight.kotlin.library_widget.ktx

import android.app.Activity
import android.os.Build
import android.transition.Transition
import android.transition.TransitionListenerAdapter
import android.view.View
import androidx.core.view.ViewCompat


/**
 * @author created by luguian
 * @organize
 * @Date 2024/8/14 14:43
 * @descript:容器变换动画
 */
//通用容器变换动画配置
fun Activity.transformShareElementConfig(view:View,transitionName:String,callBack:(() ->Unit) ?= null) {
    //因为进入视频详情页面后还需请求数据，所以在过渡动画完成后在请求数据
    //延迟动画执行
    postponeEnterTransition()
    //设置共用元素对应的View
    ViewCompat.setTransitionName(view, transitionName)
    //获取共享元素进入转场对象
    val mTransition: Transition = window.sharedElementEnterTransition
    //设置共享元素动画执行完成的回调事件
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        mTransition?.addListener(object : TransitionListenerAdapter() {
            override fun onTransitionEnd(transition: Transition?) {
                callBack?.run {
                    this()
                }
                mTransition?.removeListener(this)
            }
        })
    }
    //开始动画执行
    startPostponedEnterTransition()


}