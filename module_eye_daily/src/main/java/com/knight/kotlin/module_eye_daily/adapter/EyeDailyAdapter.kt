package com.knight.kotlin.module_eye_daily.adapter


import android.app.Activity
import com.chad.library.adapter.base.BaseProviderMultiAdapter
import com.knight.kotlin.library_base.entity.EyeDailyItemEntity
import com.knight.kotlin.module_eye_daily.adapter.provider.EyeDailyImageItemProvider
import com.knight.kotlin.module_eye_daily.adapter.provider.EyeDailyTextItemProvider
import com.knight.kotlin.library_base.config.EyeTypeConstants

/**
 * Author:Knight
 * Time:2024/5/8 10:06
 * Description:EyeDailyAdapter
 */
class EyeDailyAdapter(activity: Activity):
    BaseProviderMultiAdapter<EyeDailyItemEntity>(){
    init {
        addItemProvider(EyeDailyTextItemProvider())
        addItemProvider(EyeDailyImageItemProvider(activity))
    }
    override fun getItemType(data: List<EyeDailyItemEntity>, position: Int): Int {
        return if (data[position].type == EyeTypeConstants.TEXT_HEAD_TYPE) {
            EyeTypeConstants.TEXT_TYPE
        } else EyeTypeConstants.IMAGE_TYPE
    }

}