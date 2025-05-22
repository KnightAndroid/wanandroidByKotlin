package com.knight.kotlin.module_home.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseQuickAdapter
import com.knight.kotlin.library_base.util.dp2px
import com.knight.kotlin.library_util.image.ImageLoader
import com.knight.kotlin.module_home.R
import com.knight.kotlin.module_home.databinding.HomeEyepetizerCategoryBinding
import com.knight.kotlin.module_home.entity.EyeCategoryBean

/**
 * Author:Knight
 * Time:2024/4/26 16:01
 * Description:EyepetizerCategoryAdapter
 */

//EyeCategoryBean
class EyepetizerCategoryAdapter:
    BaseQuickAdapter<EyeCategoryBean, EyepetizerCategoryAdapter.VH>() {

    private val colorArray = mutableListOf(
        android.R.color.holo_red_dark,
        android.R.color.holo_green_dark,
        android.R.color.holo_blue_dark
    )
    // 自定义ViewHolder类
    class VH(
        parent: ViewGroup,
        val binding: HomeEyepetizerCategoryBinding = HomeEyepetizerCategoryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ),
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: VH, position: Int, item: EyeCategoryBean?) {
        item?.run {
            val gradientDrawable = GradientDrawable()
            gradientDrawable.shape = GradientDrawable.RECTANGLE
            gradientDrawable.cornerRadius = 10.dp2px().toFloat()
            gradientDrawable.setColor(Color.parseColor(categoryColor))
            holder.binding.clEyepetizerCategoryItem.background = gradientDrawable
            holder.binding.tvEyepetizerCategoryItemTitle.setText("#$categoryName")
            holder.binding.tvEyepetizerCategoryItemDesc.setText(desc)
            val iv_eyepetizer_category =holder.binding.ivEyepetizerCategory
            if (item.categoryName.equals("日报")) {
                ImageLoader.loadLocalPhoto(
                    context,
                    R.drawable.home_icon_eye_daily,
                    iv_eyepetizer_category
                )
            } else if (item.categoryName.equals("发现")) {
                ImageLoader.loadLocalPhoto(
                    context,
                    R.drawable.home_icon_eye_find,
                    iv_eyepetizer_category
                )
            } else if (item.categoryName.equals("社区")) {
                ImageLoader.loadLocalPhoto(
                    context,
                    R.drawable.home_icon_eye_square,
                    iv_eyepetizer_category
                )
            } else if (item.categoryName.equals("推荐")) {
                ImageLoader.loadLocalPhoto(
                    context,
                    R.drawable.home_icon_eye_hot,
                    iv_eyepetizer_category
                )
            }
        }
    }
    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH {
        return VH(parent)
    }
}