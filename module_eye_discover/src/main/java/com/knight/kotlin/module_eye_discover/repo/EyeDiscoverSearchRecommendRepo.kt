package com.knight.kotlin.module_eye_discover.repo

import com.knight.kotlin.library_base.repository.BaseRepository
import com.knight.kotlin.library_network.bean.EyeApiResponse
import com.knight.kotlin.library_network.model.responseCodeExceptionHandler
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.module_eye_discover.api.EyeDiscoverSearchRecommendApi
import com.knight.kotlin.module_eye_discover.entity.EyeDiscoverCardListEntity
import com.knight.kotlin.module_eye_discover.entity.EyeHotQueriesEntity
import javax.inject.Inject


/**
 * @author created by luguian
 * @organize
 * @Date 2024/12/11 11:24
 * @descript:推荐搜索仓库
 */
class EyeDiscoverSearchRecommendRepo @Inject constructor() : BaseRepository(){

    @Inject
    lateinit var mEyeDiscoverSearchRecommendApi : EyeDiscoverSearchRecommendApi


    /**
     *
     * 获取热门搜索
     */
    fun getHotQueries() = request< EyeApiResponse<EyeHotQueriesEntity>>({
          mEyeDiscoverSearchRecommendApi.getHotQueries().run {
              responseCodeExceptionHandler(this.code.toInt(), this.message.toString())
              emit(this)
          }
    }){
        it?.let { it1 -> toast(it1) }
    }


    /**
     *
     * 获取推荐视频
     */
    fun getRecommendCardList() = request<EyeDiscoverCardListEntity>({
        mEyeDiscoverSearchRecommendApi.getRecommendCardList().run {
            this.result?.let { emit(it) }
        }
    }){
        it?.let { it1 -> toast(it1) }
    }

}