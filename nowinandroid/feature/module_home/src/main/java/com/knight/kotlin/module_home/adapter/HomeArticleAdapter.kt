package com.knight.kotlin.module_home.adapter

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseQuickAdapter
import com.core.library_base.ktx.toHtml
import com.knight.kotlin.library_common.util.CacheUtils
import com.knight.kotlin.module_home.databinding.HomeArticleItemBinding
import com.knight.kotlin.module_home.entity.HomeArticleEntity

/**
 * Author:Knight
 * Time:2022/2/18 15:35
 * Description:HomeArticleAdapter
 */
//HomeArticleEntity
class HomeArticleAdapter: BaseQuickAdapter<HomeArticleEntity, HomeArticleAdapter.VH>() {

    // 自定义ViewHolder类
    class VH(
        parent: ViewGroup,
        val binding: HomeArticleItemBinding = HomeArticleItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ),
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: VH, position: Int, item: HomeArticleEntity?) {
        item?.run {
            if (!author.isNullOrEmpty()) {
                holder.binding.homeItemArticleAuthor.setText(author)
            } else {
                holder.binding.homeItemArticleAuthor.setText(shareUser)
            }

            //是否是新文章
            if (fresh == true) {
                holder.binding.homeItemArticleNewFlag.visibility = View.VISIBLE
            } else {
                holder.binding.homeItemArticleNewFlag.visibility = View.GONE
            }

//            //二级分类
            if (!chapterName.isNullOrEmpty()) {
                holder.binding.homeItemArticleChaptername.visibility = View.VISIBLE
                holder.binding.homeItemArticleChaptername.setText(chapterName)
                val gradientDrawable = GradientDrawable()
                gradientDrawable.shape = GradientDrawable.RECTANGLE
                gradientDrawable.setStroke(2, CacheUtils.getThemeColor())

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    holder.binding.homeItemArticleChaptername.background = gradientDrawable
                } else {
                    holder.binding.homeItemArticleChaptername.setBackgroundDrawable(gradientDrawable)
                }
                holder.binding.homeItemArticleChaptername.setTextColor( CacheUtils.getThemeColor())
            } else {
                holder.binding.homeItemArticleChaptername.visibility = View.GONE
            }
//
            //时间赋值
            if (!niceDate.isNullOrEmpty()) {
                holder.binding.homeItemArticledate.setText(niceDate)
            } else {
                holder.binding.homeItemArticledate.setText(niceShareDate)
            }
//
            //标题
            holder.binding.homeTvArticletitle.setText(title.toHtml())
            //描述
            if (!desc.isNullOrEmpty()) {
                holder.binding.homeTvArticledesc.visibility = View.VISIBLE
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    holder.binding.homeTvArticledesc.setText(
                        Html.fromHtml(desc,
                        Html.FROM_HTML_MODE_LEGACY))
                } else {
                    holder.binding.homeTvArticledesc.setText(Html.fromHtml(desc))
                }
            } else {
                holder.binding.homeTvArticledesc.visibility = View.GONE

            }

            //一级分类
            if (!superChapterName.isNullOrEmpty()) {
                holder.binding.homeTvArticleSuperchaptername.visibility = View.VISIBLE
                holder.binding.homeTvArticleSuperchaptername.setText(superChapterName)
            } else {
                holder.binding.homeTvArticleSuperchaptername.visibility = View.GONE
            }

            //是否有收藏
            if (collect == true) {
                holder.binding.homeIconCollect.setBackgroundResource(com.core.library_base.R.drawable.base_icon_collect)
            } else {
                holder.binding.homeIconCollect.setBackgroundResource(com.core.library_base.R.drawable.base_icon_nocollect)
            }
        }
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH {
        return VH(parent)
    }

}