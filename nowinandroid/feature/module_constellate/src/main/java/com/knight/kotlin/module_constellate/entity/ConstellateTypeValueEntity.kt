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
    val value:Int, //百分比
    val desc:String, //描述
    val children:List<ConstellateFortuneChildrenEntity> = arrayListOf() //子运势
) : Parcelable





@Parcelize
data class ConstellateFortuneChildrenEntity(
    val name:String, //类型名字
    val desc:String//具体描述
) : Parcelable