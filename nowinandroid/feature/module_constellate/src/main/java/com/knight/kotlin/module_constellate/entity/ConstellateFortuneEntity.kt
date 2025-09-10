package com.knight.kotlin.module_constellate.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


/**
 * @author created by luguian
 * @organize
 * @Date 2025/8/6 9:47
 * @descript:星座运势实体模型
 */
@Parcelize
data class ConstellateFortuneEntity (
    val ji:String,
    val yi:String,
    val all:String,
    val date:String,
    val love:String,
    val work:String,
    val money:String,
    val health:String,
    val notice:String,
    val discuss:String,
    val all_text:String,
    val love_text:String,
    val take_star:String,
    val work_text:String,
    var work_children_text:String,
    var study_children_text:String,
    val lucky_star:String,
    val money_text:String,
    val health_text:String,
    val lucky_color:String,
    val lucky_number:String

): Parcelable


