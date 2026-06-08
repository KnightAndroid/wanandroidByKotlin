package com.knight.kotlin.module_mine.repo

import com.knight.kotlin.library_base.repository.BaseRepository
import com.knight.kotlin.library_network.model.responseCodeExceptionHandler
import com.knight.kotlin.module_mine.api.MyShareArticlesApiService
import com.knight.kotlin.module_mine.entity.MyShareArticleEntity
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/5/17 10:25
 * Description:MyShareArticlesRepo
 */
class MyShareArticlesRepo @Inject constructor(
    private val api: MyShareArticlesApiService
) : BaseRepository() {

    /**
     * 获取我的分享文章
     */
    fun getMyShareArticles(
        page: Int
    ) = request<MyShareArticleEntity> {

        val res = api.getMyShareArticles(
            page
        )

        responseCodeExceptionHandler(
            res.code,
            res.msg
        )

        emit(
            res.data
        )
    }

    /**
     * 删除自己分享的文章
     */
    fun deleteMyShareArticles(
        articleId: Int
    ) = request<Any> {

        val res = api.deleteMyShareArticles(
            articleId
        )

        responseCodeExceptionHandler(
            res.code,
            res.msg
        )

        emit(
            res.data
        )
    }
}