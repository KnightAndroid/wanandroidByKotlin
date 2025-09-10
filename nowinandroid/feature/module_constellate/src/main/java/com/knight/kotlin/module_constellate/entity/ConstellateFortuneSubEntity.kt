package com.knight.kotlin.module_constellate.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


/**
 * @author created by luguian
 * @organize
 * @Date 2025/9/10 14:56
 * @descript:星座工作和学习模型
 */
@Parcelize
data class ConstellateFortuneSubEntity (
    val luckColor:String,
    val luckNumber:String,
    val adaptiveConstellation:String,
    val fitting:String,
    val avoid:String,
    val composite:String,
    val love:String,
    val cause:String,
    val work:String,
    val study:String,
    val money:String,
    val health:String,
    val author:String
): Parcelable


