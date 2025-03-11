package com.knight.kotlin.module_realtime.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.knight.kotlin.library_base.entity.BaiduCardDataBean
import com.knight.kotlin.library_base.vm.BaseViewModel
import com.knight.kotlin.module_realtime.repo.RealTimeHomeRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * @Description 热搜vm
 * @Author knight
 * @Time 2024/9/28 17:28
 *
 */
@HiltViewModel
class RealTimeHomeVm @Inject constructor(private val mRepo: RealTimeHomeRepo) : BaseViewModel(){


    /**
     * 获取百度热搜数据
     *
     */
    fun getMainBaiduRealTime(): LiveData<BaiduCardDataBean> {
        return mRepo.getMainBaiduRealTime().asLiveData()  //#1DD4EC #4B97F8
    }
}