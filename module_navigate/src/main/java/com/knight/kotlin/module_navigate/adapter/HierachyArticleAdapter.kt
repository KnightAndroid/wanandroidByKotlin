package com.knight.kotlin.module_navigate.adapter

import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.text.TextUtils
import android.view.View
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.knight.kotlin.library_base.config.Appconfig
import com.knight.kotlin.library_base.ktx.toHtml
import com.knight.kotlin.library_base.util.CacheUtils
import com.knight.kotlin.library_util.StringUtils
import com.knight.kotlin.library_util.image.ImageLoader
import com.knight.kotlin.module_navigate.entity.HierachyTabArticleEntity

/**
 * Author:Knight
 * Time:2022/5/6 16:55
 * Description:HierachyArticleAdapter
 */
class HierachyArticleAdapter(data:MutableList<HierachyTabArticleEntity>):
    BaseMultiItemQuickAdapter<HierachyTabArticleEntity,BaseViewHolder>() {


    init {
        addItemType(Appconfig.ARTICLE_TEXT_TYPE, com.knight.kotlin.library_base.R.layout.base_text_item)
        addItemType(Appconfig.ARTICLE_PICTURE_TYPE, com.knight.kotlin.library_base.R.layout.base_article_item)
    }

    override fun convert(holder: BaseViewHolder, item: HierachyTabArticleEntity) {
        item.run {
            when (holder.itemViewType) {
                Appconfig.ARTICLE_TEXT_TYPE -> {
                    //作者
                    if (!TextUtils.isEmpty(author)) {
                        holder.setText(com.knight.kotlin.library_base.R.id.base_item_article_author,StringUtils.getStyle(context,author,Appconfig.search_keyword))
                    } else {
                        holder.setText(com.knight.kotlin.library_base.R.id.base_item_article_author,StringUtils.getStyle(context,shareUser,Appconfig.search_keyword))
                    }

                    //一级分类
                    if (!TextUtils.isEmpty(superChapterName) || !TextUtils.isEmpty(chapterName)) {
                        val gradientDrawable = GradientDrawable()
                        gradientDrawable.shape = GradientDrawable.RECTANGLE
                        gradientDrawable.setStroke(2, CacheUtils.getThemeColor())
                        holder.setVisible(com.knight.kotlin.library_base.R.id.base_tv_article_superchaptername, true)
                        if (!TextUtils.isEmpty(superChapterName)) {
                            if (!TextUtils.isEmpty(chapterName)) {
                                holder.setText(
                                    com.knight.kotlin.library_base.R.id.base_tv_article_superchaptername,
                                    superChapterName + "/" + chapterName)
                            } else {
                                holder.setText(com.knight.kotlin.library_base.R.id.base_tv_article_superchaptername, superChapterName)
                            }
                        } else {
                            if (!TextUtils.isEmpty(chapterName)) {
                                holder.setText(com.knight.kotlin.library_base.R.id.base_tv_article_superchaptername, chapterName)
                            } else {
                                holder.setText(com.knight.kotlin.library_base.R.id.base_tv_article_superchaptername, "")
                            }
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            holder.getView<View>(com.knight.kotlin.library_base.R.id.base_tv_article_superchaptername)
                                .setBackground(gradientDrawable)
                        } else {
                            holder.getView<View>(com.knight.kotlin.library_base.R.id.base_tv_article_superchaptername)
                                .setBackgroundDrawable(gradientDrawable)
                        }
                    } else {
                        holder.setGone(com.knight.kotlin.library_base.R.id.base_tv_article_superchaptername, true)
                    }
                    //时间赋值
                    if (!TextUtils.isEmpty(niceDate)) {
                        holder.setText(com.knight.kotlin.library_base.R.id.base_item_articledata, niceDate)
                    } else {
                        holder.setText(com.knight.kotlin.library_base.R.id.base_item_articledata, niceShareDate)
                    }
                    //标题
                    holder.setText(com.knight.kotlin.library_base.R.id.base_tv_articletitle, StringUtils.getStyle(context,
                            title.toHtml().toString(),Appconfig.search_keyword
                        )
                    )
                    //是否收藏
                    if (collect) {
                        holder.setBackgroundResource(com.knight.kotlin.library_base.R.id.base_icon_collect, com.knight.kotlin.library_base.R.drawable.base_icon_collect)
                    } else {
                        holder.setBackgroundResource(com.knight.kotlin.library_base.R.id.base_icon_collect, com.knight.kotlin.library_base.R.drawable.base_icon_nocollect)
                    }

                }
               Appconfig.ARTICLE_PICTURE_TYPE ->{
                   //项目图片
                   ImageLoader.loadStringPhoto(context,envelopePic,holder.getView(com.knight.kotlin.library_base.R.id.base_item_imageview))

                   //作者
                   if (!TextUtils.isEmpty(author)) {
                        holder.setText(com.knight.kotlin.library_base.R.id.base_item_tv_author,StringUtils.getStyle(context,author,Appconfig.search_keyword))
                   } else {
                        holder.setText(com.knight.kotlin.library_base.R.id.base_item_tv_author,StringUtils.getStyle(context,shareUser,Appconfig.search_keyword))
                   }
                   //时间赋值
                   if (!TextUtils.isEmpty(niceDate)) {
                       holder.setText(com.knight.kotlin.library_base.R.id.base_item_tv_time,niceDate)
                   } else {
                       holder.setText(com.knight.kotlin.library_base.R.id.base_item_tv_time,niceShareDate)
                   }
                   //标题
                   holder.setText(com.knight.kotlin.library_base.R.id.base_tv_title,StringUtils.getStyle(context,title.toHtml().toString(),Appconfig.search_keyword))

                   //描述
                   if (!TextUtils.isEmpty(desc)) {
                       holder.setVisible(com.knight.kotlin.library_base.R.id.base_tv_project_desc,true)
                       holder.setText(com.knight.kotlin.library_base.R.id.base_tv_project_desc,StringUtils.getStyle(context,desc.toHtml().toString(),Appconfig.search_keyword))
                   } else {
                       holder.setGone(com.knight.kotlin.library_base.R.id.base_tv_superchapter,true)
                   }

                   //分类
                   if (!TextUtils.isEmpty(superChapterName)) {
                       holder.setVisible(com.knight.kotlin.library_base.R.id.base_tv_superchapter,true)
                       holder.setText(com.knight.kotlin.library_base.R.id.base_tv_superchapter,superChapterName)
                   } else {
                       holder.setGone(com.knight.kotlin.library_base.R.id.base_tv_superchapter,true)
                   }

                   //是否收藏
                   if (collect) {
                       holder.setBackgroundResource(com.knight.kotlin.library_base.R.id.base_article_collect,com.knight.kotlin.library_base.R.drawable.base_icon_collect)
                   } else {
                       holder.setBackgroundResource(com.knight.kotlin.library_base.R.id.base_article_collect,com.knight.kotlin.library_base.R.drawable.base_icon_nocollect)
                   }
               }
               else -> {}
            }
        }
    }


}