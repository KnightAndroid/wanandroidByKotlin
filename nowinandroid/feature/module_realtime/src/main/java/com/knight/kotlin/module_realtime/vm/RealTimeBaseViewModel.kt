package com.knight.kotlin.module_realtime.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.core.library_base.vm.BaseViewModel
import com.knight.kotlin.library_base.entity.BaiduCardDataBean
import com.knight.kotlin.module_realtime.repo.RealTimeBaseRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


/**
 * @author created by luguian
 * @organize
 * @Date 2025/3/20 9:58
 * @descript:BaseViewmodel
 */
@HiltViewModel
open class RealTimeBaseViewModel @Inject constructor(private val mRepo: RealTimeBaseRepo) : BaseViewModel() {






    /**
     * 获取百度热搜数据
     *
     */
    fun getDataByTab(platform:String,tab:String): LiveData<BaiduCardDataBean> {
        return mRepo.getDataByTab(platform,tab).asLiveData()  //#1DD4EC #4B97F8
    }

    /**
     * 获取子页面热搜数据
     *
     */
    fun getChildDataByTab(platform:String,tab:String,tag:String): LiveData<BaiduCardDataBean> {
        return mRepo.getChildDataByTab(platform,tab,tag).asLiveData()  //#1DD4EC #4B97F8
    }
}