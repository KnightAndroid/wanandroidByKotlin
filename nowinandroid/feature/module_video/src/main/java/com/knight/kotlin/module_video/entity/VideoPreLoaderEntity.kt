package com.knight.kotlin.module_video.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Author:Knight
 * Time:2024/4/7 10:29
 * Description:VideoPreLoaderEntity
 */

@Parcelize
data class VideoPreLoaderEntity (
    val originalUrl:String,
    val proxyUrl:String,
    val preLoadBytes:Long = 1024*1024*2
): Parcelable
