package com.knight.kotlin.module_square.repo

import com.knight.kotlin.library_base.entity.SearchHotKeyEntity
import com.knight.kotlin.library_base.repository.BaseRepository
import com.knight.kotlin.library_network.model.responseCodeExceptionHandler
import com.knight.kotlin.module_square.api.SquareApi
import com.knight.kotlin.module_square.entity.SquareArticleListBean
import com.knight.kotlin.module_square.entity.SquareQuestionListBean
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/4/27 16:57
 * Description:SquareRepo
 */
class SquareRepo @Inject constructor() : BaseRepository() {
    @Inject
    lateinit var mSquareApi: SquareApi

    /**
     * 获取问答文章列表
     */
    suspend fun getQuestions(page:Int) = request<SquareQuestionListBean>{
        mSquareApi.getQuestions(page).run {
            responseCodeExceptionHandler(code, msg)
            emit(data)
        }
    }

    /**
     * 获取热词
     *
     */
    suspend fun getHotKey() = request<MutableList<SearchHotKeyEntity>>{
        mSquareApi.getHotKey().run{
            responseCodeExceptionHandler(code, msg)
            emit(data)
        }

    }

    /**
     *
     * 获取广场文章列表数据
     */
    suspend fun getSquareArticles(page:Int) = request<SquareArticleListBean>{
        mSquareApi.getSquareArticles(page).run{
            responseCodeExceptionHandler(code, msg)
            emit(data)
        }

    }

    /**
     * 收藏文章
     *
     */
    suspend fun collectArticle(collectArticleId:Int) = request<Any>{
        mSquareApi.collectArticle(collectArticleId).run {
            responseCodeExceptionHandler(code,msg)
            emit(data)
        }
    }


    /**
     *
     * 取消收藏文章
     */
    suspend fun unCollectArticle(unCollectArticleId:Int) = request<Any> {
        mSquareApi.unCollectArticle(unCollectArticleId).run {
            responseCodeExceptionHandler(code,msg)
            emit(data)
        }
    }

}