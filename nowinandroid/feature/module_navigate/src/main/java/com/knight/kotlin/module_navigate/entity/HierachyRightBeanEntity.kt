package com.knight.kotlin.module_navigate.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Author:Knight
 * Time:2022/5/5 16:28
 * Description:HierachyRightBeanEntity
 */
@Parcelize
data class HierachyRightBeanEntity(
    var name: String = "",
    var titleName: String = "",
    var tag: String = "",
    var isTitle: Boolean = false,
    var link: String = "",
    var id: Int = 0,//跳转webView要用
    var parentName: String = "",
    var total: Int = 0,
    var position: Int = 0,
    var childrenName: MutableList<String>?=null,
    var cid: MutableList<Int>?=null,
    var collect: Boolean = false
) : Parcelable