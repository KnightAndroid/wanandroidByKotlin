package com.knight.kotlin.module_video.vm

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.knight.kotlin.library_base.vm.BaseViewModel
import com.knight.kotlin.module_video.entity.VideoListEntity
import com.knight.kotlin.module_video.repo.VideoRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
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
}