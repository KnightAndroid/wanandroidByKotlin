package com.knight.kotlin.module_eye_video_detail.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseMultiItemAdapter
import com.knight.kotlin.library_base.R
import com.knight.kotlin.library_base.config.Appconfig
import com.knight.kotlin.library_base.config.EyeTypeConstants
import com.knight.kotlin.library_base.entity.EyeItemEntity
import com.knight.kotlin.library_base.ktx.setOnClick
import com.knight.kotlin.library_base.ktx.toJson
import com.knight.kotlin.library_base.route.RouteActivity
import com.knight.kotlin.library_util.startPageWithAnimate
import com.knight.kotlin.module_eye_video_detail.databinding.EyeVideoRelateItemBinding
import com.knight.kotlin.module_eye_video_detail.databinding.EyeVideoRelateTitleItemBinding

/**
 * Author:Knight
 * Time:2024/7/24 17:06
 * Description:EyeVideoRelateAdapter
 */
class EyeVideoRelateAdapter(data: List<EyeItemEntity>,activity: Activity) : BaseMultiItemAdapter<EyeItemEntity>(data) {


    // 标题 的 viewholder
    class EyeVideoRelateTitleVH(val viewBinding: EyeVideoRelateTitleItemBinding) : RecyclerView.ViewHolder(viewBinding.root)

    // 正文视频 的 viewholder
    class EyeVideoRelateItemVH(val viewBinding: EyeVideoRelateItemBinding) : RecyclerView.ViewHolder(viewBinding.root)



    // 在 init 初始化的时候，添加多类型
    init {
        addItemType(EyeTypeConstants.TEXT_TYPE, object : OnMultiItemAdapterListener<EyeItemEntity, EyeVideoRelateTitleVH> { // 类型 1
            override fun onCreate(context: Context, parent: ViewGroup, viewType: Int): EyeVideoRelateTitleVH {
                // 创建 viewholder
                val viewBinding = EyeVideoRelateTitleItemBinding.inflate(LayoutInflater.from(context), parent, false)
                return EyeVideoRelateTitleVH(viewBinding)
            }

            override fun onBind(holder: EyeVideoRelateTitleVH, position: Int, item: EyeItemEntity?) {
                // 绑定 item 数据
                val binding = DataBindingUtil.getBinding<EyeVideoRelateTitleItemBinding>(holder.itemView)
                binding?.model = item
            }
        }).addItemType(EyeTypeConstants.IMAGE_TYPE, object : OnMultiItemAdapterListener<EyeItemEntity, EyeVideoRelateItemVH> { // 类型 2
            override fun onCreate(context: Context, parent: ViewGroup, viewType: Int):EyeVideoRelateItemVH {
                // 创建 viewholder
                val viewBinding = EyeVideoRelateItemBinding.inflate(LayoutInflater.from(context), parent, false)
                return EyeVideoRelateItemVH(viewBinding)
            }

            override fun onBind(holder: EyeVideoRelateItemVH, position: Int, item: EyeItemEntity?) {
                // 绑定 item 数据
                val binding = DataBindingUtil.getBinding<EyeVideoRelateItemBinding>(holder.itemView)
                binding?.model = item?.data
                binding?.clRelateItem?.setOnClick{
                    startPageWithAnimate(
                        activity,
                        RouteActivity.EyeVideo.EyeVideoDetail,
                        binding.ivCover,
                        activity.getString(R.string.base_daily_share_image),
                        Appconfig.EYE_VIDEO_PARAM_KEY to toJson(item?.data!!)
                    )
                }
            }

            override fun isFullSpanItem(itemType: Int): Boolean {
                // 使用GridLayoutManager时，此类型的 item 是否是满跨度
                return true
            }

        }).onItemViewType { position, list -> // 根据数据，返回对应的 ItemViewType
            if (position == list.size) {
                if (list[position - 1].type == EyeTypeConstants.TEXT_HEAD_TYPE) {
                    EyeTypeConstants.TEXT_TYPE
                } else EyeTypeConstants.IMAGE_TYPE
            } else {
                if (list[position].type == EyeTypeConstants.TEXT_HEAD_TYPE) {
                    EyeTypeConstants.TEXT_TYPE
                } else EyeTypeConstants.IMAGE_TYPE
            }

        }
    }

}