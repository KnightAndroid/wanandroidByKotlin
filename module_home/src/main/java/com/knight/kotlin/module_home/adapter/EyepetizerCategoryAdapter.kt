package com.knight.kotlin.module_home.adapter

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.knight.kotlin.library_base.util.dp2px
import com.knight.kotlin.library_util.image.ImageLoader
import com.knight.kotlin.module_home.R
import com.knight.kotlin.module_home.entity.EyeCategoryBean

/**
 * Author:Knight
 * Time:2024/4/26 16:01
 * Description:EyepetizerCategoryAdapter
 */
class EyepetizerCategoryAdapter(data:MutableList<EyeCategoryBean>):
    BaseQuickAdapter<EyeCategoryBean, BaseViewHolder>(R.layout.home_eyepetizer_category,data) {

    private val colorArray = mutableListOf(
        android.R.color.holo_red_dark,
        android.R.color.holo_green_dark,
        android.R.color.holo_blue_dark
    )



    override fun convert(holder: BaseViewHolder, item: EyeCategoryBean) {
        item.run {
            val gradientDrawable = GradientDrawable()
            gradientDrawable.shape = GradientDrawable.RECTANGLE
            gradientDrawable.cornerRadius = 10.dp2px().toFloat()
            gradientDrawable.setColor(Color.parseColor(categoryColor))
            holder.getView<ConstraintLayout>(R.id.cl_eyepetizer_category_item).background = gradientDrawable
            holder.setText(R.id.tv_eyepetizer_category_item_title, "#$categoryName")
            holder.setText(R.id.tv_eyepetizer_category_item_desc,desc)
            val iv_eyepetizer_category = holder.getView<ImageView>(R.id.iv_eyepetizer_category)
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
            } else if (item.categoryName.equals("热门")) {
                ImageLoader.loadLocalPhoto(
                    context,
                    R.drawable.home_icon_eye_hot,
                    iv_eyepetizer_category
                )
            }
        }

    }
}