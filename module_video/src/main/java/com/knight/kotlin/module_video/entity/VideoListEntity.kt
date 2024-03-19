package com.knight.kotlin.module_video.entity

import android.os.Parcelable
import com.google.android.exoplayer2.source.BaseMediaSource
import kotlinx.parcelize.Parcelize

/**
 * Author:Knight
 * Time:2024/2/26 11:25
 * Description:VideoListEntity
 */

@Parcelize
data class VideoListEntity(
    val joke:VideoEntity,
    val user:VideoUser,
    val info : VideoInfo
): Parcelable

@Parcelize
data class VideoEntity(
    val jokesId:Long,
    var videoUrl : String,
    val thumbUrl: String,
    val videoSize : String,
    val content : String,//视频简介
    val addTime : String,
    /** 本地文件缓存资源 */
    val videoTime : Long
): Parcelable


@Parcelize
data class VideoInfo(
    val LikeNum:Long,
    val shareNum:Long,
    val commentNum:Long,
    val disLikeNum:Long,
    var isLike : Boolean,
    val isUnlike : Boolean,
    val isAttention : Boolean

):Parcelable


@Parcelize
data class VideoUser (
    val userId : Long,
    val nickName : String,//昵称
    val signature : String,
    val avatar:String //头像
): Parcelable


data class VideoPlayEntity(
    val userId : Long,
    val jokeId : Long,
    var videoUrl : String,
    val thumbUrl : String,
    val videoSize : String,
    val nickName: String,
    val avatar:String, //头像
    val comment:String,
    val LikeNum:Long,
    val shareNum:Long,
    val commentNum:Long,
    val disLikeNum:Long,
    var isLike : Boolean,
    val isUnlike : Boolean,
    val isAttention : Boolean,
    var mediaSource: BaseMediaSource?=null,
    )


