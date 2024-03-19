package com.knight.kotlin.module_video.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Author:Knight
 * Time:2024/3/19 15:22
 * Description:VideoCommentList
 */
@Parcelize
data class VideoCommentList (
    val count:Long,
    val comments :MutableList<VideoComment>
): Parcelable