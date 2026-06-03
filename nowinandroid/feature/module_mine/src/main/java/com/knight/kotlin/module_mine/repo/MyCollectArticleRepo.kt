package com.knight.kotlin.module_mine.repo

import com.knight.kotlin.library_base.repository.BaseRepository
import com.knight.kotlin.library_network.model.responseCodeExceptionHandler
import com.knight.kotlin.module_mine.api.MyCollectArticleService
import com.knight.kotlin.module_mine.entity.MyCollectArticleListEntity
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/5/13 10:35
 * Description:MyCollectArticleRepo
 */
/**
 * 我的收藏文章
 */
class MyCollectArticleRepo @Inject constructor(
    private val api: MyCollectArticleService
) : BaseRepository() {

    /**
     * 获取收藏文章列表
     */
    fun getMyCollectArticles(
        page: Int
    ) = request<MyCollectArticleListEntity> {

        val res = api.getMyCollectArticles(page)

        responseCodeExceptionHandler(
            res.code,
            res.msg
        )

        emit(res.data)
    }

    /**
     * 取消收藏
     */
    fun cancelCollectArticle(
        articleId: Int
    ) = request<Boolean> {

        val res = api.unCollectArticle(articleId)

        responseCodeExceptionHandler(
            res.code,
            res.msg
        )

        emit(true)
    }
}