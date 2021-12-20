package com.knight.kotlin.library_base.network

/**
 * Author:Knight
 * Time:2021/12/16 10:36
 * Description:NetworkStateChangeListener
 * 网络状态改变监听
 */
interface NetworkStateChangeListener {


    /**
     * 网络类型更改回调
     * @param type Int 网络类型
     * @return Unit
     *
     */
    fun networkTypeChange(type:Int)


    /**
     * 网络连接状态更改回调
     * @param isConnected Boolean 是否已连接
     * @return Unit
     *
     */
    fun networkConnectChange(isConnected:Boolean)
}