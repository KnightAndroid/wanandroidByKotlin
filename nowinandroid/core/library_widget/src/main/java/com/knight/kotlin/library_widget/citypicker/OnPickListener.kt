package com.knight.kotlin.library_widget.citypicker


/**
 * @author created by luguian
 * @organize
 * @Date 2025/4/17 10:59
 * @descript:
 */
interface OnPickListener {

    fun onPick(position: Int, data: com.knight.kotlin.library_database.entity.CityBean)
    fun onLocate()
    fun onCancel()
}