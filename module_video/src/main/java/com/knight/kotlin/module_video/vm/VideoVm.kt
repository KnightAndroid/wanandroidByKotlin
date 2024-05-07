package com.knight.kotlin.module_video.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.knight.kotlin.library_base.vm.BaseViewModel
import com.knight.kotlin.module_video.entity.VideoCommentList
import com.knight.kotlin.module_video.entity.VideoListEntity
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
     *
     * 获取推荐视频
     */
    fun getDouyinVideos() : LiveData<MutableList<VideoListEntity>> {
        return mVideoRepo.getDouyinVideos().asLiveData()
    }

    /**
     * 获取视频评论列表
     *
     *
     * @param jokeId
     * @param page
     */
    fun getVideoCommentList(jokeId:Long, page:Long) : LiveData<VideoCommentList> {
        return mVideoRepo.getVideoCommentList(jokeId, page).asLiveData()
    }
}