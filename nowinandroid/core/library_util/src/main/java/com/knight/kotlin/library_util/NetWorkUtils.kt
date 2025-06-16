package com.knight.kotlin.library_util

import java.net.InetAddress
import java.net.NetworkInterface
import java.net.SocketException
import java.util.LinkedList
import java.util.Locale

/**
 * Author:Knight
 * Time:2023/5/10 17:43
 * Description:NetWorkUtils
 */
object NetWorkUtils {


    /**
     *
     * 获取ip地址
     *
     */
    fun getIpAddress(userIPV4:Boolean) :String {
        try {
            val nis = NetworkInterface.getNetworkInterfaces()
            val adds = LinkedList<InetAddress>()
            while (nis.hasMoreElements()) {
                val ni = nis.nextElement()
                if (!ni.isUp || ni.isLoopback) continue
                val addresses = ni.inetAddresses
                while (addresses.hasMoreElements()) {
                    adds.addFirst(addresses.nextElement())
                }
            }
            for (add in adds) {
                if (!add.isLoopbackAddress) {
                    val hostAddress = add.hostAddress
                    val isIPV4 = hostAddress.indexOf(":") < 0
                    if (userIPV4) {
                        if (isIPV4) return hostAddress
                    } else {
                        if (!isIPV4) {
                            val index = hostAddress.indexOf('%')
                            return if (index < 0) hostAddress.uppercase(Locale.getDefault()) else hostAddress.substring(
                                0,
                                index
                            ).uppercase(Locale.getDefault())
                        }
                    }
                }
            }
        } catch (e:SocketException) {
            e.printStackTrace()
        }
        return ""
    }
    /**
     *
     * 获取移动设备本地IP
     */
    fun getLocalIntetAddress() : InetAddress {
        var ip: InetAddress? = null
        try {
            // 列举
            val en_netInterface = NetworkInterface
                .getNetworkInterfaces()
            while (en_netInterface.hasMoreElements()) { // 是否还有元素
                val ni = en_netInterface
                    .nextElement() as NetworkInterface // 得到下一个元素
                val en_ip = ni.inetAddresses // 得到一个ip地址的列举
                while (en_ip.hasMoreElements()) {
                    ip = en_ip.nextElement()
                    ip = if (!ip.isLoopbackAddress
                        && ip.hostAddress.indexOf(":") == -1
                    ) break else null
                }
                if (ip != null) {
                    break
                }
            }
        } catch (e: SocketException) {
            e.printStackTrace()
        }
        return ip!!

    }


    /**
     *
     * 获取Mac地址
     */
    fun getMacAddress() :String?{
        var strMacAddr:String?= null

        try {
            val ip = getLocalIntetAddress()
            val b:ByteArray = NetworkInterface.getByInetAddress(ip)
                .hardwareAddress
            val buffer = StringBuffer()
            for (i in 0 until b.size) {
                if (i != 0) {
                    buffer.append(':')
                }
                val str = Integer.toHexString(b[i].toInt() and 0xFF)
                buffer.append(if (str.length == 1) "0$str" else str)
            }
            strMacAddr = buffer.toString().uppercase(Locale.getDefault())
        } catch (e:Exception) {

        }
        return strMacAddr
    }
}