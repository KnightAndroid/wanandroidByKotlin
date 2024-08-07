package com.knight.kotlin.module_eye_discover.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


/**
 * @author created by luguian
 * @organize
 * @Date 2024/8/7 17:44
 * @descript:发现BriefCard数据
 */

data class EyeDiscoverBriefCardEntity(
    val adIndex: Int,
    val data: EyeDiscoverBriefCardData,
    val id: Int,
    val tag: String,
    val trackingData: String,
    val type: String
):BaseEyeDiscoverEntity()
@Parcelize
data class EyeDiscoverBriefCardData(
    val actionUrl: String,
    val adTrack: String?,
    val dataType: String,
    val description: String,
    val expert: Boolean,
    val follow: FollowEntity,
    val haveReward: Boolean,
    val icon: String,
    val iconType: String,
    val id: Int,
    val ifNewest: Boolean,
    val ifPgc: Boolean,
    val ifShowNotificationIcon: Boolean,
    val medalIcon: Boolean,
    val newestEndTime: String?,
    val subTitle: String?,
    val switchStatus: Boolean,
    val title: String,
    val uid: Int
): Parcelable

