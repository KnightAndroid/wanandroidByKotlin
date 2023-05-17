package com.knight.kotlin.module_message.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Author:Knight
 * Time:2023/5/16 16:13
 * Description:MessageListEntity
 */
@Parcelize
data class MessageListEntity (
    val curpage:Int,
    val offset:Int,
    val over:Boolean,
    val pageCount:Int,
    val size:Int,
    val total:Int,
    val datas:MutableList<MessageEntity>
): Parcelable

@Parcelize
data class MessageEntity(
    val category:Int,
    val date:Long,
    val fromUser:String?,
    val id:Int,
    val isRead:Int,
    val fullLink:String,
    val link:String,
    val message:String?,
    val niceDate:String?,
    val tag:String?,
    val title:String?,
    val userId:Int
):Parcelable