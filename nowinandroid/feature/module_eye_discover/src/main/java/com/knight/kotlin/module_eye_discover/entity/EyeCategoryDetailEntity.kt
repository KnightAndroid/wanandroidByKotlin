package com.knight.kotlin.module_eye_discover.entity

import android.os.Parcelable
import com.knight.kotlin.library_base.entity.EyeItemEntity
import kotlinx.parcelize.Parcelize


/**
 * @author created by luguian
 * @organize
 * @Date 2024/8/13 16:30
 * @descript:分类详细实体
 */
@Parcelize
data class EyeCategoryDetailEntity (
    val total:Int,
    val count: Int,
    val date: Long,
    val itemList: MutableList<EyeItemEntity>,
    val publishTime: Long,
    val releaseTime: Long,
    val type: String,
    val nextPageUrl: String,
    val errorCode:Int,
    val errorMessage:String
): Parcelable