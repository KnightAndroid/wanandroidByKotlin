package com.knight.kotlin.module_home.adapter

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseQuickAdapter
import com.knight.kotlin.library_base.util.CacheUtils
import com.knight.kotlin.library_base.util.dp2px
import com.knight.kotlin.library_widget.flowlayout.TagInfo
import com.knight.kotlin.module_home.databinding.HomeKnowledgelabelItemBinding

/**
 * Author:Knight
 * Time:2022/4/24 17:14
 * Description:KnowLedgeAdapter
 */
class KnowLedgeAdapter: BaseQuickAdapter<TagInfo, KnowLedgeAdapter.VH>() {

    // 自定义ViewHolder类
    class VH(
        parent: ViewGroup,
        val binding: HomeKnowledgelabelItemBinding = HomeKnowledgelabelItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ),
    ) : RecyclerView.ViewHolder(binding.root)


    private var isEdit = false

    fun setIsEdit(isEdit: Boolean) {
        this.isEdit = isEdit
        notifyDataSetChanged()
    }

    fun getIsEdit(): Boolean {
        return isEdit
    }

    override fun onBindViewHolder(holder: VH, position: Int, item: TagInfo?) {
        item?.run {
            holder.binding.homeTvKnowledge.setText( item.tagName)
            holder.binding.homeTvKnowledge.setTextColor(CacheUtils.getThemeColor())
            val gradientDrawable = GradientDrawable()
            gradientDrawable.shape = GradientDrawable.RECTANGLE
            gradientDrawable.setStroke(2, CacheUtils.getThemeColor())
            gradientDrawable.cornerRadius = 6.dp2px().toFloat()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                holder.binding.homeTvKnowledge.setBackground(gradientDrawable)
            } else {
                holder.binding.homeTvKnowledge.setBackgroundDrawable(gradientDrawable)
            }
            //如果是编辑状态
            if (isEdit) {
                holder.binding.homeIvMoreknowledgeDelete.visibility = View.VISIBLE
            } else {
                holder.binding.homeIvMoreknowledgeDelete.visibility = View.GONE
            }
        }

    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH {
        return VH(parent)
    }

}