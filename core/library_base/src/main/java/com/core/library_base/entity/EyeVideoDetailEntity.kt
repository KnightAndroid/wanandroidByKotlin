package com.core.library_base.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


/**
 * @author created by luguian
 * @organize
 * @Date 2024/12/20 16:03
 * @descript:开眼视频实体模型(自己构造)
 */
@Parcelize
data class EyeVideoDetailEntity (
    val videoId:Long,
    val videoTitle:String,
    val videoUrl:String,
    val videoCategory:String,
    val latestReleaseTime:Long,//发布时间
    val videoDescription:String,
    val collectionCount:Long,
    val replyCount:Long,
    val shareCount:Long,
    val authorIcon:String,
    val authorName:String,
    val authorDescription:String,
    val videoCover:String
): Parcelable