package com.knight.kotlin.module_home.repo

import com.knight.kotlin.library_base.repository.BaseRepository
import com.knight.kotlin.library_network.model.responseCodeExceptionHandler
import com.knight.kotlin.module_home.api.HomeArticleApiService
import com.knight.kotlin.module_home.entity.HomeArticleListBean
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/4/11 11:27
 * Description:HomeArticleRepo
 */
class HomeArticleRepo @Inject constructor(): BaseRepository() {

    @Inject
    lateinit var mHomeArticleApiService: HomeArticleApiService

    /**
     * 获取首页文章列表
     */
    suspend fun getArticleByTag(page:Int,keyword:String) = request<HomeArticleListBean> {
        mHomeArticleApiService.getArticlebyTag(page,keyword).run {
            responseCodeExceptionHandler(code,msg)
            emit(data)
        }
    }

    /**
     * 收藏文章
     *
     */
    suspend fun collectArticle(collectArticleId:Int) = request<Any>{
        mHomeArticleApiService.collectArticle(collectArticleId).run {
            responseCodeExceptionHandler(code,msg)
            emit(data)
        }
    }


    /**
     *
     * 取消收藏文章
     */
    suspend fun unCollectArticle(unCollectArticleId:Int) = request<Any> {
        mHomeArticleApiService.unCollectArticle(unCollectArticleId).run {
            responseCodeExceptionHandler(code,msg)
            emit(data)
        }
    }
}