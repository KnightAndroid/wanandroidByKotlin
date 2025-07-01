package com.core.library_base.entity

import kotlinx.serialization.Serializable


/**
 * @author created by luguian
 * @organize
 * @Date 2025/1/23 16:37
 * @descript: 开眼通用作者实体
 */
@Serializable
data class EyeCommonAuthorEntity (
    val avatar: EyeCommonAvatar? = null,
    val description: String = "",
    val fans_count: Int = 0,
    val follow_count: Int = 0,
    val followed: Boolean = false,
    val link: String = "",
    val nick: String = "",
    val type: String = "",
    val uid: Int = 0

)