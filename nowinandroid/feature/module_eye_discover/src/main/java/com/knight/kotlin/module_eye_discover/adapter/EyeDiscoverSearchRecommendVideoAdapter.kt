package com.knight.kotlin.module_eye_discover.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseQuickAdapter
import com.core.library_base.entity.EyeCommonVideoEntity
import com.core.library_base.entity.EyeMetroCard
import com.core.library_base.ktx.json
import com.knight.kotlin.module_eye_discover.databinding.EyeDiscoverSearchRecommendVideoItemBinding

import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement
/**
 * @Description 推荐视频适配器
 * @Author knight
 * @Time 2024/12/26 21:37
 *
 */

class EyeDiscoverSearchRecommendVideoAdapter : BaseQuickAdapter<EyeMetroCard<JsonObject>,EyeDiscoverSearchRecommendVideoAdapter.VH>() {


    class VH(
        parent: ViewGroup,
        val binding: EyeDiscoverSearchRecommendVideoItemBinding = EyeDiscoverSearchRecommendVideoItemBinding .inflate(
            LayoutInflater.from(parent.context), parent, false
        ),
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(
        holder: VH,
        position: Int,
        item: EyeMetroCard<JsonObject>?
    ) {
        val video = item?.metro_data?.let { json.decodeFromJsonElement<EyeCommonVideoEntity>(it) }
        holder.binding.viewModel = video
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH {
        return VH(parent)
    }
}