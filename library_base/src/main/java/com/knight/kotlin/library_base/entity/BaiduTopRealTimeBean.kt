package com.knight.kotlin.library_base.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


/**
 * @author created by luguian
 * @organize
 * @Date 2024/9/4 10:34
 * @descript:百度热搜
 */
@Parcelize
data class BaiduTopRealTimeBean (
    val success:Boolean,
    val data : BaiduCardDataBean,
    val error: BaiduErrorMessage
): Parcelable

@Parcelize
data class BaiduErrorMessage (
    val code:Int,
    val message:String
): Parcelable



@Parcelize
data class BaiduMessage(
    val code:Int,
    val message:String
) : Parcelable
@Parcelize
data class BaiduCardDataBean(
    val cards: MutableList<BaiduCardBean>,
    val curBoardName: String,
    val logid: String,
    val platform: String,
    val tabBoard: List<BaiduTabBoard>,
    val tag: List<BaiduTag>
): Parcelable
@Parcelize
data class BaiduCardBean(
    val component: String,
    val content: MutableList<BaiduContent>,
    val more: Int,
    val moreAppUrl: String,
    val moreUrl: String,
    val text: String,
    val topContent: List<BaiduTopContent>,
    val typeName: String,
    val updateTime: String
): Parcelable
@Parcelize
data class BaiduTabBoard(
    val index: Int,
    val text: String,
    val typeName: String
): Parcelable
@Parcelize
data class BaiduTag(
    val content: List<String>,
    val curIndex: Int,
    val text: String,
    val typeName: String
): Parcelable
@Parcelize
data class BaiduContent(
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
data class BaiduTopContent(
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
