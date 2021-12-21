package com.knight.kotlin.module_welcome.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Author:Knight
 * Time:2021/12/21 17:37
 * Description:AppThemeBean
 */
@Parcelize
data class AppThemeBean(
   val gray:Boolean,
   val themeColor:String,
   val forceTheme:Boolean
): Parcelable
