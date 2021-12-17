package com.knight.kotlin.library_base.view

import androidx.viewbinding.ViewBinding

/**
 * Author:Knight
 * Time:2021/12/15 16:02
 * Description:BaseView
 */
interface BaseView<VB : ViewBinding> {


    /**
     *
     * 初始化View
     */
    fun VB.initView()

    /**
     *
     * 订阅LiveData
     */
    fun initObserver()

    /**
     *
     * 用于在页面创建时进行请求接口
     */
    fun initRequestData()

    /**
     *
     * 页面是否重建：
     * fragment被回收重新展示的时候为true，系统环境发生变化activity重新创建时为true
     */
    fun isRecreate():Boolean
}