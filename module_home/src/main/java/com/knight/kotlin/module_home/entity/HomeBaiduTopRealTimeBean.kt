package com.knight.kotlin.module_home.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


/**
 * @author created by luguian
 * @organize
 * @Date 2024/9/4 10:34
 * @descript:百度热搜
 */
@Parcelize
data class HomeBaiduTopRealTimeBean (
    val success:Boolean,
    val data :HomeBaiduCardDataBean,
    val error:HomeBaiduErrorMessage
): Parcelable

@Parcelize
data class HomeBaiduErrorMessage (
    val code:Int,
    val message:String
): Parcelable



@Parcelize
data class HomeBaiduMessage(
    val code:Int,
    val message:String
) : Parcelable
@Parcelize
data class HomeBaiduCardDataBean(
    val cards: List<HomeBaiduCardBean>,
    val curBoardName: String,
    val logid: String,
    val platform: String,
    val tabBoard: List<HomeBaiduTabBoard>,
    val tag: List<Tag>
): Parcelable
@Parcelize
data class HomeBaiduCardBean(
    val component: String,
    val content: List<HomeBaiduContent>,
    val more: Int,
    val moreAppUrl: String,
    val moreUrl: String,
    val text: String,
    val topContent: List<HomeBaiduTopContent>,
    val typeName: String,
    val updateTime: String
): Parcelable
@Parcelize
data class HomeBaiduTabBoard(
    val index: Int,
    val text: String,
    val typeName: String
): Parcelable
@Parcelize
data class HomeBaiduTag(
    val content: List<String>,
    val curIndex: Int,
    val text: String,
    val typeName: String
): Parcelable
@Parcelize
data class HomeBaiduContent(
    val appUrl: String,
    val desc: String,
    val hotChange: String,
    val hotScore: String,
    val hotTag: String,
    val hotTagImg: String,
    val img: String,
    val index: Int,
    val indexUrl: String,
    val query: String,
    val rawUrl: String,
    val show: List<String>,
    val url: String,
    val word: String
): Parcelable
@Parcelize
data class HomeBaiduTopContent(
    val appUrl: String,
    val desc: String,
    val hotChange: String,
    val hotScore: String,
    val hotTag: String,
    val img: String,
    val index: Int,
    val indexUrl: String,
    val query: String,
    val rawUrl: String,
    val show: List<String>,
    val url: String,
    val word: String
): Parcelable
