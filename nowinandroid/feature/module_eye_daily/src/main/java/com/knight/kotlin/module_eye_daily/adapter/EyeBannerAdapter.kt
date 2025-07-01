package com.knight.kotlin.module_eye_daily.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.core.library_base.entity.EyeDailyItemEntity
import com.knight.kotlin.module_eye_daily.databinding.EyeDailyBannerItemBinding
import com.youth.banner.adapter.BannerAdapter


/**
 * Author:Knight
 * Time:2024/5/10 10:00
 * Description:EyeBannerAdapter
 */
abstract class EyeBannerAdapter(context: Context, data:List<EyeDailyItemEntity>) : BannerAdapter<EyeDailyItemEntity,EyeBannerAdapter.BannerViewHolder>(data) {
    private val mInflater = LayoutInflater.from(context)
    class BannerViewHolder(val binding: EyeDailyBannerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        }
    override fun onCreateHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val binding = EyeDailyBannerItemBinding.inflate(mInflater, parent, false)
        return BannerViewHolder(binding)
    }
}