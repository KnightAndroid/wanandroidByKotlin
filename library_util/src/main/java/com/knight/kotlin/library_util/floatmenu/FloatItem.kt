package com.knight.kotlin.library_util.floatmenu

import android.graphics.Bitmap
import android.graphics.Color




/**
 * @author created by luguian
 * @organize
 * @Date 2025/4/11 14:56
 * @descript:
 */
class FloatItem {
    var title: String
    var titleColor: Int = Color.BLACK
    var bgColor: Int = Color.WHITE
    var icon: Bitmap
    var dotNum: String? = null

    constructor(title: String, titleColor: Int, bgColor: Int, icon: Bitmap, dotNum: String?) {
        this.title = title
        this.titleColor = titleColor
        this.bgColor = bgColor
        this.icon = icon
        this.dotNum = dotNum
    }


    constructor(title: String, titleColor: Int, bgColor: Int, bitmap: Bitmap) {
        this.title = title
        this.titleColor = titleColor
        this.bgColor = bgColor
        this.icon = bitmap
    }

    override fun equals(obj: Any?): Boolean {
        if (obj == null) {
            return false
        }
        if (obj === this) return true

        if (obj is FloatItem) {
            return obj.title == this.title
        } else {
            return false
        }
    }

    override fun hashCode(): Int {
        return title.hashCode()
    }

    override fun toString(): String {
        return "FloatItem{" +
                "title='" + title + '\'' +
                ", titleColor=" + titleColor +
                ", bgColor=" + bgColor +
                ", icon=" + icon +
                ", dotNum='" + dotNum + '\'' +
                '}'
    }
}