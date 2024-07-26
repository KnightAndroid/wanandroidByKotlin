package com.knight.kotlin.module_eye_daily.adapter.provider

import android.content.res.ColorStateList
import androidx.databinding.DataBindingUtil
import com.chad.library.adapter.base.provider.BaseItemProvider
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.knight.kotlin.library_base.config.EyeTypeConstants
import com.knight.kotlin.library_base.entity.EyeDailyItemEntity
import com.knight.kotlin.library_base.util.CacheUtils
import com.knight.kotlin.module_eye_daily.R
import com.knight.kotlin.module_eye_daily.databinding.EyeDailyTextItemBinding

/**
 * Author:Knight
 * Time:2024/5/13 16:29
 * Description:EyeDailyTextItemProvider
 */
class EyeDailyTextItemProvider: BaseItemProvider<EyeDailyItemEntity>() {
    override val itemViewType: Int
        get() = EyeTypeConstants.TEXT_TYPE
    override val layoutId: Int
        get() = R.layout.eye_daily_text_item

    override fun onViewHolderCreated(viewHolder: BaseViewHolder, viewType: Int) {
        DataBindingUtil.bind<EyeDailyTextItemBinding>(viewHolder.itemView)
    }
    override fun convert(helper: BaseViewHolder, item: EyeDailyItemEntity) {
        val binding = DataBindingUtil.getBinding<EyeDailyTextItemBinding>(helper.itemView)
        binding?.model = item.data
        binding?.imageLocal = R.drawable.eye_daily_title_icon
        binding?.colorStateList = ColorStateList.valueOf(CacheUtils.getThemeColor())
    }
}