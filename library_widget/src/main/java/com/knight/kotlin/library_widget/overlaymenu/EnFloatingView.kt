package com.knight.kotlin.library_widget.overlaymenu

import android.content.Context
import androidx.annotation.LayoutRes


/**
 * @author created by luguian
 * @organize
 * @Date 2025/4/23 14:51
 * @descript:
 */
class EnFloatingView @JvmOverloads constructor(context: Context, @LayoutRes resource: Int) :
    FloatingMagnetView(context, null) {


    init {
        inflate(context, resource, this)

    }

}