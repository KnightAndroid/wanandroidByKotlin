package com.knight.kotlin.module_eye_square.repo

import com.knight.kotlin.library_base.entity.EyeCardListEntity
import com.knight.kotlin.library_base.repository.BaseRepository
import com.knight.kotlin.library_network.model.responseCodeExceptionHandler
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.module_eye_square.api.EyeSquareApi
import javax.inject.Inject


/**
 * @author created by luguian
 * @organize
 * @Date 2025/2/11 15:21
 * @descript:开眼广场模块
 */
class EyeSquareRepo @Inject constructor() : BaseRepository(){

    @Inject
    lateinit var mEyeSquareApi : EyeSquareApi





    /**
     *
     * 根据关键词搜索
     */
    fun getSquareDataByTypeAndLabel(page_type:String, page_label:String) = request<EyeCardListEntity>({
        mEyeSquareApi.getSquareDataByTypeAndLabel(page_type,page_label).run {
            responseCodeExceptionHandler(this.code.toInt(), this.message.toString())
            this.result?.let { emit(it) }
        }
    }){
        it?.let { it1 -> toast(it1) }
    }
}