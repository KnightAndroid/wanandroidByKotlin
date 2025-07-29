package com.knight.kotlin.module_home.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


/**
 * @author created by luguian
 * @organize
 * @Date 2025/2/20 10:32
 * @descript:早报实体
 */
@Parcelize
data class ZaoBaoBean(
    val code :String,
    val msg:String,
    val date:String,
    val news:List<String>,
    val weiyu:String,
    val tip:String,
    val image:String,
    val audio:String,
    val head_image:String,
): Parcelable