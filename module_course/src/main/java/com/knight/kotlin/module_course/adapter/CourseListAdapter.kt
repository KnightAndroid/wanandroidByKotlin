package com.knight.kotlin.module_course.adapter

import android.text.TextUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.knight.kotlin.library_util.image.ImageLoader
import com.knight.kotlin.module_course.R
import com.knight.kotlin.module_course.entity.CourseEntity

/**
 * Author:Knight
 * Time:2022/6/2 15:58
 * Description:CourseListAdapter
 */
class CourseListAdapter (data:MutableList<CourseEntity>):
    BaseQuickAdapter<CourseEntity, BaseViewHolder>( R.layout.course_list_item,data) {
    override fun convert(holder: BaseViewHolder, item: CourseEntity) {
        item.run {
            ImageLoader.loadStringPhoto(context, cover, holder.getView(R.id.course_list_iv_cover))
            //作者
            if (!TextUtils.isEmpty(author)) {
                holder.setText(R.id.course_list_tv_author, author)
            } else {
                holder.setText(R.id.course_list_tv_author, "不详")
            }
            //标题
            holder.setText(R.id.course_tv_title, name)
            holder.setText(R.id.course_list_tv_desc, desc)

        }
    }
}