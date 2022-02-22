package com.knight.kotlin.module_home.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Author:Knight
 * Time:2022/2/16 11:37
 * Description:OpenSourceBean
 */
@Parcelize
data class OpenSourceBean(
    val name: String,
    val desc: String,
    val abroadlink: String,
    val internallink:String
) : Parcelable