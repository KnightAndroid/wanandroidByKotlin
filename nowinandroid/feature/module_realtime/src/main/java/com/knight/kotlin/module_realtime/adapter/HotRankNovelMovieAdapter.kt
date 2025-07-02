package com.knight.kotlin.module_realtime.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseQuickAdapter
import com.core.library_base.util.dp2px
import com.knight.kotlin.library_base.entity.BaiduContent
import com.knight.kotlin.library_util.image.ImageLoader
import com.knight.kotlin.module_realtime.R
import com.knight.kotlin.module_realtime.databinding.RealtimeNovelRankItemBinding
import com.knight.kotlin.module_realtime.ktx.HotAdapterInterface


/**
 * @author created by luguian
 * @organize
 * @Date 2025/3/18 17:06
 * @descript:热搜小说 电影 适配器
 */
class HotRankNovelMovieAdapter: BaseQuickAdapter<BaiduContent, HotRankNovelMovieAdapter.VH>(), HotAdapterInterface<BaiduContent> {


    // 自定义ViewHolder类
    class VH(
        parent: ViewGroup,
        val binding: RealtimeNovelRankItemBinding = RealtimeNovelRankItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ),
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: VH, position: Int, item: BaiduContent?) {
        item?.run {
            ImageLoader.loadRoundedCornerPhoto(context,img,holder.binding.ivRealtimeUrl,3.dp2px())
            holder.binding.tvRankPosition.text = (position+1).toString()
            if (position == 0) {
                holder.binding.tvRankPosition.setBackgroundResource(R.drawable.hot_rank_novel_one)

            } else if(position == 1){
                holder.binding.tvRankPosition.setBackgroundResource(R.drawable.hot_rank_novel_second)
            } else {
                holder.binding.tvRankPosition.setBackgroundResource(R.drawable.hot_rank_novel_third)
            }
            holder.binding.tvNovelMovieTitle.setText(word)
            if (show.size == 1) {
                if (show.get(0).length > 12) {
                    holder.binding.tvNovelMovieDesc.setText(show.get(0).substring(0,12)+"...")
                } else {
                    holder.binding.tvNovelMovieDesc.setText(show.get(0))
                }

            } else {
                val result = StringBuilder() // 使用 StringBuilder 提高效率
                for (str in show) {
                    if (str.length > 12) {
                        result.append(str.substring(0, 12) + "...").append("\n") // 追加字符串和换行符
                    } else {
                        result.append(str).append("\n") // 追加字符串和换行符
                    }



                }
                holder.binding.tvNovelMovieDesc.setText(result)
            }

            holder.binding.tvRankHotScore.setText(hotScore)
            if (hotChange == "up") {
                 holder.binding.ivRankTrend.setBackgroundResource(R.drawable.realtime_hotchange_up)
            } else if (hotChange == "down") {
                holder.binding.ivRankTrend.setBackgroundResource(R.drawable.realtime_hotchange_down)
            } else {
                holder.binding.ivRankTrend.setBackgroundResource(R.drawable.realtime_hotchange_same)
            }
        }
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH {
        return VH(parent)
    }

    override fun setOnItemClickListener(listener: (adapter: BaseQuickAdapter<BaiduContent, *>, view: View, position: Int) -> Unit) {
        super.setOnItemClickListener(listener)
    }
}