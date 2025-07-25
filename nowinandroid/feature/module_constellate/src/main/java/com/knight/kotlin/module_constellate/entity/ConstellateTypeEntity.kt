package com.knight.kotlin.module_constellate.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


/**
 * @author created by luguian
 * @organize 
 * @Date 2025/6/19 10:16
 * @descript:星座类型
 */
@Parcelize
data class ConstellateTypeEntity(
    val name:String,
    val enName:String,
    val date:String,
    val position:Int
): Parcelable