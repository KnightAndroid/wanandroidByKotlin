package com.knight.kotlin.module_eye_discover.entity

import android.os.Parcelable
import com.knight.kotlin.library_base.entity.EyeHeader
import com.knight.kotlin.library_base.entity.EyeItemEntity

import kotlinx.parcelize.Parcelize


/**
 * @author created by luguian
 * @organize
 * @Date 2024/8/19 11:14
 * @descript:详细专题实体
 */

@Parcelize
data class EyeSpecialTopicDetailEntity (
    val id:Long,
    val headerImage:String,
    val brief:String,
    val text:String,
    val shareLink:String,
    val count: Int,
    val errorCode:Int,
    val errorMessage:String,
    val itemList: MutableList<EyeSpecialTopicItemModel>
): Parcelable

@Parcelize
data class EyeSpecialTopicItemModel(
    val adIndex: Int,
    val data: TopicItemData,
    val id: Int,
    val tag: String,
    val trackingData: String,
    val type: String
): Parcelable

@Parcelize
data class TopicItemData(
    val dataType: String,
    val header: EyeHeader,
    val content: EyeItemEntity
): Parcelable


