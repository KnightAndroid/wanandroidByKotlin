package com.knight.kotlin.module_eye_discover.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


/**
 * @author created by luguian
 * @organize
 * @Date 2024/8/7 16:22
 * @descript:
 */
data class EyeDiscoverMiddleBannerEntity (
   val type : String,
   val data : EyeDiscoverMiddleBannerData,
   val tag:String?,
   val id : Int,
   val adIndex : Int
) :BaseEyeDiscoverEntity()


@Parcelize
data class EyeDiscoverMiddleBannerData (

    val  dataType : String,
    val id : Int,
    val title:String,
    val description : String?,
    val image:String,
    val actionUrl:String,
    val adTrack:String?,
    val shade:Boolean,
    val label:String?,
    val labelList:String?,
    val header:String?,
    val autoPlay:Boolean

) : Parcelable