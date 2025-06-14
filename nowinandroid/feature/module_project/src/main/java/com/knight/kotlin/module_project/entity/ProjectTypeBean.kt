package com.knight.kotlin.module_project.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Author:Knight
 * Time:2022/4/28 17:55
 * Description:ProjectTypeBean
 */
@Parcelize
data class ProjectTypeBean (
    var courseId:Int = 0,
    var id:Int = 0,
    var name:String = "",
    var order:Int = 0,
    var parentChapterId :Int = 0,
    var userControlSetTop:Boolean = false,
    var visible:Int = 0
    ): Parcelable