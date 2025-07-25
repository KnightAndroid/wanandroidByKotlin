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
    val date:String,
    val news:List<String>,
    val weiyu:String,
    val tip:String,
    val image:String,
   // val audio:String,
    val audio:ZaoBaoAudio,
    val head_image:String,
    val cover:String
): Parcelable

@Parcelize
data class ZaoBaoAudio(
    val music:String,
    val news:String
): Parcelable