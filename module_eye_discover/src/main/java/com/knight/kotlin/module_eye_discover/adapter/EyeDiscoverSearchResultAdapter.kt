package com.knight.kotlin.module_eye_discover.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseMultiItemAdapter
import com.knight.kotlin.library_base.config.EyeCardType
import com.knight.kotlin.library_base.config.EyeSearchResultType
import com.knight.kotlin.library_base.entity.EyeCommonVideoEntity
import com.knight.kotlin.library_base.entity.EyeMetroCard
import com.knight.kotlin.library_base.ktx.json
import com.knight.kotlin.module_eye_discover.databinding.EyeDiscoverSearchResultVideoItemBinding
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement

/**
 * @Description 搜索结果适配器
 * @Author knight
 * @Time 2025/1/15 22:38
 *
 */

class EyeDiscoverSearchResultAdapter(data :List<EyeMetroCard<JsonObject>>) : BaseMultiItemAdapter<EyeMetroCard<JsonObject>>(data) {

    //类型1 视频 viewHolder
    class EyeDiscoverSearchResultVideoVH(viewBinding: EyeDiscoverSearchResultVideoItemBinding): RecyclerView.ViewHolder(viewBinding.root)


    init {
        addItemType(EyeSearchResultType.VIDEO,object:OnMultiItemAdapterListener<EyeMetroCard<JsonObject>,EyeDiscoverSearchResultVideoVH> {
            override fun onBind(
                holder: EyeDiscoverSearchResultVideoVH,
                position: Int,
                item: EyeMetroCard<JsonObject>?
            ) {
                val video = item?.metro_data?.let { json.decodeFromJsonElement<EyeCommonVideoEntity>(it) }
            }

            override fun onCreate(
                context: Context,
                parent: ViewGroup,
                viewType: Int
            ): EyeDiscoverSearchResultVideoVH {

                val viewBinding = EyeDiscoverSearchResultVideoItemBinding.inflate(LayoutInflater.from(context), parent, false)
                return EyeDiscoverSearchResultVideoVH(viewBinding)
            }


        }).onItemViewType {position,list ->
            if (list[position].style?.tpl_label == EyeCardType.FEED_COVER_SMALL_VIDEO) {
                EyeSearchResultType.VIDEO
            }else{
                EyeSearchResultType.PGC
            }

        }

    }

}