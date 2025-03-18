package com.knight.kotlin.module_realtime.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseQuickAdapter
import com.knight.kotlin.library_base.entity.BaiduContent
import com.knight.kotlin.library_base.util.dp2px
import com.knight.kotlin.library_util.image.ImageLoader
import com.knight.kotlin.module_realtime.R
import com.knight.kotlin.module_realtime.databinding.RealtimeNovelRankItemBinding


/**
 * @author created by luguian
 * @organize
 * @Date 2025/3/18 17:06
 * @descript:
 */
class HotRankNovelMovieAdapter: BaseQuickAdapter<BaiduContent, HotRankNovelMovieAdapter.VH>() {


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
            holder.binding.tvNovelMovieTitle.setText(query)
            holder.binding.tvNovelMovieDesc.setText(show.get(0))
        }
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH {
        return VH(parent)
    }
}