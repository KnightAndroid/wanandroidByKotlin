package com.knight.kotlin.module_eye_discover.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


/**
 * @author created by luguian
 * @organize
 * @Date 2024/8/6 10:41
 * @descript:发现广告
 */

data class EyeDiscoverTopBannerEntity(
  val type:String,
  val data:EyeDiscoverTopBannerListData,
  val tag:String,
  val id:Int,
  val adIndex:Int
):BaseEyeDiscoverEntity()



@Parcelize
data class EyeDiscoverTopBannerListData (
    val dataType:String,
    val count:Int,
    val itemList:MutableList<EyeDiscoverTopBannerItemListData>
) : Parcelable




@Parcelize
data class EyeDiscoverTopBannerItemListData(
    val type:String,
    val data:EyeDiscoverTopBannerData,
    val tag:String,
    val id:Int,
    val adIndex:Int
): Parcelable

@Parcelize
data class EyeDiscoverTopBannerData(
    val actionUrl: String,
    val adTrack: List<String>,
    val autoPlay: Boolean,
    val dataType: String,
    val description: String,
    val header: Header,
    val id: Int,
    val image: String,
    val label: Label,
    val labelList: List<String>,
    val shade: Boolean,
    val title: String
): Parcelable
@Parcelize
data class Header(
    val actionUrl: String?,
    val cover: String?,
    val description: String?,
    val font: String?,
    val icon: String?,
    val id: Int,
    val label: String?,
    val labelList: String?,
    val rightText: String?,
    val subTitle: String?,
    val subTitleFont: String,
    val textAlign: String,
    val title: String?
): Parcelable
@Parcelize
data class Label(
    val card: String,
    val detail: String?,
    val text: String
): Parcelable
