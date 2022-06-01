package com.knight.kotlin.module_set.adapter

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.knight.kotlin.library_base.util.CacheUtils
import com.knight.kotlin.library_base.util.LanguageFontSizeUtils
import com.knight.kotlin.module_set.R
import com.knight.kotlin.module_set.entity.LanguageEntity

/**
 * Author:Knight
 * Time:2022/5/31 16:30
 * Description:SelectLanguageAdapter
 */
class SelectLanguageAdapter(data:MutableList<LanguageEntity>):
    BaseQuickAdapter<LanguageEntity, BaseViewHolder>( R.layout.set_language_item,data) {
    override fun convert(holder: BaseViewHolder, item: LanguageEntity) {
        item.run {
            if (LanguageFontSizeUtils.isChinese()) {
                holder.setText(R.id.set_tv_language_name,languageName)
            } else {
                holder.setText(R.id.set_tv_language_name,englishName)
            }

            if (select) {
                holder.setVisible(R.id.set_iv_select_language,true)
                (holder.getView(R.id.set_iv_select_language) as ImageView).setColorFilter(CacheUtils.getThemeColor())
            } else {
                holder.setVisible(R.id.set_iv_select_language,false)
            }

            if (showLine) {
                holder.setVisible(R.id.set_tv_language_line,true)
            } else {
                holder.setVisible(R.id.set_tv_language_line,false)
            }
        }

    }
}