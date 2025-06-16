package com.knight.kotlin.library_widget.citypicker


/**
 * @author created by luguian
 * @organize
 * @Date 2025/4/17 14:12
 * @descript:城市类型
 */
enum class CityEnum(val type: Int) {
    LOCATION(0),//定位
    HOT(1),//热门
    NORMAL(2) //普通
}