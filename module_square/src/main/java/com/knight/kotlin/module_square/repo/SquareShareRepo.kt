package com.knight.kotlin.module_square.repo

import com.knight.kotlin.library_base.repository.BaseRepository
import com.knight.kotlin.library_network.model.responseCodeExceptionHandler
import com.knight.kotlin.module_square.api.SquareShareArticleApi
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/4/8 17:41
 * Description:SquareShareRepo
 */
class SquareShareRepo @Inject constructor() : BaseRepository() {
    @Inject
    lateinit var mSquareShareArticleApi:SquareShareArticleApi

    /**
     * 分享文章
     */
    suspend fun shareArticle(title:String,link:String) = request<Any>{
        mSquareShareArticleApi.shareArticle(title, link).run {
            responseCodeExceptionHandler(code, msg)
            emit(data)
        }
    }
}