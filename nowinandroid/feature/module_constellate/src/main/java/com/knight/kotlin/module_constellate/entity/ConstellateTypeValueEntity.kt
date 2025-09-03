package com.knight.kotlin.module_constellate.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


/**
 * @author created by luguian
 * @organize
 * @Date 2025/9/3 11:11
 * @descript:各运势指数
 */
@Parcelize
data class ConstellateTypeValueEntity (
    val name:String,//中文名称
    val enName:String,//英文名称
    val value:Int //百分比
) : Parcelable