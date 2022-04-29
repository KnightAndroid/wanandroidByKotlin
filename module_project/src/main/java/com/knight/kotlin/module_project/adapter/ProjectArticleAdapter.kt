package com.knight.kotlin.module_project.adapter

import android.text.TextUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.knight.kotlin.library_base.ktx.toHtml
import com.knight.kotlin.library_util.image.ImageLoader
import com.knight.kotlin.module_project.R
import com.knight.kotlin.module_project.entity.ProjectArticleBean

/**
 * Author:Knight
 * Time:2022/4/29 14:51
 * Description:ProjectArticleAdapter
 */
class ProjectArticleAdapter(data:MutableList<ProjectArticleBean>): BaseQuickAdapter<ProjectArticleBean, BaseViewHolder>( R.layout.base_article_item,data)  {
    override fun convert(holder: BaseViewHolder, item: ProjectArticleBean) {
        item.run {
            //图片
            ImageLoader.loadStringPhoto(context,envelopePic,holder.getView(R.id.base_item_imageview))
            //作者
            if (!TextUtils.isEmpty(author)) {
                holder.setText(R.id.base_item_tv_author,author)
            } else {
                holder.setText(R.id.base_item_tv_author,shareUser)
            }
            //时间赋值
            if (!TextUtils.isEmpty(niceDate)) {
                holder.setText(R.id.base_item_tv_time,niceDate)
            } else {
                holder.setText(R.id.base_item_tv_time,niceShareDate)
            }
            //标题
            holder.setText(R.id.base_tv_title,title.toHtml())
            //描述
            if (!TextUtils.isEmpty(desc)) {
                holder.setVisible(R.id.base_tv_project_desc,true)
                holder.setText(R.id.base_tv_project_desc,desc.toHtml())
            } else {
                holder.setGone(R.id.base_tv_project_desc,true)
            }
            //分类
            if (superChapterName.isNullOrEmpty()) {
                holder.setGone(R.id.base_tv_superchapter,true)
            } else {
                holder.setVisible(R.id.base_tv_superchapter,true)
                holder.setText(R.id.base_tv_superchapter,superChapterName)
            }
            //是否收藏
            holder.setBackgroundResource(R.id.base_article_collect,if(collect) R.drawable.base_icon_collect else R.drawable.base_icon_nocollect)


        }

    }
}