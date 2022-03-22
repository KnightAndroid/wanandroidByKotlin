package com.knight.kotlin.module_wechat.adapter

import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.text.TextUtils
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.knight.kotlin.library_base.ktx.toHtml
import com.knight.kotlin.library_base.util.CacheUtils
import com.knight.kotlin.module_wechat.R
import com.knight.kotlin.module_wechat.entity.WechatArticleEntity

/**
 *
 * @ProjectName:    wanandroid
 * @Package:        com.knight.kotlin.module_wechat.adapter
 * @ClassName:      WechatArticleAdapter
 * @Description:    java类作用描述
 * @Author:         knight
 * @CreateDate:     2022/3/22 10:28 上午
 * @UpdateUser:     更新者
 * @UpdateDate:     2022/3/22 10:28 上午
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */

class WechatArticleAdapter(data:MutableList<WechatArticleEntity>) :
    BaseQuickAdapter<WechatArticleEntity, BaseViewHolder>(R.layout.base_text_item,data) {
    override fun convert(holder: BaseViewHolder, item: WechatArticleEntity) {
        item.run {

            if(!author.isNullOrEmpty()){
                holder.setText(R.id.base_item_article_author,author)
            } else {
                holder.setText(R.id.base_item_article_author,shareUser)
            }

            //一级分类
            if (!TextUtils.isEmpty(superChapterName) || !TextUtils.isEmpty(chapterName) ) {
                holder.setVisible(R.id.base_tv_article_superchaptername,true)
                val gradientDrawable = GradientDrawable()
                gradientDrawable.shape = GradientDrawable.RECTANGLE
                gradientDrawable.setStroke(2, CacheUtils.getThemeColor())
                if (!TextUtils.isEmpty(superChapterName)) {
                    if (!TextUtils.isEmpty(chapterName)) {
                        holder.setText(R.id.base_tv_article_superchaptername,
                            "$superChapterName/$chapterName"
                        )
                    } else {
                        holder.setText(R.id.base_tv_article_superchaptername,superChapterName)
                    }
                } else {
                    if (!TextUtils.isEmpty(chapterName)) {
                        holder.setText(R.id.base_tv_article_superchaptername,chapterName)
                    } else {
                        holder.setText(R.id.base_tv_article_superchaptername,"")
                    }
                }

                holder.setTextColor(R.id.base_tv_article_superchaptername,CacheUtils.getThemeColor())
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    (holder.getView(R.id.base_tv_article_superchaptername) as TextView).background = gradientDrawable
                } else {
                    (holder.getView(R.id.base_tv_article_superchaptername) as TextView).setBackgroundDrawable(gradientDrawable)
                }
            } else {
                holder.setGone(R.id.base_tv_article_superchaptername,true)
            }

            //时间赋值
            if (!niceDate.isNullOrEmpty()) {
                holder.setText(R.id.base_item_articledata,niceDate)
            } else {
                holder.setText(R.id.base_item_articledata,niceShareDate)
            }

            //标题
            holder.setText(R.id.base_tv_articletitle,title.toHtml())

            //是否收藏
            if (collect) {
                holder.setBackgroundResource(R.id.base_icon_collect,R.drawable.base_icon_collect)
            } else {
                holder.setBackgroundResource(R.id.base_icon_collect,R.drawable.base_icon_nocollect)
            }


        }
    }
}