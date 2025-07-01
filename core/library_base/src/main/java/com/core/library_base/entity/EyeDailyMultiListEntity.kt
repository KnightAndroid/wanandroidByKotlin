package com.core.library_base.entity

import androidx.annotation.IntDef

/**
 * Author:Knight
 * Time:2024/4/29 15:51
 * Description:EyeDailyMultiListEntity 日常类型
 */
class EyeDailyMultiListEntity(
    @Type val type: Int,
    val items:List<EyeDailyItemEntity> = mutableListOf()
) {

    @IntDef(value = [Type.EYE_BANNER, Type.EYE_TITLE, Type.EYE_IMAGE])
    @Target(AnnotationTarget.VALUE_PARAMETER)
    @Retention(AnnotationRetention.SOURCE)
    annotation class Type {
        companion object {
            const val EYE_BANNER = 0
            const val EYE_TITLE = 1
            const val EYE_IMAGE = 2
        }
    }
}