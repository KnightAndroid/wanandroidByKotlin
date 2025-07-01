package com.core.library_base.enum

import android.content.Context
import com.knight.kotlin.library_base.R
import com.knight.kotlin.library_base.util.ResArrayUtils


/**
 * @author created by luguian
 * @organize
 * @Date 2025/2/26 11:08
 * @descript:深色模型
 */
enum class DarkMode(
    override val id: String,
) : BaseEnum {

    SYSTEM("system"),
    LIGHT("light"),
    DARK("dark"),
    ;

    companion object {

        fun getInstance(
            value: String,
        ) = DarkMode.entries.firstOrNull {
            it.id == value
        } ?: SYSTEM
    }

    override val valueArrayId = R.array.base_dark_mode_values
    override val nameArrayId = R.array.base_dark_modes

    override fun getName(context: Context) = ResArrayUtils.getName(context, this)
}