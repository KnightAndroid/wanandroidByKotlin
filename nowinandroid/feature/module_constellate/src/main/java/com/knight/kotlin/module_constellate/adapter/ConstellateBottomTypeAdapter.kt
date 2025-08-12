package com.knight.kotlin.module_constellate.adapter

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseQuickAdapter
import com.core.library_common.util.dp2px
import com.knight.kotlin.module_constellate.R
import com.knight.kotlin.module_constellate.adapter.ConstellateTypeAdapter.VH
import com.knight.kotlin.module_constellate.databinding.ConstellateBottomTypeItemBinding

import com.knight.kotlin.module_constellate.entity.ConstellateTypeEntity

/**
 * @Description
 * @Author knight 底部弹窗星座选择适配器
 * @Time 2025/8/12 20:54
 *
 */

class ConstellateBottomTypeAdapter : BaseQuickAdapter<ConstellateTypeEntity, ConstellateBottomTypeAdapter.VH>() {
    private lateinit var typedArrayIcons: TypedArray

    private var itemSize = 0
    class VH(
        parent: ViewGroup,
        val binding: ConstellateBottomTypeItemBinding = ConstellateBottomTypeItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ),
    ) : RecyclerView.ViewHolder(binding.root)

    fun setItemSize(itemSize:Int) {
        this.itemSize = itemSize
    }

    fun setTypedArray(typedArrayIcons: TypedArray) {
        this.typedArrayIcons = typedArrayIcons
    }

    override fun onBindViewHolder(holder: VH, position: Int, item: ConstellateTypeEntity?) {
        item?.run {
            val drawable: Drawable? = typedArrayIcons.getDrawable(position) // 'index' 将是你的动态索引
            holder.binding.constellateBottomTypeHeadIv.setImageDrawable(drawable)
            holder.binding.constellateBottomNameTv.text = name
            holder.binding.tvConstellateStartDate.text = date.split("-").get(0)
            holder.binding.tvConstellateEndDate.text = date.split("-").get(1)


            val params = ConstraintLayout.LayoutParams(
                itemSize - 20.dp2px(),
                itemSize - 20.dp2px() // 宽高一样
            )
            params.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
            params.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID

            holder.binding.constellateBottomHeadRl.layoutParams = params
            holder.binding.constellateBottomHeadRl.setBackgroundResource(R.drawable.constellate_bottom_default)
        }
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH {
        val holder = VH(parent)
        // 设置 itemView 宽高
        holder.itemView.layoutParams = RecyclerView.LayoutParams(
            itemSize,
            itemSize+26.dp2px()// 宽高一样
        )
        return holder
    }


}