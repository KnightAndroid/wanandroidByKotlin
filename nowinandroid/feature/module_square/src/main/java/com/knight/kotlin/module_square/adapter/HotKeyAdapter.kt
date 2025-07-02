package com.knight.kotlin.module_square.adapter

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseQuickAdapter
import com.knight.kotlin.library_base.utils.CacheUtils
import com.core.library_base.util.dp2px
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexboxLayoutManager
import com.knight.kotlin.library_base.entity.SearchHotKeyEntity
import com.knight.kotlin.module_square.databinding.SquareHotsearchBinding

/**
 * Author:Knight
 * Time:2022/4/26 15:20
 * Description:HotKeyAdapter
 */
class HotKeyAdapter:
    BaseQuickAdapter<SearchHotKeyEntity, HotKeyAdapter.VH>(){


    class VH(
        parent: ViewGroup,
        val binding: SquareHotsearchBinding = SquareHotsearchBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ),
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: VH, position: Int, item: SearchHotKeyEntity?) {
        item?.run {
            holder.binding.squareTvHotkey.setText( name)
            holder.binding.squareTvHotkey.setTextColor( CacheUtils.getThemeColor())
            val gradientDrawable = GradientDrawable()
            gradientDrawable.shape = GradientDrawable.RECTANGLE
            gradientDrawable.setStroke(2, CacheUtils.getThemeColor())
            gradientDrawable.cornerRadius = 6.dp2px().toFloat()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                holder.binding.squareTvHotkey.setBackground(gradientDrawable)
            } else {
                holder.binding.squareTvHotkey
                    .setBackgroundDrawable(gradientDrawable)
            }
        }
    }



    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH {
        val vh= VH(parent)
        val lp = vh.binding.squareTvHotkey.layoutParams
        if (lp is FlexboxLayoutManager.LayoutParams) {
            val flexboxLp: FlexboxLayoutManager.LayoutParams =
                vh.binding.squareTvHotkey.layoutParams as FlexboxLayoutManager.LayoutParams
            flexboxLp.flexGrow = 1.0f
            flexboxLp.alignSelf = AlignItems.FLEX_END
        }
        return vh
    }
}