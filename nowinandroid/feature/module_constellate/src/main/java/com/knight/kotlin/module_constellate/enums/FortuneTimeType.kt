package com.knight.kotlin.module_constellate.enums


/**
 * @author created by luguian
 * @organize
 * @Date 2025/9/11 15:21
 * @descript:
 */
enum class FortuneTimeType(val label: String) {

    DAY("日"),
    WEEK("周"),
    MONTH("月"),
    YEAR("年");

    override fun toString(): String {
        return label
    }
}