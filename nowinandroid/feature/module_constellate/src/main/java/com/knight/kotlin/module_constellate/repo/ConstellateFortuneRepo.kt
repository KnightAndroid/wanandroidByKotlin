package com.knight.kotlin.module_constellate.repo

import com.core.library_base.ktx.dimissLoadingDialog
import com.knight.kotlin.library_base.repository.BaseRepository
import com.knight.kotlin.library_network.model.responseCodeExceptionHandler
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.module_constellate.api.ConstellateFortuneApi
import com.knight.kotlin.module_constellate.entity.ConstellateFortuneSubEntity
import com.knight.kotlin.module_constellate.entity.ConstellateResponseEntity
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
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
     * 并发请求五个时间段的星座运势（today / tomorrow / week / month / year）
     */
    fun getAllConstellateFortunes(
        type: String
    ) = request<ConstellateResponseEntity>({
        coroutineScope {
            // 并发发起 5 个请求
            val todayDeferred = async { mConstellateFortuneApi.getConstellateFortune(type, "today") }
            val tomorrowDeferred = async { mConstellateFortuneApi.getConstellateFortune(type, "nextday") }
            val weekDeferred = async { mConstellateFortuneApi.getConstellateFortune(type, "week") }
            val monthDeferred = async { mConstellateFortuneApi.getConstellateFortune(type, "month") }
            val yearDeferred = async { mConstellateFortuneApi.getConstellateFortune(type, "year") }

            // 等待全部完成
            val today = todayDeferred.await()
            val tomorrow = tomorrowDeferred.await()
            val week = weekDeferred.await()
            val month = monthDeferred.await()
            val year = yearDeferred.await()

            dimissLoadingDialog()

            // 检查响应状态码（如果你项目封装里统一要求）
            responseCodeExceptionHandler(today.code, today.msg)
            responseCodeExceptionHandler(tomorrow.code, tomorrow.msg)
            responseCodeExceptionHandler(week.code, week.msg)
            responseCodeExceptionHandler(month.code, month.msg)
            responseCodeExceptionHandler(year.code, year.msg)

            // 发射组合结果
            emit(
                ConstellateResponseEntity(
                    day = today.data,
                    tomorrow = tomorrow.data,
                    week = week.data,
                    month = month.data,
                    year = year.data
                )
            )
        }
    }) {
        dimissLoadingDialog()
        it?.let { err ->
            toast(err)
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