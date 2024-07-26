package com.knight.kotlin.module_eye_video_detail.adapter.provider

import androidx.databinding.DataBindingUtil
import com.chad.library.adapter.base.provider.BaseItemProvider
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.knight.kotlin.library_base.config.EyeTypeConstants
import com.knight.kotlin.library_base.entity.EyeItemEntity
import com.knight.kotlin.library_util.LogUtils
import com.knight.kotlin.module_eye_video_detail.R
import com.knight.kotlin.module_eye_video_detail.databinding.EyeVideoRelateTitleItemBinding

/**
 * Author:Knight
 * Time:2024/7/24 17:10
 * Description:EyeVideoRelateTitleProvider
 */
class EyeVideoRelateTitleProvider : BaseItemProvider<EyeItemEntity>() {

    override val itemViewType: Int
        get() = EyeTypeConstants.TEXT_TYPE
    override val layoutId: Int
        get() = R.layout.eye_video_relate_title_item


    override fun onViewHolderCreated(viewHolder: BaseViewHolder, viewType: Int) {
        DataBindingUtil.bind<EyeVideoRelateTitleItemBinding>(viewHolder.itemView)
    }

    override fun convert(helper: BaseViewHolder, item: EyeItemEntity) {
        val binding = DataBindingUtil.getBinding<EyeVideoRelateTitleItemBinding>(helper.itemView)
        LogUtils.d(item.data.text)
        binding?.model = item
    }
}