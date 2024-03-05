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
    val user:VideoUser
): Parcelable

@Parcelize
data class VideoEntity(
    val jokesId:Long,
    var videoUrl : String,
    val videoSize : String,
    val content : String,
    val addTime : String,
    /** 本地文件缓存资源 */
    val videoTime : Long
): Parcelable


@Parcelize
data class VideoUser (
    val userId : Long,
    val nickName : String,
    val signature : String,
    val avatar:String
): Parcelable


data class VideoPlayEntity(
    val userId : Long,
    var videoUrl : String,
    val videoSize : String,
    var mediaSource: BaseMediaSource?=null,

    )


