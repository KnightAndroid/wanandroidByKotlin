package com.knight.kotlin.module_square.repo

import com.knight.kotlin.library_base.repository.BaseRepository
import com.knight.kotlin.library_network.model.responseCodeExceptionHandler
import com.knight.kotlin.module_square.api.SquareApi
import com.knight.kotlin.module_square.entity.SquareQuestionListBean
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/4/27 16:57
 * Description:SquareRepo
 */
class SquareRepo @Inject constructor(
    private val api: SquareApi
) : BaseRepository() {

    /**
     * 获取问答文章列表
     */
    fun getQuestions(page: Int): Flow<SquareQuestionListBean> = flow {

        val response = api.getQuestions(page)

        responseCodeExceptionHandler(response.code, response.msg)

        emit(response.data)
    }

    /**
     * 收藏文章
     */
    fun collectArticle(
        collectArticleId: Int
    ): Flow<Boolean> = flow {

        val response = api.collectArticle(collectArticleId)

        responseCodeExceptionHandler(response.code, response.msg)

        emit(true)
    }

    /**
     * 取消收藏
     */
    fun unCollectArticle(
        unCollectArticleId: Int
    ): Flow<Boolean> = flow {

        val response = api.unCollectArticle(unCollectArticleId)

        responseCodeExceptionHandler(response.code, response.msg)

        emit(true)
    }
}