package com.knight.kotlin.module_mine.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseQuickAdapter
import com.knight.kotlin.library_base.ktx.getUser
import com.knight.kotlin.library_base.util.CacheUtils
import com.knight.kotlin.library_util.image.ImageLoader
import com.knight.kotlin.module_mine.R
import com.knight.kotlin.module_mine.databinding.MineCoinrankItemBinding
import com.knight.kotlin.module_mine.entity.CoinRankEntity

/**
 * Author:Knight
 * Time:2022/5/7 14:53
 * Description:RankCoinAdapter
 */
class RankCoinAdapter :
    BaseQuickAdapter<CoinRankEntity, RankCoinAdapter.VH>()  {

    class VH(
        parent: ViewGroup,
        val binding: MineCoinrankItemBinding = MineCoinrankItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ),
    ) : RecyclerView.ViewHolder(binding.root)




    override fun onBindViewHolder(holder: VH, position: Int, item: CoinRankEntity?) {
        item?.run {
            holder.binding.mineTvRank.setText(rank)
            holder.binding.mineTvRankusername.setText(username)
            holder.binding.mineTvRankcoincount.setText(coinCount.toString())

            getUser()?.let {
                if (it.id == userId) {
                    holder.binding.mineTvRank.setTextColor(CacheUtils.getThemeColor())
                    holder.binding.mineTvRankusername.setTextColor(CacheUtils.getThemeColor())
                } else {
                    if (CacheUtils.getNormalDark()) {
                        holder.binding.mineTvRank.setTextColor( Color.parseColor("#D3D3D3"))
                        holder.binding.mineTvRankusername.setTextColor(Color.parseColor("#D3D3D3"))

                    } else {
                        holder.binding.mineTvRank.setTextColor(Color.parseColor("#333333"))
                        holder.binding.mineTvRankusername.setTextColor(Color.parseColor("#333333"))
                    }
                }
            } ?: run {
                if (CacheUtils.getNormalDark()) {
                    holder.binding.mineTvRank.setTextColor(Color.parseColor("#D3D3D3"))
                    holder.binding.mineTvRankusername.setTextColor(Color.parseColor("#D3D3D3"))
                } else {
                    holder.binding.mineTvRank.setTextColor(Color.parseColor("#333333"))
                    holder.binding.mineTvRankusername.setTextColor(Color.parseColor("#333333"))
                }
            }

            if ("1" == rank) {
                holder.binding.mineIvRank.visibility = View.VISIBLE
                ImageLoader.loadLocalPhoto(context,R.drawable.mine_first_points,holder.binding.mineIvRank)
            } else if ("2" == rank) {
                holder.binding.mineIvRank.visibility = View.VISIBLE
                ImageLoader.loadLocalPhoto(context,R.drawable.mine_second_points,holder.binding.mineIvRank)
            } else if ("3" == rank) {
                holder.binding.mineIvRank.visibility = View.VISIBLE
                ImageLoader.loadLocalPhoto(context,R.drawable.mine_third_points,holder.binding.mineIvRank)
            } else {
                holder.binding.mineIvRank.visibility = View.INVISIBLE
            }

            holder.binding.mineTvRankcoincount.setTextColor(CacheUtils.getThemeColor())
        }
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH {
        return VH(parent)
    }
}