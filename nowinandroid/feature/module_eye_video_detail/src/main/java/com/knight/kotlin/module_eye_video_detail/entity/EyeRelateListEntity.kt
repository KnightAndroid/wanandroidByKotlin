package com.knight.kotlin.module_eye_video_detail.entity

import android.os.Parcelable
import com.core.library_base.entity.EyeItemEntity
import kotlinx.parcelize.Parcelize

/**
 * Author:Knight
 * Time:2024/7/26 16:16
 * Description:EyeRelateListEntity
 */
@Parcelize
data class EyeRelateListEntity (
    val itemList: MutableList<EyeItemEntity>,
    val nextPageUrl: String?,
    val count:Int = 0,
    val total:Int = 0,
    val errorCode:Int,
    val errorMessage:String
): Parcelable