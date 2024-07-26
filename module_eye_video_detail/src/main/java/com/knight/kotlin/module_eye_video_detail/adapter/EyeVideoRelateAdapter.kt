package com.knight.kotlin.module_eye_video_detail.adapter

import android.app.Activity
import com.chad.library.adapter.base.BaseProviderMultiAdapter
import com.knight.kotlin.library_base.config.EyeTypeConstants
import com.knight.kotlin.library_base.entity.EyeItemEntity
import com.knight.kotlin.module_eye_video_detail.adapter.provider.EyeVideoRelateItemProvider
import com.knight.kotlin.module_eye_video_detail.adapter.provider.EyeVideoRelateTitleProvider

/**
 * Author:Knight
 * Time:2024/7/24 17:06
 * Description:EyeVideoRelateAdapter
 */
class EyeVideoRelateAdapter(activity: Activity) : BaseProviderMultiAdapter<EyeItemEntity>() {

    init {
        addItemProvider(EyeVideoRelateItemProvider(activity))
        addItemProvider(EyeVideoRelateTitleProvider())
    }

    override fun getItemType(data: List<EyeItemEntity>, position: Int): Int {
        return if (data[position].type == EyeTypeConstants.TEXT_HEAD_TYPE) {
            EyeTypeConstants.TEXT_TYPE
        } else EyeTypeConstants.IMAGE_TYPE
    }


}