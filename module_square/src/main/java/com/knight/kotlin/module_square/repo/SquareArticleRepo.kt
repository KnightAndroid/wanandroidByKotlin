package com.knight.kotlin.module_square.repo

import com.knight.kotlin.library_base.repository.BaseRepository
import com.knight.kotlin.library_network.model.responseCodeExceptionHandler
import com.knight.kotlin.module_square.api.SquareArticleApi
import com.knight.kotlin.module_square.entity.SquareArticleListBean
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/4/11 11:27
 * Description:SquareArticleRepo
 */
class SquareArticleRepo @Inject constructor(): BaseRepository() {

    @Inject
    lateinit var mSquareArticleApi :SquareArticleApi

    /**
     * 获取首页文章列表
     */
    suspend fun getArticleByTag(page:Int,keyword:String) = request<SquareArticleListBean> {
        mSquareArticleApi.getArticlebyTag(page,keyword).run {
            responseCodeExceptionHandler(code,msg)
            emit(data)
        }
    }

    /**
     * 收藏文章
     *
     */
    suspend fun collectArticle(collectArticleId:Int) = request<Any>{
        mSquareArticleApi.collectArticle(collectArticleId).run {
            responseCodeExceptionHandler(code,msg)
            emit(data)
        }
    }


    /**
     *
     * 取消收藏文章
     */
    suspend fun unCollectArticle(unCollectArticleId:Int) = request<Any> {
        mSquareArticleApi.unCollectArticle(unCollectArticleId).run {
            responseCodeExceptionHandler(code,msg)
            emit(data)
        }
    }
}