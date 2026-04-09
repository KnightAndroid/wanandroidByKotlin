package com.knight.kotlin.module_square.repo

import com.knight.kotlin.library_base.repository.BaseRepository
import com.knight.kotlin.library_network.model.responseCodeExceptionHandler
import com.knight.kotlin.module_square.api.SquareArticleApi
import com.knight.kotlin.module_square.entity.SquareArticleListBean
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/4/11 11:27
 * Description:SquareArticleRepo
 */
class SquareArticleRepo @Inject constructor(
    private val api: SquareArticleApi
) : BaseRepository() {

    /**
     * 获取文章列表
     */
    fun getArticleByTag(
        page: Int,
        keyword: String
    ): Flow<SquareArticleListBean> = request {

        val response = api.getArticlebyTag(page, keyword)

        responseCodeExceptionHandler(response.code, response.msg)

        val data = response.data

        emit(data)
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