package com.knight.kotlin.module_eye_recommend.repo

import com.knight.kotlin.library_base.entity.EyeCardListEntity
import com.knight.kotlin.library_base.repository.BaseRepository
import com.knight.kotlin.library_network.model.responseCodeExceptionHandler
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.module_eye_recommend.api.EyeRecommendApi
import com.knight.kotlin.module_eye_recommend.entity.EyeSmallRecommendEntity
import javax.inject.Inject


/**
 * @author created by luguian
 * @organize
 * @Date 2025/5/23 14:18
 * @descript:开眼推荐仓库
 */
class EyeRecommendRepo @Inject constructor() : BaseRepository(){

    @Inject
    lateinit var mEyeRecommendApi : EyeRecommendApi



    /**
     *
     * 获取推荐数据
     */
    fun getEyeRecommendData(page_type:String, page_label:String) = request<EyeCardListEntity>({
        mEyeRecommendApi.getEyeRecommendData(page_type,page_label).run {
            responseCodeExceptionHandler(this.code.toInt(), this.message.toString())
            this.result?.let { emit(it) }
        }
    }){
        it?.let { it1 -> toast(it1) }
    }



    /**
     *
     * 搜索结果后下滑加载更多
     */
    fun getEyeRecommendMoreData(url:String,params:MutableMap<String,String>) = request<EyeSmallRecommendEntity>({
        mEyeRecommendApi.getEyeRecommendMoreData(url,params).run {
            responseCodeExceptionHandler(this.code.toInt(), this.message.toString())
            this.result?.let { emit(it) }
        }
    }){
        it?.let { it1 -> toast(it1) }
    }

}