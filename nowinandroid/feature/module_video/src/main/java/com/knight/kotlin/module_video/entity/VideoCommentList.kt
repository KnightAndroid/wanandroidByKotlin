package com.knight.kotlin.module_video.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Author:Knight
 * Time:2024/3/19 15:22
 * Description:VideoCommentList
 */

@Parcelize
data class VideoCommentList(
    val adExist: Boolean,
    val count: Int,
    val itemList: List<VideoCommentItem>,
    val nextPageUrl: String,
    val total: Int,
    val errorCode:Int,
    val errorMessage:String
): Parcelable

@Parcelize
data class VideoCommentItem(
    val adIndex: Int,
    val `data`: VideoCommentData,
    val id: Int,
    val tag: String,
    val trackingData: String,
    val type: String

): Parcelable

@Parcelize
data class VideoCommentData(
    val actionUrl: String,
    val adTrack: String,
    val cover: String,
    val createTime: Long,
    val dataType: String,
    val font: String,
    val hot: Boolean,
    val id: Long,
    val imageUrl: String,
    val likeCount: Int,
    val liked: Boolean,
    val message: String,
    val parentReply: String,
    val parentReplyId: Int,
    val recommendLevel: String,
    val replyStatus: String,
    val rootReplyId: Long,
    val sequence: Int,
    val showConversationButton: Boolean,
    val showParentReply: Boolean,
    val sid: String,
    val text: String,
    val type: String,
    val ugcVideoId: String,
    val ugcVideoUrl: String,
    val user: VideoCommentUser,
    val userBlocked: Boolean,
    val userType: String,
    val videoId: Int,
    val videoTitle: String
    //    val commentItemId:Long, //子评论id
//    val commentParentId:Long, //主评论id
//    val commentUser : VideoCommentUser,//发起评论的用户
//    val commentedNickname:String,//被评论的用户名称
//    val commentedUserId:Long,//被评论的用户id
//    val content:String,//评论内容
//    val isReplyChild :Boolean, //是否回复子内容
//    val jokeId:Long,//段子id
//    val timeStr:String//评论时间




//    val commentId : Long,
//    val commentUser : VideoCommentUser,
//    val content : String,//评论的内容
//    val isLike : Boolean,//你是否点赞过这条评论
//    val itemCommentList : List<VideoCommentItem>,//子评论列表
//    val itemCommentNum : Long,//子评论的数量
//    val jokeId:Long,//被评论的段子id
//    val jokeOwnerUserId : Long,//段子所属用户
//    val likeNum : Long,//评论的点赞数
//    val timeStr:String//评论时间戳
): Parcelable
@Parcelize
data class VideoCommentUser(
    val actionUrl: String,
    val area: String,
    val avatar: String,
    val birthday: String,
    val city: String,
    val country: String,
    val cover: String,
    val description: String,
    val expert: Boolean,
    val followed: Boolean,
    val gender: String,
    val ifPgc: Boolean,
    val job: String,
    val library: String,
    val limitVideoOpen: Boolean,
    val nickname: String,
    val registDate: Long,
    val releaseDate: Long,
    val uid: Int,
    val university: String,
    val userType: String
): Parcelable