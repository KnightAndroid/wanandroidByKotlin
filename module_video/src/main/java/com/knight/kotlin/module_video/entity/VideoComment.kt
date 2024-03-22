package com.knight.kotlin.module_video.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Author:Knight
 * Time:2024/3/19 14:46
 * Description:VideoComment
 */
@Parcelize
data class VideoComment (
    val commentId : Long,
    val commentUser : VideoCommentUser,
    val content : String,//评论的内容
    val isLike : Boolean,//你是否点赞过这条评论
    val itemCommentList : List<VideoCommentItem>,//子评论列表
    val itemCommentNum : Long,//子评论的数量
    val jokeId:Long,//被评论的段子id
    val jokeOwnerUserId : Long,//段子所属用户
    val likeNum : Long,//评论的点赞数
    val timeStr:String//评论时间戳
): Parcelable