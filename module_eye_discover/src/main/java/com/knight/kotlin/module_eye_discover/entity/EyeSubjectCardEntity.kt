package com.knight.kotlin.module_eye_discover.entity


/**
 * @author created by luguian
 * @organize
 * @Date 2024/8/7 15:43
 * @descript:栏目卡片实体
 */
data class EyeSubjectCardEntity (
    val type:String,
    val data:EyeCategoryCardDataEntity,
    val id : Int,
    val adIndex:Int,
    val tag:String?
) : BaseEyeDiscoverEntity()




