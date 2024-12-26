package com.knight.kotlin.module_eye_discover.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseQuickAdapter
import com.knight.kotlin.library_base.entity.EyeCommonVideoEntity
import com.knight.kotlin.library_base.ktx.json
import com.knight.kotlin.module_eye_discover.databinding.EyeDiscoverSearchRecommendVideoItemBinding
import com.knight.kotlin.module_eye_discover.entity.EyeDiscoverMetroCard
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement
/**
 * @Description 推荐视频适配器
 * @Author knight
 * @Time 2024/12/26 21:37
 *
 */

class EyeDiscoverSearchRecommendVideoAdapter : BaseQuickAdapter<EyeDiscoverMetroCard<JsonObject>,EyeDiscoverSearchRecommendVideoAdapter.VH>() {


    class VH(
        parent: ViewGroup,
        val binding: EyeDiscoverSearchRecommendVideoItemBinding = EyeDiscoverSearchRecommendVideoItemBinding .inflate(
            LayoutInflater.from(parent.context), parent, false
        ),
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(
        holder: VH,
        position: Int,
        item: EyeDiscoverMetroCard<JsonObject>?
    ) {
        val video = item?.metro_data?.let { json.decodeFromJsonElement<EyeCommonVideoEntity>(it) }
        holder.binding.viewModel = video
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH {
        return VH(parent)
    }
}