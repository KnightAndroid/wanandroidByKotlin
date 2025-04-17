package com.knight.kotlin.module_home.repo

import com.knight.kotlin.library_base.repository.BaseRepository
import com.knight.kotlin.library_network.model.responseCodeExceptionHandler
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.module_home.api.HomeCityGroupApiService
import com.knight.kotlin.library_widget.GroupCityListBean
import javax.inject.Inject


/**
 * @author created by luguian
 * @organize
 * @Date 2025/4/17 16:48
 * @descript:获取城市列表仓库
 */
class HomeCityGroupRepo @Inject constructor(): BaseRepository() {
    @Inject
    lateinit var mHomeCityGroupApiService: HomeCityGroupApiService

    /**
     *
     * 获取早报新闻
     */
    fun getCityGroupData(source:String) = request<MutableList<GroupCityListBean>>({
        mHomeCityGroupApiService.getCityGroupData(source).run {
            responseCodeExceptionHandler(0,msg)
            emit(data)
        }
    }) {
        it?.let { it1 -> toast(it1) }
    }

}