package com.knight.kotlin.module_eye_discover.okplaystd

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


/**
 * @author created by luguian
 * @organize
 * @Date 2024/8/20 16:20
 * @descript:
 */
@Parcelize
data class ViewAttr(
    val x: Int,
    val y: Int,
    val width: Int,
    val height: Int
) : Parcelable