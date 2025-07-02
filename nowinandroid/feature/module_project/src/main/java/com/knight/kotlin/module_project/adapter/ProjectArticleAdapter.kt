package com.knight.kotlin.module_project.adapter

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseQuickAdapter
import com.core.library_base.databinding.BaseArticleItemBinding
import com.core.library_base.ktx.toHtml
import com.knight.kotlin.library_util.image.ImageLoader
import com.knight.kotlin.module_project.entity.ProjectArticleBean

/**
 * Author:Knight
 * Time:2022/4/29 14:51
 * Description:ProjectArticleAdapter
 */
class ProjectArticleAdapter: BaseQuickAdapter<ProjectArticleBean, ProjectArticleAdapter.VH>()  {

    class VH(
        parent: ViewGroup,
        val binding: BaseArticleItemBinding = BaseArticleItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ),
    ) : RecyclerView.ViewHolder(binding.root)


    override fun onBindViewHolder(holder: VH, position: Int, item: ProjectArticleBean?) {
        item?.run {
            //图片
            ImageLoader.loadStringPhoto(context,envelopePic,holder.binding.baseItemImageview)
            //作者
            if (!TextUtils.isEmpty(author)) {
                holder.binding.baseItemTvAuthor.setText(author)
            } else {
                holder.binding.baseItemTvAuthor.setText(shareUser)
            }
            //时间赋值
            if (!TextUtils.isEmpty(niceDate)) {
                holder.binding.baseItemTvTime.setText(niceDate)
            } else {
                holder.binding.baseItemTvTime.setText(niceShareDate)
            }
            //标题
            holder.binding.baseTvTitle.setText(title.toHtml())
            //描述
            if (!TextUtils.isEmpty(desc)) {
                holder.binding.baseTvProjectDesc.visibility = View.VISIBLE
                holder.binding.baseTvProjectDesc.setText(desc.toHtml())
            } else {
                holder.binding.baseTvProjectDesc.visibility = View.GONE
            }
            //分类
            if (superChapterName.isNullOrEmpty()) {
                holder.binding.baseTvSuperchapter.visibility = View.GONE
            } else {
                holder.binding.baseTvSuperchapter.visibility = View.VISIBLE
                holder.binding.baseTvSuperchapter.setText(superChapterName)
            }
            //是否收藏
            holder.binding.baseArticleCollect.setBackgroundResource(if(collect) com.core.library_base.R.drawable.base_icon_collect else com.core.library_base.R.drawable.base_icon_nocollect)

        }
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH {
        return VH(parent)
    }
}