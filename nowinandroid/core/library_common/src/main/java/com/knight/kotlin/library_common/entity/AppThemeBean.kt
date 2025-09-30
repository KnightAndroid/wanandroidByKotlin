package com.knight.kotlin.library_common.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Author:Knight
 * Time:2021/12/21 17:37
 * Description:AppThemeBean
 */
@Parcelize
data class AppThemeBean(
   val gray:Boolean, //是否灰色
   var themeColor:String, //主题色
   val forceTheme:Boolean,//是否强制颜色
   val usePicture:Boolean,//是否使用图片
   val pictureUrl:String //图片Url
): Parcelable