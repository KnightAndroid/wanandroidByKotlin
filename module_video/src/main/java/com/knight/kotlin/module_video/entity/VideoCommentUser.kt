package com.knight.kotlin.module_video.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Author:Knight
 * Time:2024/3/19 14:43
 * Description:VideoCommentUser
 */
@Parcelize
data class VideoCommentUser(
    val userId:Long,
    val nickname:String,
    val userAvatar:String
): Parcelable
