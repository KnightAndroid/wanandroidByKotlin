package com.core.library_base.network

import com.core.library_base.network.enums.NetworkState
import java.lang.reflect.Method

/**
 * Author:Knight
 * Time:2022/4/22 14:45
 * Description:NetworkStateReceiverMethod
 */
class NetworkStateReceiverMethod(
    var any: Any? = null,
    var method: Method? = null,
    var monitorFilter: Array<NetworkState>? = null
)