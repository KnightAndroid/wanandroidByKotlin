package com.knight.kotlin.module_video.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.knight.kotlin.library_base.vm.BaseViewModel
import com.knight.kotlin.module_video.entity.VideoCommentList
import com.knight.kotlin.module_video.entity.VideoListEntity
import com.knight.kotlin.module_video.repo.VideoRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2024/2/26 14:18
 * Description:VideoVm
 */
@HiltViewModel
class VideoVm @Inject constructor(private val mVideoRepo : VideoRepo) : BaseViewModel(){

    val videos = MutableLiveData<MutableList<VideoListEntity>>()


    /**
     *
     * 获取推荐视频
     */
    fun getDouyinVideos() {
        viewModelScope.launch(Dispatchers.IO) {
            mVideoRepo.getDouyinVideos()
                .catch {

                }
                .collect{
                    videos.postValue(it)
                }


        }
    }

    /**
     * 获取视频评论列表
     *
     *
     * @param jokeId
     * @param page
     */
    fun getVideoCommentList(jokeId:Long, page:Long, successCallBack:(VideoCommentList)->Unit, failureCallBack:((String?) ->Unit) ?= null) {
        viewModelScope.launch {
            mVideoRepo.getVideoCommentList(jokeId, page)
                //指定线程
                .flowOn(Dispatchers.IO)
                .onStart {
                    //开始
                }
                .onEach {
                    successCallBack(it)
                }
                .onCompletion {
                    //结束
                }
                .catch {
                    failureCallBack?.let { it1 -> it1(it.message) }
                }
                .collect()


        }
    }
}