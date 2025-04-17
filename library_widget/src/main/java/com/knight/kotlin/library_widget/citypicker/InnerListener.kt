package com.knight.kotlin.library_widget.citypicker


/**
 * @author created by luguian
 * @organize
 * @Date 2025/4/17 14:56
 * @descript:
 */
interface InnerListener {
    fun dismiss(position: Int, data: CityBean)
    fun locate()
}