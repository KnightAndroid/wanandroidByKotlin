package com.knight.kotlin.module_eye_discover.repo

import com.knight.kotlin.library_base.repository.BaseRepository
import com.knight.kotlin.library_network.bean.EyeApiResponse
import com.knight.kotlin.library_network.model.responseCodeExceptionHandler
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.module_eye_discover.api.EyeDiscoverSearchRecommendApi
import com.knight.kotlin.module_eye_discover.api.EyeDiscoverSearchResultApi
import com.knight.kotlin.module_eye_discover.entity.EyeHotQueriesEntity
import com.knight.kotlin.module_eye_discover.entity.EyeSearchResultEntity
import javax.inject.Inject

/**
 * @Description 搜索结果仓库
 * @Author knight
 * @Time 2025/1/2 21:28
 *
 */

class EyeDiscoverSearchResultRepo @Inject constructor() : BaseRepository(){

    @Inject
    lateinit var mEyeDiscoverSearchResultApi : EyeDiscoverSearchResultApi



    /**
     *
     * 根据关键词复习
     */
    fun getSearchResultByQuery(query:String) = request<EyeSearchResultEntity>({
        mEyeDiscoverSearchResultApi.getSearchResultByQuery(query).run {
            responseCodeExceptionHandler(this.code.toInt(), this.message.toString())
            this.result?.let { emit(it) }
        }
    }){
        it?.let { it1 -> toast(it1) }
    }

}