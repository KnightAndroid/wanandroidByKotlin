package com.core.library_base.enum

import android.content.Context
import com.core.library_base.util.ResArrayUtils
import com.knight.kotlin.library_base.R



/**
 * @author created by luguian
 * @organize
 * @Date 2025/2/26 14:29
 * @descript:背景动画模式
 */
enum class BackgroundAnimationMode(
    override val id: String,
) : com.core.library_base.enum.BaseEnum {

    SYSTEM("system"),
    ENABLED("enabled"),
    DISABLED("disabled"),
    ;

    companion object {

        fun getInstance(
            value: String,
        ) = com.core.library_base.enum.BackgroundAnimationMode.entries.firstOrNull {
            it.id == value
        } ?: com.core.library_base.enum.BackgroundAnimationMode.SYSTEM
    }

    override val valueArrayId = R.array.base_background_animation_values
    override val nameArrayId = R.array.base_background_animation

    override fun getName(context: Context) = ResArrayUtils.getName(context, this)
}