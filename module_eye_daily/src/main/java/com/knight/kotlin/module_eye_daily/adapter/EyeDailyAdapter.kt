package com.knight.kotlin.module_eye_daily.adapter


import com.chad.library.adapter.base.BaseProviderMultiAdapter
import com.knight.kotlin.library_base.entity.EyeDailyItemEntity
import com.knight.kotlin.module_eye_daily.adapter.provider.EyeDailyImageItemProvider
import com.knight.kotlin.module_eye_daily.adapter.provider.EyeDailyTextItemProvider
import com.knight.kotlin.module_eye_daily.constants.EyeDailyConstants

/**
 * Author:Knight
 * Time:2024/5/8 10:06
 * Description:EyeDailyAdapter
 */
class EyeDailyAdapter:
    BaseProviderMultiAdapter<EyeDailyItemEntity>(){
    init {
        addItemProvider(EyeDailyTextItemProvider())
        addItemProvider(EyeDailyImageItemProvider())
    }
    override fun getItemType(data: List<EyeDailyItemEntity>, position: Int): Int {
        return if (data[position].type == EyeDailyConstants.TEXT_HEAD_TYPE) {
            EyeDailyConstants.TEXT_TYPE
        } else EyeDailyConstants.IMAGE_TYPE
    }

}