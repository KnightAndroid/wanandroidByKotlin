package com.core.library_base.entity.eye_type

import com.core.library_base.entity.EyeCommonAuthor
import com.core.library_base.entity.EyeCommonConsumption
import com.core.library_base.entity.EyeCommonTopic


import kotlinx.serialization.Serializable


/**
 * @author created by luguian
 * @organize
 * @Date 2025/2/12 14:59
 * @descript:信息流
 */
@Serializable
data class EyeFeedItemDetail (
    val item_id: String = "",
    val publish_time: String = "",
    val raw_publish_time: String = "",
    val text: String = "",
    val location: EyeLocation? = null,
    val topics: List<EyeCommonTopic>? = null,
    val images:List<String>? = null,
    val video: EyeVideo? = null,
    val author: EyeCommonAuthor? = null,
    val liked: Boolean = false,
    val collected: Boolean = false,
    val is_mine: Boolean = false,
    val show_follow_btn: Boolean = false,
    val consumption: EyeCommonConsumption? = null,
    val resource_id: String = "",
    val resource_type: String = "",
    val private_msg_link: String = "",
    val comment_extra_tracking_fields:EyeExtraTrackingFields,
    val show_lock_icon:Boolean = false,
    val show_more_btn:Boolean =  false,
    val real_location:String = "",
    val more_option:List<EyeMoreOption>?=null
    )


@Serializable
data class EyeExtraTrackingFields(
    val resource_id: String = "",
    val resource_type: String = "",
)


@Serializable
data class EyeMoreOption(
    val title: String = "",
    val type: String = "",
)