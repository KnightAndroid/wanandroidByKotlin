package com.knight.kotlin.library_network.bean

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 *Author:Knight
 *Time:2021/12/15 10:07
 *Description:BaseResponse
 */
@Parcelize
data class BaseResponse<D:Parcelable >(
    val `data`:D,
    val code:Int,
    val msg:String
):Parcelable