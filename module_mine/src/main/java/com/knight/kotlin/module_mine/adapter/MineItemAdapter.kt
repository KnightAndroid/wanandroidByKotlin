package com.knight.kotlin.module_mine.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.knight.kotlin.library_base.ktx.getUser
import com.knight.kotlin.library_base.util.LanguageFontSizeUtils
import com.knight.kotlin.module_mine.R
import com.knight.kotlin.module_mine.entity.MineItemEntity
import java.util.Locale

/**
 * Author:Knight
 * Time:2024/4/23 15:18
 * Description:MineItemAdapter
 */
class MineItemAdapter(data:MutableList<MineItemEntity>) : BaseQuickAdapter<MineItemEntity,BaseViewHolder>(
    R.layout.mine_item,data) {
    override fun convert(holder: BaseViewHolder, item: MineItemEntity) {
        item.run {
            holder.setText(R.id.mine_tv_item_name,getLanguageMode(item))
            if (id == 2) {
               holder.setVisible(R.id.mine_tv_tip,true)
               getUser()?.run {
                   holder.setText(R.id.mine_tv_tip, coinCount.toString())
               } ?: {
                   holder.setText(R.id.mine_tv_tip, "0")
               }

            } else {
                holder.setGone(R.id.mine_tv_tip,true)
            }
        }
    }

    /**
     * 判断当前系统是英文还是中文
     *
     *
     * @param mineItem
     * @return
     */
    private fun getLanguageMode(mineItem: MineItemEntity):String {
        val locale = LanguageFontSizeUtils.getSetLanguageLocale()
        return if ( locale.language == Locale.SIMPLIFIED_CHINESE.language) {
            mineItem.name_zh
        } else {
            mineItem.name_en
        }
    }
}