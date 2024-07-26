package com.knight.kotlin.module_eye_video_detail.adapter.provider

import android.app.Activity
import androidx.databinding.DataBindingUtil
import com.chad.library.adapter.base.provider.BaseItemProvider
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.knight.kotlin.library_base.config.Appconfig
import com.knight.kotlin.library_base.config.EyeTypeConstants
import com.knight.kotlin.library_base.entity.EyeItemEntity
import com.knight.kotlin.library_base.ktx.setOnClick
import com.knight.kotlin.library_base.ktx.toJson
import com.knight.kotlin.library_base.route.RouteActivity
import com.knight.kotlin.library_util.startPageWithAnimate
import com.knight.kotlin.module_eye_video_detail.R
import com.knight.kotlin.module_eye_video_detail.databinding.EyeVideoRelateItemBinding

/**
 * Author:Knight
 * Time:2024/7/24 17:48
 * Description:EyeVideoRelateItemProvider
 */
class EyeVideoRelateItemProvider(private val activity: Activity) : BaseItemProvider<EyeItemEntity>() {
    override val itemViewType: Int
        get() = EyeTypeConstants.IMAGE_TYPE
    override val layoutId: Int
        get() = R.layout.eye_video_relate_item


    override fun onViewHolderCreated(viewHolder: BaseViewHolder, viewType: Int) {
        DataBindingUtil.bind<EyeVideoRelateItemBinding>(viewHolder.itemView)
    }
    override fun convert(helper: BaseViewHolder, item: EyeItemEntity) {
        val binding = DataBindingUtil.getBinding<EyeVideoRelateItemBinding>(helper.itemView)
        binding?.model = item.data
        binding?.clRelateItem?.setOnClick{
            startPageWithAnimate(
                activity,
                RouteActivity.EyeVideo.EyeVideoDetail,
                binding.ivCover,
                activity.getString(com.knight.kotlin.library_base.R.string.base_daily_share_image),
                Appconfig.EYE_VIDEO_PARAM_KEY to toJson(item.data)
            )
        }
    }
}