package com.knight.kotlin.module_home.adapter

import android.content.Context
import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseQuickAdapter
import com.knight.kotlin.module_home.databinding.HomeTopArticleItemBinding
import com.knight.kotlin.module_home.entity.TopArticleBean

/**
 * Author:Knight
 * Time:2022/2/17 10:43
 * Description:TopArticleAdapter 置顶文章/热搜
 */
@Deprecated("先废弃，暂时不用")
class TopArticleAdapter : BaseQuickAdapter<TopArticleBean, TopArticleAdapter.VH>() {


    // 自定义ViewHolder类
    class VH(
        parent: ViewGroup,
        val binding: HomeTopArticleItemBinding = HomeTopArticleItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ),
    ) : RecyclerView.ViewHolder(binding.root)


    private var mIsShowOnlyCount = false
    private var mCount = 3 //设置最多展示几条数据

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

    override fun getItemCount(items: List<TopArticleBean>): Int {
        return if (mIsShowOnlyCount) if (super.getItemCount(items) > mCount) mCount else super.getItemCount(items) else super.getItemCount(items)
    }

    override fun onBindViewHolder(holder: VH, position: Int, item: TopArticleBean?) {


          item?.run {
            //设置标题
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                holder.binding.homeTvTopArticleTitle.setText(Html.fromHtml(title, Html.FROM_HTML_MODE_LEGACY))
            } else {
                holder.binding.homeTvTopArticleTitle.setText( Html.fromHtml(title))
            }

            if (!author.isNullOrEmpty()) {
                //设置作者
                holder.binding.tvTopArticleAuthor.setText(author)
            } else {
                holder.binding.tvTopArticleAuthor.setText(shareUser)
            }


        }


    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH {
        return VH(parent)
    }
}