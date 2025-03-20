package com.knight.kotlin.module_realtime.repo

import com.knight.kotlin.library_base.entity.BaiduCardDataBean
import com.knight.kotlin.library_base.repository.BaseRepository
import com.knight.kotlin.library_network.model.responseCodeExceptionHandler
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.module_realtime.api.RealTimeApiService
import javax.inject.Inject


/**
 * @author created by luguian
 * @organize
 * @Date 2025/3/20 9:55
 * @descript:
 */
open class RealTimeBaseRepo  @Inject constructor(): BaseRepository() {

    @Inject
    lateinit var mRealTimeApiService: RealTimeApiService

    /**
     *
     * 根据tab获取数据
     */
    fun getDataByTab(platform:String,tab:String) = request<BaiduCardDataBean>({
        mRealTimeApiService.getDataByTab(platform,tab).run {
            responseCodeExceptionHandler(this.error.code,this.error.message)
            emit(this.data)
        }
    }) {
        it?.let { it1 -> toast(it1) }
    }


    /**
     *
     * 根据tab获取数据
     */
    fun getChildDataByTab(platform:String,tab:String,tag:String) = request<BaiduCardDataBean>({
        mRealTimeApiService.getChildDataByTab(platform,tab,tag).run {
            responseCodeExceptionHandler(this.error.code,this.error.message)
            emit(this.data)
        }
    }) {
        it?.let { it1 -> toast(it1) }
    }
}