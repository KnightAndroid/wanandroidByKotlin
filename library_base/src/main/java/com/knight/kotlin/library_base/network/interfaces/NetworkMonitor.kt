package com.knight.kotlin.library_base.network.interfaces

import com.knight.kotlin.library_base.network.enums.NetworkState

/**
 * Author:Knight
 * Time:2022/4/22 14:36
 * Description:NetworkMonitor
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
annotation class NetworkMonitor(
    val monitorFilter: Array<NetworkState> = [NetworkState.CELLULAR, NetworkState.WIFI, NetworkState.NONE]
)
