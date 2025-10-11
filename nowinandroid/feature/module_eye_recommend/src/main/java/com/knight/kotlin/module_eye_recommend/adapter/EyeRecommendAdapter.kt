package com.knight.kotlin.module_eye_recommend.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseMultiItemAdapter
import com.knight.kotlin.library_base.config.EyeCardType
import com.knight.kotlin.library_base.config.EyeRecommendVideoType
import com.knight.kotlin.library_base.entity.EyeCardEntity
import com.knight.kotlin.module_eye_recommend.databinding.EyeRecommendRvLargeVideoItemBinding
import com.knight.kotlin.module_eye_recommend.databinding.EyeRecommendRvSmallVideoItemBinding


/**
 * @author created by luguian
 * @organize
 * @Date 2025/9/19 11:19
 * @descript:开眼推荐页面适配器 具体逻辑参考EyeSquareAdapter
 */
class EyeRecommendAdapter(data:List<EyeCardEntity>): BaseMultiItemAdapter<EyeCardEntity>(data) {

    //类型1 大视频(包含广告视频)
    class EyeCoverLargeVideoVH(viewBinding: EyeRecommendRvLargeVideoItemBinding) : RecyclerView.ViewHolder(viewBinding.root)

    //类型2 小视频
    class EyeCoverSmallVideoVH(viewBinding: EyeRecommendRvSmallVideoItemBinding) : RecyclerView.ViewHolder(viewBinding.root)

    init {
        addItemType(EyeRecommendVideoType.COVERLARGEVIDEO, object : OnMultiItemAdapterListener<EyeCardEntity, EyeCoverLargeVideoVH> {
            override fun onBind(
                holder: EyeCoverLargeVideoVH,
                position: Int,
                item: EyeCardEntity?
            ) {
                TODO("Not yet implemented")
            }

            override fun onCreate(
                context: Context,
                parent: ViewGroup,
                viewType: Int
            ): EyeCoverLargeVideoVH {
                TODO("Not yet implemented")
            }
        }).addItemType(EyeRecommendVideoType.COVERSMALLVIDEO, object : OnMultiItemAdapterListener<EyeCardEntity, EyeCoverSmallVideoVH> {
            override fun onBind(
                holder: EyeCoverSmallVideoVH,
                position: Int,
                item: EyeCardEntity?
            ) {
                TODO("Not yet implemented")
            }

            override fun onCreate(
                context: Context,
                parent: ViewGroup,
                viewType: Int
            ): EyeCoverSmallVideoVH {
                TODO("Not yet implemented")
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