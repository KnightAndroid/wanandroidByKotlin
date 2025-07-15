package com.knight.kotlin.module_course.adapter

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseQuickAdapter
import com.knight.kotlin.library_common.util.CacheUtils

import com.knight.kotlin.module_course.databinding.CourseDetailListItemBinding
import com.knight.kotlin.module_course.entity.CourseDetailEntity

/**
 * Author:Knight
 * Time:2022/6/2 17:37
 * Description:CourseDetailListAdapter
 */
class CourseDetailListAdapter:
    BaseQuickAdapter<CourseDetailEntity, CourseDetailListAdapter.VH>() {

    //R.layout.course_detail_list_item data:MutableList<CourseDetailEntity>
    // 自定义ViewHolder类
    class VH(
        parent: ViewGroup,
        val binding: CourseDetailListItemBinding = CourseDetailListItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ),
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH {
        // 返回一个 ViewHolder
        return VH(parent)
    }

    override fun onBindViewHolder(holder: VH, position: Int, item: CourseDetailEntity?) {
        // 设置item数据
        item?.run {
            holder.binding.courseItemArticleauthor.setText(author)
            val gradientDrawable = GradientDrawable()
            gradientDrawable.shape = GradientDrawable.RECTANGLE
            gradientDrawable.setStroke(2, CacheUtils.getThemeColor())
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                holder.binding.courseTvArticlechaptername.setBackground(gradientDrawable)
            } else {
                holder.binding.courseTvArticlechaptername.setBackgroundDrawable(gradientDrawable)
            }
            //标签
            holder.binding.courseTvArticlechaptername.setText(chapterName)
            //标题
            holder.binding.courseTvArticletitle.setText(title)
            //时间
            holder.binding.courseItemArticledate.setText(niceDate)
        }
    }




}