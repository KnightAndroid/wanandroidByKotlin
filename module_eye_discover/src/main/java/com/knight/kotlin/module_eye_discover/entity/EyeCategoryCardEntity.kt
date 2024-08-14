package com.knight.kotlin.module_eye_discover.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


/**
 * @author created by luguian
 * @organize
 * @Date 2024/8/7 15:20
 * @descript:发现分类卡片实体
 */
data class EyeCategoryCardEntity (
    val type:String,
    val data:EyeCategoryCardDataEntity,
    val id : Int,
    val adIndex:Int,
    val tag:String?
):BaseEyeDiscoverEntity()


@Parcelize
data class EyeCategoryCardDataEntity(
    val dataType : String,
    val header : EyeCategoryHeadEntity,
    val count : Int,
    val adTrack:String?,
    val footer:String,
    val itemList:List<EyeCategoryItemEntity>
):Parcelable

@Parcelize
data class EyeCategoryItemEntity(
    val type:String,
    val data:SquareCard,
    val tag:String?,
    val id:Int,
    val adIndex:Int
):Parcelable

@Parcelize
data class SquareCard(
    val id:Long,
    val title:String,
    val image:String,
    val actionUrl:String
) : Parcelable

@Parcelize
data class EyeCategoryHeadEntity(
    val actionUrl: String,
    val cover: String?,
    val font: String,
    val id: Int,
    val label: String?,
    val labelList: String?,
    val rightText: String,
    val subTitle: String?,
    val subTitleFont: String?,
    val textAlign: String,
    val title: String
): Parcelable





