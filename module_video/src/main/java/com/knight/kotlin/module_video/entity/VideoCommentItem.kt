package com.knight.kotlin.module_video.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Author:Knight
 * Time:2024/3/19 14:50
 * Description:VideoCommentItem
 */
@Parcelize
data class VideoCommentItem (
    val commentItemId:Long, //子评论id
    val commentParentId:Long, //主评论id
    val commentUser : VideoCommentUser,//发起评论的用户
    val commentedNickname:String,//被评论的用户名称
    val commentedUserId:Long,//被评论的用户id
    val content:String,//评论内容
    val isReplyChild :Boolean, //是否回复子内容
    val jokeId:Long,//段子id
    val timeStr:String//评论时间
): Parcelable