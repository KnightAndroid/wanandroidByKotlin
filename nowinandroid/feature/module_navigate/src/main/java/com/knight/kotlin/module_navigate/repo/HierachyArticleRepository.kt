package com.knight.kotlin.module_navigate.repo

import com.knight.kotlin.library_base.repository.BaseRepository
import com.knight.kotlin.library_network.model.responseCodeExceptionHandler
import com.knight.kotlin.module_navigate.api.HierachyArticleApi
import com.knight.kotlin.module_navigate.entity.HierachyTabArticleListEntity
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/5/6 16:05
 * Description:HierachyArticleRepository
 */
class HierachyArticleRepository @Inject constructor(
    private val api: HierachyArticleApi
) : BaseRepository() {

    /**
     * 获取体系文章
     */
    fun getHierachyArticle(page: Int, cid: Int) =
        request<HierachyTabArticleListEntity> {
            val res = api.getHierachyArticle(page, cid)
            responseCodeExceptionHandler(res.code, res.msg)
            emit(res.data)
        }

    /**
     * 收藏文章
     */
    fun collectArticle(id: Int) =
        request<Unit> {
            val res = api.collectArticle(id)
            responseCodeExceptionHandler(res.code, res.msg)
            emit(Unit)
        }

    /**
     * 取消收藏
     */
    fun cancelCollectArticle(id: Int) =
        request<Unit> {
            val res = api.unCollectArticle(id)
            responseCodeExceptionHandler(res.code, res.msg)
            emit(Unit)
        }
}