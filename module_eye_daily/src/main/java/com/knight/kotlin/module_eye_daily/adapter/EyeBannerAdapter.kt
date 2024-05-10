package com.knight.kotlin.module_eye_daily.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.knight.kotlin.library_util.DateUtils
import com.knight.kotlin.library_util.image.ImageLoader
import com.knight.kotlin.module_eye_daily.R
import com.knight.kotlin.module_eye_daily.databinding.EyeDailyBannerItemBinding
import com.knight.kotlin.module_eye_daily.entity.EyeDailyItemEntity
import com.youth.banner.adapter.BannerAdapter


/**
 * Author:Knight
 * Time:2024/5/10 10:00
 * Description:EyeBannerAdapter
 */
class EyeBannerAdapter(data:List<EyeDailyItemEntity>) : BannerAdapter<EyeDailyItemEntity,EyeBannerAdapter.BannerViewHolder>(data) {


    class BannerViewHolder(val view: View) :
        RecyclerView.ViewHolder(view) {
        val eye_daily_iv_banner: ImageView = view.findViewById(R.id.eye_daily_iv_banner)
        val tv_daily_banner_title : TextView = view.findViewById(R.id.tv_daily_banner_title)
        val tv_daily_banner_category : TextView = view.findViewById(R.id.tv_daily_banner_category)
        val tv_daily_banner_video_time : TextView = view.findViewById(R.id.tv_daily_banner_video_time)
        }

    override fun onCreateHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        return BannerViewHolder(EyeDailyBannerItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false).root)
    }

    override fun onBindView(
        holder: BannerViewHolder,
        data: EyeDailyItemEntity,
        position: Int,
        size: Int
    ) {
        data.data.content.data.cover?.run {
            ImageLoader.loadStringPhoto(
                holder.itemView.context,
                feed,
                holder.eye_daily_iv_banner
            )

        }
        //标题
        holder.tv_daily_banner_title.setText(data.data.content.data.title)
        //栏目
        holder.tv_daily_banner_category.setText(data.data.content.data.category)
        //视频时间
        holder.tv_daily_banner_video_time.setText(DateUtils.formatDateMsByMS(data.data.content.data.duration * 1000))
    }

}