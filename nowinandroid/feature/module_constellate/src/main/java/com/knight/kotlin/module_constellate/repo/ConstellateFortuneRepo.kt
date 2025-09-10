package com.knight.kotlin.module_constellate.repo

import com.core.library_base.ktx.dimissLoadingDialog
import com.knight.kotlin.library_base.repository.BaseRepository
import com.knight.kotlin.library_network.model.responseCodeExceptionHandler
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.module_constellate.api.ConstellateFortuneApi
import com.knight.kotlin.module_constellate.entity.ConstellateFortuneSubEntity
import com.knight.kotlin.module_constellate.entity.ConstellateResponseEntity
import javax.inject.Inject


/**
 * @author created by luguian
 * @organize
 * @Date 2025/8/6 11:04
 * @descript:星座运势Repo
 */
class ConstellateFortuneRepo @Inject constructor() : BaseRepository(){

    @Inject
    lateinit var mConstellateFortuneApi:ConstellateFortuneApi


    /**
     *
     * 根据星座类型和时间获取运势信息
      */
    fun getConstellateFortune(type:String,time:String,failureCallback:((String?) ->Unit) ?= null) = request<ConstellateResponseEntity> ({
        mConstellateFortuneApi.getConstellateFortune(type,time).run {
            dimissLoadingDialog()
            responseCodeExceptionHandler(code,msg)
            emit(data)
        }
    }){
        dimissLoadingDialog()
        it?.let {
                it1 -> toast(it1)
        }
    }


    /**
     *
     * 获取今日工作和学习
     */
    fun getConstellateFortuneWorkStudy(type:String,failureCallback: ((String?) -> Unit)?= null) = request<ConstellateFortuneSubEntity> ({
        mConstellateFortuneApi.getConstellateFortuneWorkStudy(type).run {
            emit(this)
        }
    }) { errorMsg ->
        // 出错时回调
        failureCallback?.let { it(errorMsg) }
    }



}