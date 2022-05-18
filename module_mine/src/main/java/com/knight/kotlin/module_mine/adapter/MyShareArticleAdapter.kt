package com.knight.kotlin.module_mine.adapter

import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.knight.kotlin.library_base.ktx.toHtml
import com.knight.kotlin.library_base.util.CacheUtils
import com.knight.kotlin.module_mine.R
import com.knight.kotlin.module_mine.entity.MyArticleEntity

/**
 * Author:Knight
 * Time:2022/5/17 11:17
 * Description:MyShareArticleAdapter
 */
class MyShareArticleAdapter(data:MutableList<MyArticleEntity>) :
    BaseQuickAdapter<MyArticleEntity, BaseViewHolder>(R.layout.mine_sharearticles_item,data) {
    override fun convert(holder: BaseViewHolder, item: MyArticleEntity) {
        item.run {
            //作者
            holder.setText(R.id.mine_tv_share_articleauthor,shareUser)
            //时间
            holder.setText(R.id.mine_tv_share_articledata,niceDate)
            //标题
            holder.setText(R.id.mine_tv_share_articletitle,title.toHtml())
            //文章类别
            if (chapterName.isNullOrEmpty()) {
                holder.setText(R.id.mine_tv_share_articlechaptername,superChapterName)
            } else {
                holder.setText(R.id.mine_tv_share_articlechaptername, "$superChapterName/$chapterName")

            }
            val gradientDrawable = GradientDrawable()
            gradientDrawable.shape = GradientDrawable.RECTANGLE
            gradientDrawable.setStroke(2, CacheUtils.getThemeColor())
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                holder.getView<View>(R.id.mine_tv_share_articlechaptername).setBackground(gradientDrawable)
            } else {
                holder.getView<View>(R.id.mine_tv_share_articlechaptername).setBackgroundDrawable(gradientDrawable)
            }
            holder.setTextColor(R.id.mine_tv_share_articlechaptername,CacheUtils.getThemeColor())
            //是否是新文章
            if (fresh) {
                holder.setVisible(R.id.mine_tv_share_articlenew,true)
            } else {
                holder.setGone(R.id.mine_tv_share_articlenew,true)
            }
        }

    }
}