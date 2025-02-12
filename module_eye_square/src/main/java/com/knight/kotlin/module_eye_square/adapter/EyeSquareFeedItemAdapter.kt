package com.knight.kotlin.module_eye_square.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseQuickAdapter
import com.google.android.material.chip.Chip
import com.google.android.material.shape.CornerFamily
import com.knight.kotlin.library_base.entity.eye_type.EyeFeedItemDetail
import com.knight.kotlin.library_base.ktx.screenWidth
import com.knight.kotlin.module_eye_square.R
import com.knight.kotlin.module_eye_square.databinding.EyeSquareFeedDetailItemBinding


/**
 * @author created by luguian
 * @organize
 * @Date 2025/2/12 16:08
 * @descript:信息流适配器
 */
class EyeSquareFeedItemAdapter : BaseQuickAdapter<EyeFeedItemDetail, EyeSquareFeedItemAdapter.VH>() {

    class VH(
        parent: ViewGroup,
        val binding: EyeSquareFeedDetailItemBinding = EyeSquareFeedDetailItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ),
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: VH, position: Int, item: EyeFeedItemDetail?) {
        holder.binding.viewModel = item

        holder.binding.ivFeedItemCover.apply {
            layoutParams.width = context.screenWidth
            layoutParams.height =
                layoutParams.width.div(item?.video?.cover?.img_info?.scale ?: 1.0).toInt()
        }
        //动态禁用tint 代码动态清除 FAB 的 tint
        holder.binding.fbSquareCollect.imageTintList = null
        holder.binding.cgFeedItemTag.removeAllViews()
        item?.topics?.forEach {
            val assist = Chip(
               context
            )
            assist.text = it.title
            assist.chipIcon = ContextCompat.getDrawable(context, R.drawable.eye_square_feed_item_tag)


            val shapeAppearanceModel = assist.shapeAppearanceModel.toBuilder()
                .setAllCorners(CornerFamily.ROUNDED, 6f) // 设置所有角的圆角半径为 16dp
                .build()

            assist.shapeAppearanceModel = shapeAppearanceModel
            holder.binding.cgFeedItemTag.addView(assist)
        }

    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH {
        return VH(parent)
    }



}