package com.knight.kotlin.library_base.config

import com.knight.kotlin.library_base.entity.UserInfoEntity

/**
 * Author:Knight
 * Time:2021/12/24 10:32
 * Description:Appconfig
 * 一些App的配置
 */
object Appconfig {

     /**
      * 是否变灰
      */
     var gray = false

     /**
      * APP主题颜色
      */
     var appThemeColor = "#55aff4"

     /**
      * 文章类型：文字文章
      *
      */
     const val ARTICLE_TEXT_TYPE = 0

     /**
      * 文章类型：图片文章
      *
      */
     const val ARTICLE_PICTURE_TYPE = 1;

     const val IMAGE_TRANSITION_NAME = "transitionImage"
     const val TEXT_AUTHOR_NAME = "authorName"
     const val TEXT_CHAPTERNAME_NAME = "chapterName"

     /**
      *
      * 字段搜索名称
      */
     var search_keyword = ""

     /**
      *
      * 用户信息
      */
     var user:UserInfoEntity?=null

}