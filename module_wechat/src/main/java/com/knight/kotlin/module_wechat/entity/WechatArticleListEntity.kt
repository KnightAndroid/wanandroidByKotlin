package com.knight.kotlin.module_wechat.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 *
 * @ProjectName:    wanandroid
 * @Package:        com.knight.kotlin.module_wechat.entity
 * @ClassName:      WechatArticleListEntity
 * @Description:    java类作用描述
 * @Author:         knight
 * @CreateDate:     2022/3/19 11:33 上午
 * @UpdateUser:     更新者
 * @UpdateDate:     2022/3/19 11:33 上午
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */

@Parcelize
data class WechatArticleListEntity(
    val curpage:Int,
    val offset:Int,
    val over:Boolean,
    val pageCount:Int,
    val size:Int,
    val total:Int,
    val datas:MutableList<WechatArticleEntity>
): Parcelable

@Parcelize
data class WechatArticleEntity(
    val apkLink:String,
    val audit:String,
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
    val tags:MutableList<TagsDTO>,
    val title:String,
    val type:Int,
    val userId:Int,
    val visible:Int,
    val zan:Int

): Parcelable


@Parcelize
data class TagsDTO(
    val name:String,
    val url:String

) : Parcelable