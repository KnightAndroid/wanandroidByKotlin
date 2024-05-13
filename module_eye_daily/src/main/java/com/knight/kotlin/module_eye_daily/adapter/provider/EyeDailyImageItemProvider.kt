package com.knight.kotlin.module_eye_daily.adapter.provider

import androidx.databinding.DataBindingUtil
import com.chad.library.adapter.base.provider.BaseItemProvider
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.knight.kotlin.library_base.entity.EyeDailyItemEntity
import com.knight.kotlin.module_eye_daily.R
import com.knight.kotlin.module_eye_daily.constants.EyeDailyConstants
import com.knight.kotlin.module_eye_daily.databinding.EyeDailyImageItemBinding

/**
 * Author:Knight
 * Time:2024/5/13 16:02
 * Description:EyeDailyImageItemProvider
 */
class EyeDailyImageItemProvider:
    BaseItemProvider<EyeDailyItemEntity>() {
    override val itemViewType: Int
        get() = EyeDailyConstants.IMAGE_TYPE
    override val layoutId: Int
        get() = R.layout.eye_daily_image_item

    override fun onViewHolderCreated(viewHolder: BaseViewHolder, viewType: Int) {
        DataBindingUtil.bind<EyeDailyImageItemBinding>(viewHolder.itemView)
    }

    override fun convert(helper: BaseViewHolder, item: EyeDailyItemEntity) {
        val binding = DataBindingUtil.getBinding<EyeDailyImageItemBinding>(helper.itemView)
        binding?.model = item.data?.content
    }
}