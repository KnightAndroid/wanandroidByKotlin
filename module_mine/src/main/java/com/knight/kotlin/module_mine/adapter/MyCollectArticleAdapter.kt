package com.knight.kotlin.module_mine.adapter

import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.text.TextUtils
import android.widget.TextView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.knight.kotlin.library_base.config.Appconfig
import com.knight.kotlin.library_base.ktx.toHtml
import com.knight.kotlin.library_base.util.CacheUtils
import com.knight.kotlin.library_util.image.ImageLoader
import com.knight.kotlin.module_mine.R
import com.knight.kotlin.module_mine.entity.MyCollectArticleEntity

/**
 * Author:Knight
 * Time:2022/5/13 10:51
 * Description:MyCollectArticleAdapter
 */
class MyCollectArticleAdapter(data:MutableList<MyCollectArticleEntity>) :
    BaseMultiItemQuickAdapter<MyCollectArticleEntity, BaseViewHolder>() {

    init {
        addItemType(Appconfig.ARTICLE_TEXT_TYPE, R.layout.base_text_item)
        addItemType(Appconfig.ARTICLE_PICTURE_TYPE, R.layout.base_article_item)

    }

    override fun convert(holder: BaseViewHolder, item: MyCollectArticleEntity) {
        item.run {
            when (holder.itemViewType) {
                Appconfig.ARTICLE_TEXT_TYPE -> {
                    //作者
                    if (author.isNullOrEmpty()) {
                        holder.setText(R.id.base_item_article_author,"不详")
                    } else {
                        holder.setText(R.id.base_item_article_author,author)
                    }

                    //一级分类
                    if (!TextUtils.isEmpty(item.chapterName)) {
                        holder.setVisible(R.id.base_tv_article_superchaptername,true)
                        holder.setText(R.id.base_tv_article_superchaptername,chapterName)
                        holder.setTextColor(R.id.base_tv_article_superchaptername,CacheUtils.getThemeColor())
                        val gradientDrawable = GradientDrawable()
                        gradientDrawable.shape = GradientDrawable.RECTANGLE
                        gradientDrawable.setStroke(2,CacheUtils.getThemeColor())
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            holder.getView<TextView>(R.id.base_tv_article_superchaptername).background = gradientDrawable
                        } else {
                            holder.getView<TextView>(R.id.base_tv_article_superchaptername).setBackgroundDrawable(gradientDrawable)
                        }
                    } else {
                        holder.setGone(R.id.base_tv_article_superchaptername,true)
                    }

                    //时间赋值
                    if (!TextUtils.isEmpty(niceDate)) {
                        holder.setText(R.id.base_item_articledata,niceDate)
                    } else {
                        holder.setGone(R.id.base_item_articledata,true)
                    }
                    //标题
                    holder.setText(R.id.base_tv_articletitle,title.toHtml())
                    //是否收藏
                    holder.setBackgroundResource(R.id.base_icon_collect,R.drawable.mine_icon_delete)
                }

                Appconfig.ARTICLE_PICTURE_TYPE ->{
                    //项目图片
                    ImageLoader.loadStringPhoto(context,envelopePic,holder.getView(R.id.base_item_imageview))
                    //作者
                    if (author.isNullOrEmpty()) {
                        holder.setText(R.id.base_item_tv_author,"不详")
                    } else {
                        holder.setText(R.id.base_item_tv_author,author)
                    }

                    //时间赋值
                    if (!TextUtils.isEmpty(niceDate)) {
                        holder.setVisible(R.id.base_item_tv_time,true)
                        holder.setText(R.id.base_item_tv_time,niceDate)
                    } else {
                        holder.setGone(R.id.base_item_tv_time,true)
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
                    if (!TextUtils.isEmpty(chapterName)) {
                        holder.setVisible(R.id.base_tv_superchapter,true)
                        holder.setText(R.id.base_tv_superchapter,chapterName)
                    } else {
                        holder.setGone(R.id.base_tv_superchapter,true)
                    }
                }
                else -> {}
            }
        }
    }
}