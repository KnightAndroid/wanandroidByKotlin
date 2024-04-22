package com.knight.kotlin.module_square.repo

import com.knight.kotlin.library_base.repository.BaseRepository
import com.knight.kotlin.library_network.model.responseCodeExceptionHandler
import com.knight.kotlin.module_square.api.SquareShareArticleApi
import com.knight.kotlin.module_square.entity.SquareShareArticleListBean
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/4/8 17:41
 * Description:SquareShareListRepo
 */
class SquareShareListRepo @Inject constructor() : BaseRepository() {
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

    /**
     *
     * 获取广场文章列表数据
     */
    suspend fun getSquareArticles(page:Int) = request<SquareShareArticleListBean>{
        mSquareShareArticleApi.getSquareArticles(page).run{
            responseCodeExceptionHandler(code, msg)
            emit(data)
        }

    }

    /**
     * 收藏文章
     *
     */
    suspend fun collectArticle(collectArticleId:Int) = request<Any>{
        mSquareShareArticleApi.collectArticle(collectArticleId).run {
            responseCodeExceptionHandler(code,msg)
            emit(data)
        }
    }


    /**
     *
     * 取消收藏文章
     */
    suspend fun unCollectArticle(unCollectArticleId:Int) = request<Any> {
        mSquareShareArticleApi.unCollectArticle(unCollectArticleId).run {
            responseCodeExceptionHandler(code,msg)
            emit(data)
        }
    }
}