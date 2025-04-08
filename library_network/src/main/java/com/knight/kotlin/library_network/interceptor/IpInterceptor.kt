package com.knight.kotlin.library_network.interceptor

import android.util.Log
import com.knight.kotlin.library_base.config.Appconfig
import com.knight.kotlin.library_base.util.CacheUtils
import com.knight.kotlin.library_network.util.PublicIpUtils
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response


/**
 * @author created by luguian
 * @organize
 * @Date 2025/4/7 16:52
 * @descript:添加公用头部拦截器
 */
class IpInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        // 获取原始请求
        val originalRequest: Request = chain.request()
        if (Appconfig.IP.isNullOrEmpty()) {
            Appconfig.IP = CacheUtils.getIp()
        }

        if (Appconfig.IP.isNullOrEmpty()) {
            PublicIpUtils.getPublicIP { ip, error ->
                if (error != null) {
                    Log.d("ip","获取公网 IP 失败：${error.message}")
                } else {
                    ip?.let {
                        Appconfig.IP = ip
                        CacheUtils.setIp(ip)
                    }
                }
            }
        }

        if (Appconfig.IP.isNotEmpty()) {
            // 构造一个带有 IP 的请求
            val requestWithIp: Request = originalRequest.newBuilder()
                .addHeader("ip", Appconfig.IP) // 添加自定义头部
                .build()

            return chain.proceed(requestWithIp)
        } else {
            return chain.proceed(chain.request())
        }

    }

}