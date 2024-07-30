package com.knight.kotlin.module_course.adapter

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseQuickAdapter
import com.knight.kotlin.library_util.image.ImageLoader
import com.knight.kotlin.module_course.databinding.CourseListItemBinding
import com.knight.kotlin.module_course.entity.CourseEntity

/**
 * Author:Knight
 * Time:2022/6/2 15:58
 * Description:CourseListAdapter      CourseEntity
 */
class CourseListAdapter:
    BaseQuickAdapter<CourseEntity,CourseListAdapter.VH>() {



    class VH(
        parent: ViewGroup,
        val binding: CourseListItemBinding = CourseListItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ),
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: VH, position: Int, item: CourseEntity?) {
        item?.run {
            ImageLoader.loadStringPhoto(context, cover, holder.binding.courseListIvCover)
            //作者
            if (!TextUtils.isEmpty(author)) {
                holder.binding.courseListTvAuthor.setText( author)
            } else {
                holder.binding.courseListTvAuthor.setText( "不详")
            }
            //标题
            holder.binding.courseTvTitle.setText(name)
            holder.binding.courseListTvDesc.setText(desc)

        }
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH {
        return VH(parent)
    }

}