package com.core.library_base.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import com.core.library_base.network.enums.NetworkState


/**
 * Author:Knight
 * Time:2022/4/21 11:19
 * Description:NetworkStateUtils
 */
object NetworkStateUtils {

    /**
     * 判断当前设备是否有网络连接
     */
    fun hasNetworkCapability(context: Context): Boolean {
        try {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager?
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val activeNetwork = connectivityManager?.activeNetwork ?: return false
                val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
                return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) &&
                        networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            } else {
                val networkInfo = connectivityManager?.activeNetworkInfo ?: return false
                return networkInfo.isAvailable && networkInfo.isConnected
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    /**
     * 获取当前网络状态
     */
    fun getNetworkState(context: Context?): NetworkState {
        try {
            val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager?

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val activeNetwork = connectivityManager?.activeNetwork ?: return NetworkState.NONE
                val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return NetworkState.NONE

                return when {
                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> NetworkState.WIFI
                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> NetworkState.CELLULAR
                    else -> NetworkState.NONE
                }
            } else {
                return when (connectivityManager?.activeNetworkInfo?.type) {
                    ConnectivityManager.TYPE_MOBILE -> NetworkState.CELLULAR
                    ConnectivityManager.TYPE_WIFI -> NetworkState.WIFI
                    else -> NetworkState.NONE
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return NetworkState.NONE
    }

    internal fun getNetworkState(context: Context?, network: Network?): NetworkState {
        try {
            val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager?
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val networkCapabilities = connectivityManager?.getNetworkCapabilities(network) ?: return NetworkState.NONE
                return when {
                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> NetworkState.WIFI
                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> NetworkState.CELLULAR
                    else -> NetworkState.NONE
                }
            } else {
                return when (connectivityManager?.activeNetworkInfo?.type) {
                    ConnectivityManager.TYPE_MOBILE -> NetworkState.CELLULAR
                    ConnectivityManager.TYPE_WIFI -> NetworkState.WIFI
                    else -> NetworkState.NONE
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return NetworkState.NONE
    }
}