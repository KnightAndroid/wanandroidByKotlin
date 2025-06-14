package com.knight.kotlin.module_wechat.adapter

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseQuickAdapter
import com.knight.kotlin.library_base.databinding.BaseTextItemBinding
import com.knight.kotlin.library_base.ktx.toHtml
import com.knight.kotlin.library_base.util.CacheUtils
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

class WechatArticleAdapter :
    BaseQuickAdapter<WechatArticleEntity, WechatArticleAdapter.VH>() {

    class VH(
        parent: ViewGroup,
        val binding: BaseTextItemBinding = BaseTextItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ),
    ) : RecyclerView.ViewHolder(binding.root)



    override fun onBindViewHolder(holder: VH, position: Int, item: WechatArticleEntity?) {
        item?.run {

            if (!author.isNullOrEmpty()) {
                holder.binding.baseItemArticleAuthor.setText(author)
            } else {
                holder.binding.baseItemArticleAuthor.setText(shareUser)
            }

            //一级分类
            if (!TextUtils.isEmpty(superChapterName) || !TextUtils.isEmpty(chapterName)) {
                holder.binding.baseTvArticleSuperchaptername.visibility = View.VISIBLE
                val gradientDrawable = GradientDrawable()
                gradientDrawable.shape = GradientDrawable.RECTANGLE
                gradientDrawable.setStroke(2, CacheUtils.getThemeColor())
                if (!TextUtils.isEmpty(superChapterName)) {
                    if (!TextUtils.isEmpty(chapterName)) {
                        holder.binding.baseTvArticleSuperchaptername.setText("$superChapterName/$chapterName")
                    } else {
                        holder.binding.baseTvArticleSuperchaptername.setText(superChapterName)
                    }
                } else {
                    if (!TextUtils.isEmpty(chapterName)) {
                        holder.binding.baseTvArticleSuperchaptername.setText(chapterName)
                    } else {
                        holder.binding.baseTvArticleSuperchaptername.setText("")
                    }
                }

                holder.binding.baseTvArticleSuperchaptername.setTextColor(
                    CacheUtils.getThemeColor()
                )
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                     holder.binding.baseTvArticleSuperchaptername.background =
                        gradientDrawable
                } else {
                    holder.binding.baseTvArticleSuperchaptername.setBackgroundDrawable(
                        gradientDrawable
                    )
                }
            } else {
                holder.binding.baseTvArticleSuperchaptername.visibility = View.GONE
            }

            //时间赋值
            if (!niceDate.isNullOrEmpty()) {
                holder.binding.baseItemArticledate.setText(niceDate)
            } else {
                holder.binding.baseItemArticledate.setText(niceShareDate)
            }

            //标题
            holder.binding.baseTvArticletitle.setText(title.toHtml())

            //是否收藏
            if (collect) {
                holder.binding.baseIconCollect.setBackgroundResource(
                    com.knight.kotlin.library_base.R.drawable.base_icon_collect
                )
            } else {
                holder.binding.baseIconCollect.setBackgroundResource(
                    com.knight.kotlin.library_base.R.drawable.base_icon_nocollect
                )
            }
        }
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH {
        return VH(parent)
    }
}