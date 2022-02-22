package com.knight.kotlin.library_network.bean

import com.google.gson.annotations.SerializedName

/**
 *Author:Knight
 *Time:2021/12/15 10:07
 *Description:BaseResponse
 */
data class BaseResponse<D>(
    val `data`:D,
    @SerializedName("errorCode") val code:Int,
    @SerializedName("errorMsg") val msg:String
)