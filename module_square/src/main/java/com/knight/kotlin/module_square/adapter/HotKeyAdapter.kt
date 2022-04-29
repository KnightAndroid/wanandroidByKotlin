package com.knight.kotlin.module_square.adapter

import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexboxLayoutManager
import com.knight.kotlin.library_base.entity.SearchHotKeyEntity
import com.knight.kotlin.library_base.util.CacheUtils
import com.knight.kotlin.library_base.util.dp2px
import com.knight.kotlin.module_square.R

/**
 * Author:Knight
 * Time:2022/4/26 15:20
 * Description:HotKeyAdapter
 */
class HotKeyAdapter(data:MutableList<SearchHotKeyEntity>):
    BaseQuickAdapter<SearchHotKeyEntity, BaseViewHolder>( R.layout.square_hotsearch,data) {


    override fun convert(holder: BaseViewHolder, item: SearchHotKeyEntity) {
        item.run {
            holder.setText(R.id.square_tv_hotkey, name)
            holder.setTextColor(R.id.square_tv_hotkey, CacheUtils.getThemeColor())
            val gradientDrawable = GradientDrawable()
            gradientDrawable.shape = GradientDrawable.RECTANGLE
            gradientDrawable.setStroke(2, CacheUtils.getThemeColor())
            gradientDrawable.cornerRadius = 6.dp2px().toFloat()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                holder.getView<View>(R.id.square_tv_hotkey).setBackground(gradientDrawable)
            } else {
                holder.getView<View>(R.id.square_tv_hotkey)
                    .setBackgroundDrawable(gradientDrawable)
            }
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val lp = holder.getView<View>(R.id.square_tv_hotkey).layoutParams
        if (lp is FlexboxLayoutManager.LayoutParams) {
            val flexboxLp: FlexboxLayoutManager.LayoutParams =
                holder.getView<View>(R.id.square_tv_hotkey).layoutParams as FlexboxLayoutManager.LayoutParams
            flexboxLp.flexGrow = 1.0f
            flexboxLp.alignSelf = AlignItems.FLEX_END
        }
    }
}