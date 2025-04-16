package com.knight.kotlin.module_home.entity

import android.os.Parcelable
import com.knight.kotlin.library_base.enum.PollutantIndex
import kotlinx.parcelize.Parcelize


/**
 * @author created by luguian
 * @organize
 * @Date 2025/4/16 11:27
 * @descript:污染物 实体
 */
@Parcelize
data class PollutantBean (
    val pollutantIndex:PollutantIndex,
    val concentration:Float,
    val unit:String
): Parcelable