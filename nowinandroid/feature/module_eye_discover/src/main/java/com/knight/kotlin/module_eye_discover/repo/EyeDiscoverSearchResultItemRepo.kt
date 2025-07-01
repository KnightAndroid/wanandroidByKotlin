package com.knight.kotlin.module_eye_discover.repo

import com.core.library_base.repository.BaseRepository
import com.knight.kotlin.library_network.model.responseCodeExceptionHandler
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.module_eye_discover.api.EyeDiscoverSearchResultItemApi
import com.knight.kotlin.module_eye_discover.entity.EyeSearchResultEntity
import com.knight.kotlin.module_eye_discover.entity.EyeSearchResultLoadEntity
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
    fun getMoreDataSearchByQuery(url:String,params:MutableMap<String,String>) = request<EyeSearchResultLoadEntity>({
        myeDiscoverSearchResultItemApi .getMoreDataSearchByQuery(url,params).run {
            responseCodeExceptionHandler(this.code.toInt(), this.message.toString())
            this.result?.let { emit(it) }
        }
    }){
        it?.let { it1 -> toast(it1) }
    }
}