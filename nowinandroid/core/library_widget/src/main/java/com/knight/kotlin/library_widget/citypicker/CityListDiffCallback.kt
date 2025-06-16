package com.knight.kotlin.library_widget.citypicker

import androidx.recyclerview.widget.DiffUtil
import com.knight.kotlin.library_widget.GroupCityListBean


/**
 * @author created by luguian
 * @organize 
 * @Date 2025/4/18 17:20
 * @descript:
 */
class CityListDiffCallback(
    private val oldList: List<GroupCityListBean>,
    private val newList: List<GroupCityListBean>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].group == newList[newItemPosition].group
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].city == newList[newItemPosition].city
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        if (oldItem.city != newItem.city) {
            return newItem.city // 返回改变的城市列表作为 payload
        }
        return null
    }
}