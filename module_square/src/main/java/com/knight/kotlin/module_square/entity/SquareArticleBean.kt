package com.knight.kotlin.module_square.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Author:Knight
 * Time:2022/4/26 15:55
 * Description:SquareArticleBean
 */
@Parcelize
data class SquareArticleBean(
    val apkLink:String,
    val audit:Int,
    val author:String,
    val canEdit:Boolean,
    val chapterId:Int,
    val chapterName:String,
    var collect:Boolean,
    val courseId:Int,
    val desc:String,
    val descMd:String,
    val envelopePic:String,
    val fresh:Boolean,
    val host:String,
    val id:Int,
    val link:String,
    val niceDate:String,
    val niceShareDate:String,
    val origin:String,
    val prefix:String,
    val projectLink:String,
    val publishTime:Long,
    val realSuperChapterId:Int,
    val selfVisible:Int,
    val shareDate:Long,
    val shareUser:String,
    val superChapterId:Int,
    val superChapterName:String,
    val title:String,
    val type:Int,
    val userId:Int,
    val visible:Int,
    val zan:Int
) : Parcelable