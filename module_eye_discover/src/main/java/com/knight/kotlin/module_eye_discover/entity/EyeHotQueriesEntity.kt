package com.knight.kotlin.module_eye_discover.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


/**
 * @author created by luguian
 * @organize
 * @Date 2024/12/11 11:17
 * @descript:热词搜索
 */
@Parcelize
data class EyeHotQueriesEntity (
    val item_list: List<String>? = null
): Parcelable