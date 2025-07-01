package com.knight.kotlin.module_video.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.core.library_base.entity.EyeDailyListEntity
import com.core.library_base.ktx.showLoadingDialog
import com.core.library_base.vm.BaseViewModel
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.module_video.entity.VideoCommentList
import com.knight.kotlin.module_video.repo.VideoRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2024/2/26 14:18
 * Description:VideoVm
 */
@HiltViewModel
class VideoVm @Inject constructor(private val mVideoRepo : VideoRepo) : BaseViewModel(){


    /**
     * 获取推荐视频
     */
    fun getVideos():LiveData<EyeDailyListEntity> {
        showLoadingDialog()
        return mVideoRepo.getVideos(failureCallBack = {
            it?.let { it1 -> toast(it1) }
        }).asLiveData()
    }


    /**
     * 获取视频评论列表
     *
     *
     * @param jokeId
     * @param page
     */
    fun getVideoCommentList(jokeId:Long) : LiveData<VideoCommentList> {
        return mVideoRepo.getVideoCommentList(jokeId).asLiveData()
    }
}