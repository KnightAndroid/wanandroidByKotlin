package com.knight.kotlin.module_eye_recommend.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseMultiItemAdapter
import com.knight.kotlin.library_base.config.EyeCardType
import com.knight.kotlin.library_base.config.EyeRecommendVideoType
import com.knight.kotlin.library_base.entity.EyeCardEntity
import com.knight.kotlin.library_widget.ktx.init
import com.knight.kotlin.module_eye_recommend.databinding.EyeRecommendRvLargeVideoItemBinding
import com.knight.kotlin.module_eye_recommend.databinding.EyeRecommendRvSmallVideoItemBinding
import com.knight.kotlin.module_eye_recommend.entity.EyeRecommendVideoEntity
import kotlinx.serialization.json.Json


/**
 * @author created by luguian
 * @organize
 * @Date 2025/9/19 11:19
 * @descript:开眼推荐页面适配器 具体逻辑参考EyeSquareAdapter
 */
class EyeRecommendAdapter(private val activity: Activity, data:List<EyeCardEntity>): BaseMultiItemAdapter<EyeCardEntity>(data) {

    //类型1 大视频(包含广告视频)
    class EyeCoverLargeVideoVH(viewBinding: EyeRecommendRvLargeVideoItemBinding) : RecyclerView.ViewHolder(viewBinding.root)

    //类型2 小视频
    class EyeCoverSmallVideoVH(viewBinding: EyeRecommendRvSmallVideoItemBinding) : RecyclerView.ViewHolder(viewBinding.root)

    init {
        //大视频布局
        addItemType(EyeRecommendVideoType.COVERLARGEVIDEO, object : OnMultiItemAdapterListener<EyeCardEntity, EyeCoverLargeVideoVH> {
            override fun onBind(
                holder: EyeCoverLargeVideoVH,
                position: Int,
                item: EyeCardEntity?
            ) {
                val binding = DataBindingUtil.getBinding<EyeRecommendRvLargeVideoItemBinding>(holder.itemView)
                val largeVideoLists:List<EyeRecommendVideoEntity>? = item?.card_data?.body?.metro_list?.map {
                    eyeMetroCard ->
                    // 将JsonObject转换为JSON字符串
                    val jsonString = eyeMetroCard.metro_data.toString()
                    Json.decodeFromString<EyeRecommendVideoEntity>(jsonString)
                }
                val mEyeRecommendLargeItemAdapter = EyeRecommendLargeVideoAdapter(activity)
                binding?.rvEyeRecommendLargeVideo?.init(
                    LinearLayoutManager(context),
                    mEyeRecommendLargeItemAdapter,
                    false
                )
                mEyeRecommendLargeItemAdapter.submitList(largeVideoLists)
            }

            override fun onCreate(
                context: Context,
                parent: ViewGroup,
                viewType: Int
            ): EyeCoverLargeVideoVH {
                val viewBinding = EyeRecommendRvLargeVideoItemBinding.inflate(LayoutInflater.from(context), parent, false)
                return EyeCoverLargeVideoVH(viewBinding)
            }
        }).addItemType(EyeRecommendVideoType.COVERSMALLVIDEO, object : OnMultiItemAdapterListener<EyeCardEntity, EyeCoverSmallVideoVH> {
            override fun onBind(
                holder: EyeCoverSmallVideoVH,
                position: Int,
                item: EyeCardEntity?
            ) {
                val binding = DataBindingUtil.getBinding<EyeRecommendRvSmallVideoItemBinding>(holder.itemView)
                val largeVideoLists:List<EyeRecommendVideoEntity>? = item?.card_data?.body?.metro_list?.map {
                        eyeMetroCard ->
                    // 将JsonObject转换为JSON字符串
                    val jsonString = eyeMetroCard.metro_data.toString()
                    Json.decodeFromString<EyeRecommendVideoEntity>(jsonString)
                }
                val mEyeRecommendSmallItemAdapter = EyeRecommendSmallVideoAdapter(activity)
                binding?.rvEyeRecommendSmallVideo?.init(
                    LinearLayoutManager(context),
                    mEyeRecommendSmallItemAdapter,
                    false
                )
                mEyeRecommendSmallItemAdapter.submitList(largeVideoLists)
            }

            override fun onCreate(
                context: Context,
                parent: ViewGroup,
                viewType: Int
            ): EyeCoverSmallVideoVH {
                val viewBinding = EyeRecommendRvSmallVideoItemBinding.inflate(LayoutInflater.from(context), parent, false)
                return EyeCoverSmallVideoVH(viewBinding)
            }
        }).onItemViewType { position, list ->
            val tplLabel = list.getOrNull(position)
                ?.card_data?.body?.metro_list
                ?.getOrNull(0)?.style?.tpl_label

            if (tplLabel == EyeCardType.FEED_COVER_LARGE_VIDEO) {
                EyeRecommendVideoType.COVERLARGEVIDEO
            } else {
                EyeRecommendVideoType.COVERSMALLVIDEO
            }
        }

    }
}