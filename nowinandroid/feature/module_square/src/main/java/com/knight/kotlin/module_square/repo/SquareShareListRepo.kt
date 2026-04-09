package com.knight.kotlin.module_square.repo

import com.knight.kotlin.library_base.repository.BaseRepository
import com.knight.kotlin.library_network.model.responseCodeExceptionHandler
import com.knight.kotlin.module_square.api.SquareShareArticleApi
import com.knight.kotlin.module_square.entity.SquareShareArticleListBean
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/4/8 17:41
 * Description:SquareShareListRepo
 */
class SquareShareListRepo @Inject constructor(
    private val api: SquareShareArticleApi
) : BaseRepository() {

    /**
     * 分享文章
     */
    fun shareArticle(
        title: String,
        link: String
    ): Flow<Boolean> = request {

        val response = api.shareArticle(title, link)

        responseCodeExceptionHandler(response.code, response.msg)

        emit(true)
    }

    /**
     * 获取广场文章列表
     */
    fun getSquareArticles(
        page: Int
    ): Flow<SquareShareArticleListBean> = request {

        val response = api.getSquareArticles(page)

        responseCodeExceptionHandler(response.code, response.msg)

        emit(response.data)
    }

    /**
     * 收藏文章
     */
    fun collectArticle(
        collectArticleId: Int
    ): Flow<Boolean> = request {

        val response = api.collectArticle(collectArticleId)

        responseCodeExceptionHandler(response.code, response.msg)

        emit(true)
    }

    /**
     * 取消收藏
     */
    fun unCollectArticle(
        unCollectArticleId: Int
    ): Flow<Boolean> = request {

        val response = api.unCollectArticle(unCollectArticleId)

        responseCodeExceptionHandler(response.code, response.msg)

        emit(true)
    }
}