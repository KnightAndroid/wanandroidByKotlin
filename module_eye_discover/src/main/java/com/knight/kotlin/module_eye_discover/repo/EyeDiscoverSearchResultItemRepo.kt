package com.knight.kotlin.module_eye_discover.repo

import com.knight.kotlin.library_base.repository.BaseRepository
import com.knight.kotlin.library_network.bean.EyeApiResponse
import com.knight.kotlin.library_network.model.responseCodeExceptionHandler
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.module_eye_discover.api.EyeDiscoverSearchRecommendApi
import com.knight.kotlin.module_eye_discover.api.EyeDiscoverSearchResultItemApi
import com.knight.kotlin.module_eye_discover.entity.EyeDiscoverMetro
import com.knight.kotlin.module_eye_discover.entity.EyeSearchResultEntity
import javax.inject.Inject

/**
 * @Description
 * @Author knight
 * @Time 2025/1/6 20:38
 *
 */

class EyeDiscoverSearchResultItemRepo  @Inject constructor() : BaseRepository() {


    @Inject
    lateinit var myeDiscoverSearchResultItemApi : EyeDiscoverSearchResultItemApi


    /**
     *
     * 搜索结果后下滑加载更多
     */
    fun getMoreDataSearchByQuery(url:String,params:MutableMap<String,Any>) = request<EyeSearchResultEntity>({
        myeDiscoverSearchResultItemApi .getMoreDataSearchByQuery(url,params).run {
            responseCodeExceptionHandler(this.code.toInt(), this.message.toString())
            this.result?.let { emit(it) }
        }
    }){
        it?.let { it1 -> toast(it1) }
    }
}