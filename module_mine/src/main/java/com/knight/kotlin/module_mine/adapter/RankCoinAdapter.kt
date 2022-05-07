package com.knight.kotlin.module_mine.adapter

import android.graphics.Color
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.knight.kotlin.library_base.ktx.getUser
import com.knight.kotlin.library_base.util.CacheUtils
import com.knight.kotlin.library_util.image.ImageLoader
import com.knight.kotlin.module_mine.R
import com.knight.kotlin.module_mine.entity.CoinRankEntity

/**
 * Author:Knight
 * Time:2022/5/7 14:53
 * Description:RankCoinAdapter
 */
class RankCoinAdapter (data:MutableList<CoinRankEntity>):
    BaseQuickAdapter<CoinRankEntity, BaseViewHolder>(R.layout.mine_coinrank_item,data)  {
    override fun convert(holder: BaseViewHolder, item: CoinRankEntity) {
        item.run {
            holder.setText(R.id.mine_tv_rank,rank)
            holder.setText(R.id.mine_tv_rankusername,username)
            holder.setText(R.id.mine_tv_rankcoincount,coinCount.toString())

            getUser()?.let {
                if (it.id == userId) {
                    holder.setTextColor(R.id.mine_tv_rank,CacheUtils.getThemeColor())
                    holder.setTextColor(R.id.mine_tv_rankusername,CacheUtils.getThemeColor())
                } else {
                    if (CacheUtils.getNormalDark()) {
                        holder.setTextColor(R.id.mine_tv_rank, Color.parseColor("#D3D3D3"))
                        holder.setTextColor(R.id.mine_tv_rankusername,Color.parseColor("#D3D3D3"))

                    } else {
                        holder.setTextColor(R.id.mine_tv_rank,Color.parseColor("#333333"))
                        holder.setTextColor(R.id.mine_tv_rankusername,Color.parseColor("#333333"))
                    }
                }
            } ?: run {
                if (CacheUtils.getNormalDark()) {
                    holder.setTextColor(R.id.mine_tv_rank,Color.parseColor("#D3D3D3"))
                    holder.setTextColor(R.id.mine_tv_rankusername,Color.parseColor("#D3D3D3"))
                } else {
                    holder.setTextColor(R.id.mine_tv_rank,Color.parseColor("#333333"))
                    holder.setTextColor(R.id.mine_tv_rankusername,Color.parseColor("#333333"))
                }
            }

            if ("1" == rank) {
                holder.setVisible(R.id.mine_iv_rank,true)
                ImageLoader.loadLocalPhoto(context,R.drawable.mine_first_points,holder.getView(R.id.mine_iv_rank))
            } else if ("2" == rank) {
                holder.setVisible(R.id.mine_iv_rank,true)
                ImageLoader.loadLocalPhoto(context,R.drawable.mine_second_points,holder.getView(R.id.mine_iv_rank))
            } else if ("3" == rank) {
                holder.setVisible(R.id.mine_iv_rank,true)
                ImageLoader.loadLocalPhoto(context,R.drawable.mine_third_points,holder.getView(R.id.mine_iv_rank))
            } else {
                holder.setVisible(R.id.mine_iv_rank,false)
            }

            holder.setTextColor(R.id.mine_tv_rankcoincount,CacheUtils.getThemeColor())
        }

    }
}