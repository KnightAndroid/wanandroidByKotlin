package com.knight.kotlin.module_home.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


/**
 * @author created by luguian
 * @organize
 * @Date 2025/6/27 14:27
 * @descript:早报获取音频
 */
@Parcelize
data class ZaoBaoAudioBean (
    val date:String,//2025-05-07
    val news:List<String>,
    val weiyu:String,
    val image:String,
    val audio:String,
    val head_image:String,
): Parcelable