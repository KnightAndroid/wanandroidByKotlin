package com.knight.kotlin.module_set.adapter

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.knight.kotlin.library_base.util.CacheUtils
import com.knight.kotlin.library_base.util.LanguageFontSizeUtils
import com.knight.kotlin.module_set.R
import com.knight.kotlin.module_set.entity.DarkSelectEntity

/**
 * Author:Knight
 * Time:2022/5/20 17:29
 * Description:SelectDarkModeAdapter
 */
class SelectDarkModeAdapter (data:MutableList<DarkSelectEntity>):
    BaseQuickAdapter<DarkSelectEntity, BaseViewHolder>( R.layout.set_select_dark_item,data)  {
    override fun convert(holder: BaseViewHolder, item: DarkSelectEntity) {
        item.run {
            if (LanguageFontSizeUtils.isChinese()) {
                holder.setText(R.id.set_tv_dark_name,name)
            } else {
                holder.setText(R.id.set_tv_dark_name,englishNamn)
            }

            if (select) {
                holder.setVisible(R.id.set_iv_select_darkmodel,true)
                (holder.getView(R.id.set_iv_select_darkmodel) as ImageView).setColorFilter(CacheUtils.getThemeColor())
            } else {
                holder.setVisible(R.id.set_iv_select_darkmodel,false)
            }

            if (showLine) {
                holder.setVisible(R.id.set_tv_darkmodel_line,true)
            } else {
                holder.setVisible(R.id.set_tv_darkmodel_line,false)
            }
        }
    }
}