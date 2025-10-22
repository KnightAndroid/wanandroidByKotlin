package com.knight.kotlin.module_video.entity

import androidx.media3.exoplayer.source.BaseMediaSource

/**
 * Author:Knight
 * Time:2024/2/26 11:25
 * Description:VideoListEntity
 */




data class VideoPlayEntity(
    val playerId : Long,
    val userId : Long,
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


