package com.knight.kotlin.module_eye_daily.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Author:Knight
 * Time:2024/4/29 10:36
 * Description:DailyListEntity
 */
@Parcelize
data class EyeDailyListEntity(
    val itemList: MutableList<EyeDailyItemEntity>,
    val nextPageUrl: String,
    val errorCode:Int,
    val errorMessage:String
) : Parcelable

@Parcelize
data class EyeDailyItemEntity(
    val type: String,
    val data: EyeDailyItemData,
) : Parcelable
@Parcelize
data class EyeDailyItemData(
    val text: String,
    val content: EyeItemEntity,
): Parcelable

@Parcelize
data class EyeItemEntity(
    val adIndex: Int,
    val `data`: EyeData,
    val id: Int,
    val type: String,
    var itemType: Int
): Parcelable

@Parcelize
data class EyeData(
    val actionUrl: String,
    val ad: Boolean,
    val author: EyeAuthorEntity?,
    val owner: EyeOwner?,
    val autoPlay: Boolean,
    val category: String,
    val collected: Boolean,
    val consumption: EyeConsumption,
    val cover: EyeCover?,
    val dataType: String,
    val date: Long,
    val description: String,
    val text: String,
    val descriptionEditor: String,
    val duration: Int,
    val header: EyeHeader,
    val id: Int,
    val idx: Int,
    val ifLimitVideo: Boolean,
    val image: String,
    val library: String,
    val playInfo: List<EyePlayInfo>,
    val playUrl: String,
    val played: Boolean,
    val provider: EyeProvider,
    val reallyCollected: Boolean,
    val releaseTime: Long,
    val resourceType: String,
    val searchWeight: Int,
    val shade: Boolean,
    val tags: List<EyeTag>,
    val title: String,
    val type: String,
    val webUrl: EyeWebUrl,
    val itemList: List<EyeItemEntity>,
    val width: Int,
    val height: Int,
    val urls: List<String>,
): Parcelable

@Parcelize
data class EyeAuthorEntity(
    val approvedNotReadyVideoCount: Int,
    val description: String,
    val expert: Boolean,
    val follow: EyeFollow,
    val icon: String,
    val id: Int,
    val ifPgc: Boolean,
    val latestReleaseTime: Long,
    val link: String,
    val name: String,
    val recSort: Int,
    val shield: EyeShield,
    val videoNum: Int
): Parcelable

@Parcelize
data class EyeOwner(
    val actionUrl: String,
    val area: String,
    val avatar: String,
    val birthday: String,
    val city: String,
    val country: String,
    val cover: String,
    val description: String,
    val expert: Boolean,
    val followed: Boolean,
    val gender: String,
    val ifPgc: Boolean,
    val job: String,
    val library: String,
    val limitVideoOpen: Boolean,
    val nickname: String,
    val registDate: Long,
    val releaseDate: Long,
    val uid: Int,
    val university: String,
    val userType: String
): Parcelable

@Parcelize
data class EyeConsumption(
    val collectionCount: Int,
    val realCollectionCount: Int,
    val replyCount: Int,
    val shareCount: Int
): Parcelable
@Parcelize
data class EyeCover(
    val blurred: String,
    val detail: String,
    val feed: String,
    val homepage: String,
): Parcelable

@Parcelize
data class EyeHeader(
    val actionUrl: String,
    val description: String,
    val expert: Boolean,
    val issuerName: String,
    val icon: String,
    val iconType: String,
    val id: Int,
    val time: Long,
    val ifPgc: Boolean,
    val ifShowNotificationIcon: Boolean,
    val title: String,
    val uid: Int
): Parcelable
@Parcelize
data class EyePlayInfo(
    val height: Int,
    val name: String,
    val type: String,
    val url: String,
    val urlList: List<EyeUrl>,
    val width: Int
): Parcelable
@Parcelize
data class EyeProvider(
    val alias: String,
    val icon: String,
    val name: String
): Parcelable

@Parcelize
data class EyeTag(
    val actionUrl: String,
    val bgPicture: String,
    val communityIndex: Int,
    val desc: String,
    val haveReward: Boolean,
    val headerImage: String,
    val id: Int,
    val ifNewest: Boolean,
    val name: String,
    val tagRecType: String
): Parcelable
@Parcelize
data class EyeWebUrl(
    val forWeibo: String,
    val raw: String
): Parcelable

@Parcelize
data class EyeFollow(
    val followed: Boolean,
    val itemId: Int,
    val itemType: String
): Parcelable

@Parcelize
data class EyeShield(
    val itemId: Int,
    val itemType: String,
    val shielded: Boolean
): Parcelable

@Parcelize
data class EyeUrl(
    val name: String,
    val url: String
): Parcelable
