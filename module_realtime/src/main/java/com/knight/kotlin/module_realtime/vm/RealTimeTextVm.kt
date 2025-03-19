package com.knight.kotlin.module_realtime.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.knight.kotlin.library_base.entity.BaiduCardDataBean
import com.knight.kotlin.library_base.vm.BaseViewModel
import com.knight.kotlin.module_realtime.repo.RealTimeTextRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


/**
 * @author created by luguian
 * @organize
 * @Date 2025/3/19 16:40
 * @descript:
 */
@HiltViewModel
class RealTimeTextVm @Inject constructor(private val mRepo: RealTimeTextRepo) : BaseViewModel(){


    /**
     * 获取百度热搜数据
     *
     */
    fun getDataByTab(platform:String,tab:String): LiveData<BaiduCardDataBean> {
        return mRepo.getDataByTab(platform,tab).asLiveData()  //#1DD4EC #4B97F8
    }
}