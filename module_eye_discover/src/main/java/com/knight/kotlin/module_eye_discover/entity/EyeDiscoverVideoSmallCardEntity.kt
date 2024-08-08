package com.knight.kotlin.module_eye_discover.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


/**
 * @author created by luguian
 * @organize
 * @Date 2024/8/7 17:09
 * @descript:发现小视频卡片实体
 */

data class EyeDiscoverVideoSmallCardEntity(
    val adIndex: Int,
    val data: EyeDiscoverVideoSmallCardData,
    val id: Int,
    val tag: String?,
    val trackingData: String?,
    val type: String
): BaseEyeDiscoverEntity()
@Parcelize
data class EyeDiscoverVideoSmallCardData(
    val ad: Boolean,
    val adTrack: List<String?>,
    val author: Author,
    val brandWebsiteInfo: String?,
    val campaign: String?,
    val category: String,
    val collected: Boolean,
    val consumption: Consumption,
    val cover: Cover,
    val dataType: String,
    val date: Long,
    val description: String,
    val descriptionEditor: String,
    val descriptionPgc: String,
    val duration: Int,
    val favoriteAdTrack: String?,
    val id: Int,
    val idx: Int,
    val ifLimitVideo: Boolean,
    val label: String?,
    val labelList: List<String?>,
    val lastViewTime: String?,
    val library: String,
    val playInfo: List<EyeDiscoverPlayInfoEntity?>,
    val playUrl: String,
    val played: Boolean,
    val playlists: String?,
    val promotion: String?,
    val provider: Provider,
    val reallyCollected: Boolean,
    val recallSource: String?,
    val recall_source: String?,
    val releaseTime: Long,
    val remark: String,
    val resourceType: String?,
    val searchWeight: Int,
    val shareAdTrack: String?,
    val slogan: String,
    val src: String?,
    val subtitles: List<String?>,
    val tags: List<Tag>,
    val thumbPlayUrl: String,
    val title: String,
    val titlePgc: String,
    val type: String,
    val videoPosterBean: EyeDiscoverVideoPosterEntity,
    val waterMarks: String?,
    val webAdTrack: String?,
    val webUrl: WebUrl
): Parcelable
@Parcelize
data class Author(
    val adTrack: String,
    val approvedNotReadyVideoCount: Int,
    val description: String,
    val expert: Boolean,
    val follow: FollowEntity,
    val icon: String,
    val id: Int,
    val ifPgc: Boolean,
    val latestReleaseTime: Long,
    val link: String,
    val name: String,
    val recSort: Int,
    val shield: Shield,
    val videoNum: Int
): Parcelable
@Parcelize
data class Consumption(
    val collectionCount: Int,
    val realCollectionCount: Int,
    val replyCount: Int,
    val shareCount: Int
): Parcelable
@Parcelize
data class Cover(
    val blurred: String,
    val detail: String,
    val feed: String,
    val homepage: String,
    val sharing: String
): Parcelable
@Parcelize
data class Provider(
    val alias: String,
    val icon: String,
    val name: String
): Parcelable
@Parcelize
data class Tag(
    val actionUrl: String,
    val adTrack: String?,
    val bgPicture: String,
    val childTagIdList: String?,
    val childTagList: String?,
    val communityIndex: Int,
    val desc: String?,
    val haveReward: Boolean,
    val headerImage: String,
    val id: Int,
    val ifNewest: Boolean,
    val name: String,
    val newestEndTime: String?,
    val tagRecType: String
): Parcelable
@Parcelize
data class WebUrl(
    val forWeibo: String,
    val raw: String
): Parcelable
@Parcelize
data class FollowEntity(
    val followed: Boolean,
    val itemId: Int,
    val itemType: String
): Parcelable
@Parcelize
data class Shield(
    val itemId: Int,
    val itemType: String,
    val shielded: Boolean
): Parcelable

@Parcelize
data class EyeDiscoverVideoPosterEntity(
    val fileSizeStr: String,
    val scale: Double,
    val url: String
): Parcelable

@Parcelize
data class EyeDiscoverPlayInfoEntity(
    val height: Int,
    val name: String,
    val type: String,
    val url: String,
    val urlList: List<EyeDiscoverPlayUrl>,
    val width: Int
): Parcelable
@Parcelize
data class EyeDiscoverPlayUrl(
    val name: String,
    val size: Int,
    val url: String
): Parcelable


