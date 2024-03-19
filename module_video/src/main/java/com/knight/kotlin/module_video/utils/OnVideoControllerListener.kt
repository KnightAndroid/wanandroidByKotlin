package com.knight.kotlin.module_video.utils

/**
 * Author:Knight
 * Time:2024/3/18 16:57
 * Description:OnVideoControllerListener
 */
interface OnVideoControllerListener {
    fun onHeadClick()

    fun onLikeClick()

    fun onCommentClick(jokeId:Long)

    fun onShareClick()
}