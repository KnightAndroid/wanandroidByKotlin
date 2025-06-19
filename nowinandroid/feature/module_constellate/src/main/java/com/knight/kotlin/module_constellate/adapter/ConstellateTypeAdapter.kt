package com.knight.kotlin.module_constellate.adapter

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseQuickAdapter
import com.knight.kotlin.module_constellate.databinding.ConstellateTypeItemBinding
import com.knight.kotlin.module_constellate.entity.ConstellateTypeEntity


/**
 * @author created by luguian
 * @organize 
 * @Date 2025/6/19 10:19
 * @descript:星座类型适配器
 */
class ConstellateTypeAdapter():BaseQuickAdapter<ConstellateTypeEntity,ConstellateTypeAdapter.VH>() {

    private lateinit var typedArrayIcons: TypedArray

    private var itemSize = 0


    fun setTypedArray(typedArrayIcons: TypedArray) {
        this.typedArrayIcons = typedArrayIcons
    }

    fun setItemSize(itemSize:Int) {
        this.itemSize = itemSize
    }


    class VH(
        parent: ViewGroup,
        val binding: ConstellateTypeItemBinding = ConstellateTypeItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ),
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: VH, position: Int, item: ConstellateTypeEntity?) {
        item?.run {
            val drawable: Drawable? = typedArrayIcons.getDrawable(position) // 'index' 将是你的动态索引
            holder.binding.constellateTypeHeadIv.setImageDrawable(drawable)
            holder.binding.constellateNameTv.text = name
            holder.binding.constellateDateTv.text = date
        }

    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH {
        val holder = VH(parent)
        // 设置 itemView 宽高
        holder.itemView.layoutParams = RecyclerView.LayoutParams(
            itemSize,
            itemSize // 宽高一样
        )
        return holder
    }

}