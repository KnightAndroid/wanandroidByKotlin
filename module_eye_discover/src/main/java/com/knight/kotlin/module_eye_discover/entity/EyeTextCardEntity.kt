package com.knight.kotlin.module_eye_discover.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


/**
 * @author created by luguian
 * @organize
 * @Date 2024/8/7 15:52
 * @descript:文字卡片实体
 */
data class EyeTextCardEntity (
   val type : String,
   val data : EyeTextCardDataEntity,
   val tag:String?,
   val id:Int,
   val adIndex:Int,
) : BaseEyeDiscoverEntity()

@Parcelize
data class EyeTextCardDataEntity(
    val dataType:String,
    val id:Int,
    val type:String,
    val text:String,
    val subTitle:String?,
    val actionUrl:String,
    val adTrack:String?,
    val follow:String?,
    val rightText:String,

)  : Parcelable