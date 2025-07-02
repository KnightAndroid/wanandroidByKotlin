package com.knight.kotlin.module_web.repo

import com.knight.kotlin.library_base.repository.BaseRepository
import com.knight.kotlin.library_network.model.responseCodeExceptionHandler
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.module_web.api.WebApiService
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/2/21 18:11
 * Description:WebRepo
 */
class WebRepo @Inject constructor() : BaseRepository() {

    @Inject
    lateinit var mWebApiService:WebApiService

    fun collectArticle(collectArticleId:Int) = request<Any>({
        mWebApiService.collectArticle(collectArticleId).run {
            responseCodeExceptionHandler(code, msg)
            emit(data)
        }
    }){
        it?.run {
            toast(it)
        }
    }
}