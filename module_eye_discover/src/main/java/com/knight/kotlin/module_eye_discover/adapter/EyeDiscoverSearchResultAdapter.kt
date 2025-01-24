package com.knight.kotlin.module_eye_discover.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseMultiItemAdapter
import com.google.android.material.R
import com.knight.kotlin.library_base.config.EyeCardType
import com.knight.kotlin.library_base.config.EyeSearchResultType
import com.knight.kotlin.library_base.entity.EyeCommonAuthorEntity
import com.knight.kotlin.library_base.entity.EyeCommonGraphicEntity
import com.knight.kotlin.library_base.entity.EyeCommonTopicEntity
import com.knight.kotlin.library_base.entity.EyeCommonVideoEntity
import com.knight.kotlin.library_base.entity.EyeMetroCard
import com.knight.kotlin.library_base.ktx.json
import com.knight.kotlin.module_eye_discover.databinding.EyeDiscoverSearchResultAuthorItemBinding
import com.knight.kotlin.module_eye_discover.databinding.EyeDiscoverSearchResultGraphicItemBinding
import com.knight.kotlin.module_eye_discover.databinding.EyeDiscoverSearchResultTopicItemBinding
import com.knight.kotlin.module_eye_discover.databinding.EyeDiscoverSearchResultVideoItemBinding
import com.knight.kotlin.module_eye_discover.databinding.EyeDiscoverTestBinding
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
    //类型 2 作者 viewHolder
    class EyeDiscoverSearchResultAuthorVH(viewBinding: EyeDiscoverSearchResultAuthorItemBinding): RecyclerView.ViewHolder(viewBinding.root)
    //类型3 图文 viewHolder
    class EyeDiscoverSearchResultGraphicVH(viewBinding : EyeDiscoverSearchResultGraphicItemBinding) : RecyclerView.ViewHolder(viewBinding.root)
    //类型3 话题 viewHolder
    class EyeDiscoverSearchResultTopicVH(viewBinding : EyeDiscoverSearchResultTopicItemBinding) : RecyclerView.ViewHolder(viewBinding.root)
    class EyeDiscoverSearchResultTestVH(viewBinding: EyeDiscoverTestBinding): RecyclerView.ViewHolder(viewBinding.root)
    //类型4 话题
    init {
        addItemType(EyeSearchResultType.VIDEO,object:OnMultiItemAdapterListener<EyeMetroCard<JsonObject>,EyeDiscoverSearchResultVideoVH> {
            override fun onBind(
                holder: EyeDiscoverSearchResultVideoVH,
                position: Int,
                item: EyeMetroCard<JsonObject>?
            ) {
                val binding = DataBindingUtil.getBinding<EyeDiscoverSearchResultVideoItemBinding>(holder.itemView)
                val video = item?.metro_data?.let { json.decodeFromJsonElement<EyeCommonVideoEntity>(it) }
                binding?.viewModel = video
            }

            override fun onCreate(
                context: Context,
                parent: ViewGroup,
                viewType: Int
            ): EyeDiscoverSearchResultVideoVH {

                val viewBinding = EyeDiscoverSearchResultVideoItemBinding.inflate(LayoutInflater.from(context), parent, false)
                return EyeDiscoverSearchResultVideoVH(viewBinding)
            }


        }).addItemType(EyeSearchResultType.PGC,object:OnMultiItemAdapterListener<EyeMetroCard<JsonObject>,EyeDiscoverSearchResultAuthorVH> {
            override fun onBind(
                holder:EyeDiscoverSearchResultAuthorVH,
                position: Int,
                item: EyeMetroCard<JsonObject>?
            ) {
                val binding = DataBindingUtil.getBinding<EyeDiscoverSearchResultAuthorItemBinding>(holder.itemView)
                val author = item?.metro_data?.let { json.decodeFromJsonElement<EyeCommonAuthorEntity>(it) }
                binding?.viewModel = author
            }

            override fun onCreate(
                context: Context,
                parent: ViewGroup,
                viewType: Int
            ): EyeDiscoverSearchResultAuthorVH {

                val viewBinding = EyeDiscoverSearchResultAuthorItemBinding.inflate(LayoutInflater.from(context), parent, false)
                return EyeDiscoverSearchResultAuthorVH(viewBinding)
            }


        }).addItemType(EyeSearchResultType.GRAPHIC,object:OnMultiItemAdapterListener<EyeMetroCard<JsonObject>,EyeDiscoverSearchResultGraphicVH> {
            override fun onBind(
                holder:EyeDiscoverSearchResultGraphicVH,
                position: Int,
                item: EyeMetroCard<JsonObject>?
            ) {
                val binding = DataBindingUtil.getBinding<EyeDiscoverSearchResultGraphicItemBinding>(holder.itemView)
                val graphicEntity = item?.metro_data?.let { json.decodeFromJsonElement<EyeCommonGraphicEntity>(it) }
                binding?.viewModel = graphicEntity
            }

            override fun onCreate(
                context: Context,
                parent: ViewGroup,
                viewType: Int
            ): EyeDiscoverSearchResultGraphicVH {

                val viewBinding = EyeDiscoverSearchResultGraphicItemBinding.inflate(LayoutInflater.from(context), parent, false)
                return EyeDiscoverSearchResultGraphicVH(viewBinding)
            }


        }).addItemType(EyeSearchResultType.TOPIC,object:OnMultiItemAdapterListener<EyeMetroCard<JsonObject>,EyeDiscoverSearchResultTopicVH> {
            override fun onBind(
                holder:EyeDiscoverSearchResultTopicVH,
                position: Int,
                item: EyeMetroCard<JsonObject>?
            ) {
                val binding = DataBindingUtil.getBinding<EyeDiscoverSearchResultTopicItemBinding>(holder.itemView)
                 val topic = item?.metro_data?.let { json.decodeFromJsonElement<EyeCommonTopicEntity>(it) }
                  binding?.viewModel = topic

                binding?.tags?.removeAllViews()
                topic?.tags?.forEach { tag ->
                    val view =
                        TextView(context, null, R.attr.textAppearanceLabelSmall).also {
                            it.text = tag.title
                        }
                    binding?.tags?.addView(view)
                }
            }

            override fun onCreate(
                context: Context,
                parent: ViewGroup,
                viewType: Int
            ): EyeDiscoverSearchResultTopicVH {

                val viewBinding = EyeDiscoverSearchResultTopicItemBinding.inflate(LayoutInflater.from(context), parent, false)
                return EyeDiscoverSearchResultTopicVH(viewBinding)
            }


        }).addItemType(EyeSearchResultType.UGC,object:OnMultiItemAdapterListener<EyeMetroCard<JsonObject>,EyeDiscoverSearchResultTestVH> {
            override fun onBind(
                holder:EyeDiscoverSearchResultTestVH,
                position: Int,
                item: EyeMetroCard<JsonObject>?
            ) {
                val bing = DataBindingUtil.getBinding<EyeDiscoverTestBinding>(holder.itemView)
                // val video = item?.metro_data?.let { json.decodeFromJsonElement<EyeCommonVideoEntity>(it) }
                //  binding?.viewModel = video
            }

            override fun onCreate(
                context: Context,
                parent: ViewGroup,
                viewType: Int
            ): EyeDiscoverSearchResultTestVH {

                val viewBinding = EyeDiscoverTestBinding.inflate(LayoutInflater.from(context), parent, false)
                return EyeDiscoverSearchResultTestVH(viewBinding)
            }


        }).onItemViewType {position,list ->
            if (list[position].style?.tpl_label == EyeCardType.FEED_COVER_SMALL_VIDEO) {
                EyeSearchResultType.VIDEO
            }else if (list[position].style?.tpl_label == EyeCardType.FEED_USER){
                EyeSearchResultType.PGC
            } else if(list[position].style?.tpl_label == EyeCardType.SEARCH_RESULT_IMAGE){
                EyeSearchResultType.GRAPHIC
            } else if(list[position].style?.tpl_label == EyeCardType.FEED_COVER_DETAIL_TOPIC){
                EyeSearchResultType.TOPIC
            } else {
                EyeSearchResultType.UGC
            }

        }

    }

}