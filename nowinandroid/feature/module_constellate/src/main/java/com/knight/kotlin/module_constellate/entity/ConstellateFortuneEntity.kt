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
    val type:String,
    val name:String,
    val title:String,
    val time:String,
    val todo:TodoEntity,
    val fortune:FortuneEntity,
    val shortcomment:String,
    val fortunetext:FortuneTextEntity,
    val luckynumber:String,
    val luckycolor:String,
    val luckyconstellation:String


): Parcelable


@Parcelize
data class TodoEntity (
    val yi:String,
    val ji:String
): Parcelable

@Parcelize
data class FortuneEntity(
    val all:Int,
    val love:Int,
    val work:Int,
    val money:Int,
    val health:Int
): Parcelable


@Parcelize
data class FortuneTextEntity(
    val all:String,
    val love:String,
    val work:String,
    val money:String,
    val health:String

): Parcelable


@Parcelize
data class IndexEntity(
    val all:String,
    val love:String,
    val work:String,
    val money:String,
    val health:String
): Parcelable