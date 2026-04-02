package com.knight.kotlin.module_set.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


/**
 * @author created by luguian
 * @organize
 * @Date 2026/4/2 16:17
 * @descript:配置信息
 */
@Parcelize
data class DeviceInfo(
    val systemVersion: String,
    val sdkVersion: String,
    val screenSize: String,
    val country: String,
    val timeZone: String,
    val ipAddress: String,
    val macAddress: String,
    val simOperator: String,
    val deviceId: String
): Parcelable