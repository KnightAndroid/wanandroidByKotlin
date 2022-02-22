package com.knight.kotlin.module_home.adapter

import android.os.Build
import android.text.Html
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.knight.kotlin.module_home.R
import com.knight.kotlin.module_home.entity.TopArticleBean

/**
 * Author:Knight
 * Time:2022/2/17 10:43
 * Description:TopArticleAdapter
 */
class TopArticleAdapter(data:MutableList<TopArticleBean>) : BaseQuickAdapter<TopArticleBean,BaseViewHolder>(
    R.layout.home_top_article_item,data) {

    private var mIsShowOnlyCount = false
    private var mCount = 3 //设置最多展示几条数据

    override fun convert(holder: BaseViewHolder, item: TopArticleBean) {
        item.run {
            //设置标题
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                holder.setText(R.id.home_tv_top_article_title, Html.fromHtml(title, Html.FROM_HTML_MODE_LEGACY))
            } else {
                holder.setText(R.id.home_tv_top_article_title,Html.fromHtml(title))
            }

            if (!author.isNullOrEmpty()) {
                //设置作者
                holder.setText(R.id.tv_top_article_author,author)
            } else {
                holder.setText(R.id.tv_top_article_author,shareUser)
            }


        }
    }

    /**
     * 设置是否仅显示的条数         * 默认全部显示
     */
    fun setShowOnlyThree(isShowOnlyThree: Boolean) {
        setShowOnlyCount(isShowOnlyThree, 3)
    }

    /**
     * 设置显示的条数
     */
    fun setShowOnlyCount(isShowOnlyThree: Boolean, count: Int) {
        mIsShowOnlyCount = isShowOnlyThree
        mCount = count
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return if (mIsShowOnlyCount) if (super.getItemCount() > mCount) mCount else super.getItemCount() else super.getItemCount()
    }
}