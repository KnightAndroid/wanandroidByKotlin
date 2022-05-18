package com.knight.kotlin.library_widget.floatmenu

import android.view.View

/**
 * Author:Knight
 * Time:2022/5/18 9:49
 * Description:MenuItem
 */
class MenuItem {
    private var item: String? = null
    private var itemResId = View.NO_ID


    fun getItem(): String? {
        return item
    }

    fun setItem(item: String?) {
        this.item = item
    }

    fun getItemResId(): Int {
        return itemResId
    }

    fun setItemResId(itemResId: Int) {
        this.itemResId = itemResId
    }
}