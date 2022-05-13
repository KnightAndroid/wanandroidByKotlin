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
class MyCollectArticleRepo @Inject constructor() : BaseRepository() {


    @Inject
    lateinit var mMineApiService: MyCollectArticleService


    /***
     * 获取自己收藏文章列表
     *
     */
    suspend fun getMyCollectArticles(page:Int) = request<MyCollectArticleListEntity>{
        mMineApiService.getMyCollectArticles(page).run {
            responseCodeExceptionHandler(code, msg)
            emit(data)
        }
    }


    /**
     * 取消文章点赞/收藏
     *
     */
    suspend fun cancelCollectArticle(unCollectArticleId:Int) = request<Any> {
        mMineApiService.unCollectArticle(unCollectArticleId).run {
            responseCodeExceptionHandler(code, msg)
            emit(data)
        }
    }

}