package com.knight.kotlin.module_course.adapter

import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.knight.kotlin.library_base.util.CacheUtils
import com.knight.kotlin.module_course.R
import com.knight.kotlin.module_course.entity.CourseDetailEntity

/**
 * Author:Knight
 * Time:2022/6/2 17:37
 * Description:CourseDetailListAdapter
 */
class CourseDetailListAdapter (data:MutableList<CourseDetailEntity>):
    BaseQuickAdapter<CourseDetailEntity, BaseViewHolder>( R.layout.course_detail_list_item,data) {
    override fun convert(holder: BaseViewHolder, item: CourseDetailEntity) {
        item.run {
            holder.setText(R.id.course_item_articleauthor, author)
            val gradientDrawable = GradientDrawable()
            gradientDrawable.shape = GradientDrawable.RECTANGLE
            gradientDrawable.setStroke(2, CacheUtils.getThemeColor())
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                holder.getView<View>(R.id.course_tv_articlechaptername)
                    .setBackground(gradientDrawable)
            } else {
                holder.getView<View>(R.id.course_tv_articlechaptername)
                    .setBackgroundDrawable(gradientDrawable)
            }
            //标签
            holder.setText(
                R.id.course_tv_articlechaptername,
                chapterName
            )
            //标题
            holder.setText(R.id.course_tv_articletitle,title)
            //时间
            holder.setText(R.id.course_item_articledata, niceDate)
        }
    }
}