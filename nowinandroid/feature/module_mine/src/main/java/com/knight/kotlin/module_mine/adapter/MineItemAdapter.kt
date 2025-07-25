package com.knight.kotlin.module_mine.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseQuickAdapter
import com.knight.kotlin.library_common.ktx.getUser
import com.knight.kotlin.library_common.util.LanguageFontSizeUtils
import com.knight.kotlin.module_mine.databinding.MineItemBinding
import com.knight.kotlin.module_mine.entity.MineItemEntity
import java.util.Locale

/**
 * Author:Knight
 * Time:2024/4/23 15:18
 * Description:MineItemAdapter
 */
class MineItemAdapter : BaseQuickAdapter<MineItemEntity, MineItemAdapter.VH>(
 ) {

    class VH(
        parent: ViewGroup,
        val binding: MineItemBinding = MineItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ),
    ) : RecyclerView.ViewHolder(binding.root)

    /**
     * 判断当前系统是英文还是中文
     *
     *
     * @param mineItem
     * @return
     */
    private fun getLanguageMode(mineItem: MineItemEntity):String {
        val locale = LanguageFontSizeUtils.getLanguageLocale()
        return if ( locale.language == Locale.SIMPLIFIED_CHINESE.language) {
            mineItem.name_zh
        } else {
            mineItem.name_en
        }
    }

    override fun onBindViewHolder(holder: VH, position: Int, item: MineItemEntity?) {
        item?.run {
            holder.binding.mineTvItemName.setText(getLanguageMode(item))
            if (id == 2) {
                holder.binding.mineTvTip.visibility = View.VISIBLE
                getUser()?.run {
                    holder.binding.mineTvTip.setText(coinCount.toString())
                } ?: {
                    holder.binding.mineTvTip.setText( "0")
                }

            } else {
                holder.binding.mineTvTip.visibility = View.GONE
            }
        }
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH {
        return VH(parent)
    }
}