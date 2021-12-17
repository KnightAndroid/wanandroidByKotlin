package com.knight.kotlin.library_util.network

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities

/**
 * Author:Knight
 * Time:2021/12/16 11:10
 * Description:NetworkCallbackImpl
 */
class NetworkCallbackImpl : ConnectivityManager.NetworkCallback() {


    /**
     * 当前网络类型
     */
    var currentNetworkType = -1


    /**
     * 当前网络是否已连接
     */
    var isConnected = false

    /**
     * 注册监听
     */
    var changeCall:NetworkStateChangeListener? = null

    override fun onAvaliable(network: Network) {
        super.onAvailable(network)
        isConnected = true
        changeCall?.networkConnectChange(isConnected)
    }

    override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
        super.onCapabilitiesChanged(network, networkCapabilities)
        if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) {
            currentNetworkType = when {
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ->{
                    NetworkCapabilities.TRANSPORT_WIFI
                }
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ->{
                    NetworkCapabilities.TRANSPORT_CELLULAR
                }
                else -> {
                    NetworkCapabilities.TRANSPORT_WIFI
                }
            }
            changeCall?.networkTypeChange(currentNetworkType)
        }
    }




}