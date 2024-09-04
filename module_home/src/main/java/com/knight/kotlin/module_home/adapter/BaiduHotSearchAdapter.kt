package com.knight.kotlin.module_home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseQuickAdapter
import com.knight.kotlin.module_home.R
import com.knight.kotlin.module_home.databinding.HomeBaiduRealtimeItemBinding
import com.knight.kotlin.module_home.entity.HomeBaiduContent


/**
 * @author created by luguian
 * @organize
 * @Date 2024/9/4 16:13
 * @descript:百度热搜榜适配器
 */
class BaiduHotSearchAdapter : BaseQuickAdapter<HomeBaiduContent, BaiduHotSearchAdapter.VH>(){


    // 自定义ViewHolder类
    class VH(
        parent: ViewGroup,
        val binding: HomeBaiduRealtimeItemBinding = HomeBaiduRealtimeItemBinding.inflate(
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
//        if (mIsShowOnlyCount) {
//            notifyItemRangeInserted(count, itemCount - count)
//        } else {
//            notifyItemRangeInserted(1,   count - itemCount)
//        }

        notifyDataSetChanged()
    }

//    override fun getItemCount(items: List<HomeBaiduContent>): Int {
//        return if (mIsShowOnlyCount) if (super.getItemCount(items) > mCount) mCount else super.getItemCount(items) else super.getItemCount(items)
//    }


    override fun onBindViewHolder(holder: VH, position: Int, item: HomeBaiduContent?) {
        val binding = DataBindingUtil.getBinding<HomeBaiduRealtimeItemBinding>(holder.itemView)
        item?.run {
            binding?.viewModel = this
            if (position == 0) {
                holder.binding.ivRankHot.visibility = View.VISIBLE
                holder.binding.ivRankHot.setBackgroundResource(R.drawable.home_baidu_real_time_one)
            } else if (position == 1) {
                holder.binding.ivRankHot.visibility = View.VISIBLE
                holder.binding.ivRankHot.setBackgroundResource(R.drawable.home_baidu_real_time_two)
            } else if (position == 2) {
                holder.binding.ivRankHot.visibility = View.VISIBLE
                holder.binding.ivRankHot.setBackgroundResource(R.drawable.home_baidu_real_time_three)
            } else {
                holder.binding.ivRankHot.visibility = View.INVISIBLE
            }
        }
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH {
        return VH(parent)
    }

}