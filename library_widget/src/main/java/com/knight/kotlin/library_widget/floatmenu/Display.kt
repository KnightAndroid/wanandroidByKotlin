package com.knight.kotlin.library_widget.floatmenu

import android.content.Context
import android.graphics.Point

/**
 * Author:Knight
 * Time:2022/5/18 9:49
 * Description:Display
 */
class Display {
    companion object {
        fun getScreenMetrics(context: Context): Point {
            val dm = context.resources.displayMetrics
            val w_screen = dm.widthPixels
            val h_screen = dm.heightPixels
            return Point(w_screen, h_screen)
        }
    }
}