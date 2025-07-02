package com.knight.kotlin.module_eye_video_detail.util


import com.core.library_base.util.dp2px
import com.knight.kotlin.library_base.ktx.getScreenHeight
import com.knight.kotlin.library_base.ktx.getScreenWidth

/**
 * Author:Knight
 * Time:2024/7/22 11:27
 * Description:BlurredLoadUrlUtils
 */
object BlurredLoadUrlUtils {

    /**
     * 加载模糊背景大图
     *
     * @param blurred
     * @return
     */
    fun loadBlurredUrl(blurred:String):String {
        val width:Int = getScreenWidth()
        val height:Int = getScreenHeight() - 250.dp2px()
        return "${blurred}/thumbnail/${height}x${width}"
    }
}