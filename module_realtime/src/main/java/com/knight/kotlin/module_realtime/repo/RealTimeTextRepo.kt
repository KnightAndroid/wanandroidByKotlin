package com.knight.kotlin.module_realtime.repo

import com.knight.kotlin.library_base.entity.BaiduCardDataBean
import com.knight.kotlin.library_base.repository.BaseRepository
import com.knight.kotlin.library_network.model.responseCodeExceptionHandler
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.module_realtime.api.RealTimeTextApiService
import javax.inject.Inject


/**
 * @author created by luguian
 * @organize
 * @Date 2025/3/19 16:29
 * @descript:
 */
class RealTimeTextRepo @Inject constructor(): BaseRepository() {

    @Inject
    lateinit var mRealTimeTextApiService: RealTimeTextApiService


    /**
     *
     * 获取早报新闻
     */
    fun getDataByTab(platform:String,tab:String) = request<BaiduCardDataBean>({
        mRealTimeTextApiService.getDataByTab(platform,tab).run {
            responseCodeExceptionHandler(this.error.code,this.error.message)
            emit(this.data)
        }
    }) {
        it?.let { it1 -> toast(it1) }
    }

}