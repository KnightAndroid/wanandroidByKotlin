package com.knight.kotlin.module_realtime.repo

import com.knight.kotlin.library_base.entity.BaiduCardDataBean
import com.knight.kotlin.library_base.repository.BaseRepository
import com.knight.kotlin.library_network.model.responseCodeExceptionHandler
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.module_realtime.api.RealTimeMainApiService
import javax.inject.Inject


/**
 * @author created by luguian
 * @organize
 * @Date 2025/3/11 15:53
 * @descript:
 */
class RealTimeHomeRepo @Inject constructor(): BaseRepository() {

    @Inject
    lateinit var mRealTimeMainApiService: RealTimeMainApiService


    /**
     *
     * 获取早报新闻
     */
    fun getMainBaiduRealTime() = request<BaiduCardDataBean>({
        mRealTimeMainApiService.getMainBaiduRealTime().run {
            responseCodeExceptionHandler(this.error.code,this.error.message)
            emit(this.data)
        }
    }) {
        it?.let { it1 -> toast(it1) }
    }




}