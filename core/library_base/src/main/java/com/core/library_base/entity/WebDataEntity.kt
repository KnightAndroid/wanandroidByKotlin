package com.core.library_base.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Author:Knight
 * Time:2022/1/12 16:18
 * Description:WebDataEntity
 */
@Parcelize
data class WebDataEntity (
      val webUrl:String,
      val title:String,
      val articleId:Int,
      var isCollect:Boolean,
      val envelopePic:String,
      val articledesc:String,
      val chapterName:String,
      val author:String
) : Parcelable