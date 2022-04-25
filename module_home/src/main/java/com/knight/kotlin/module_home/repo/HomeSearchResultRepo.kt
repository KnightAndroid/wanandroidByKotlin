package com.knight.kotlin.module_home.repo

import com.knight.kotlin.library_base.repository.BaseRepository
import com.knight.kotlin.library_network.model.responseCodeExceptionHandler
import com.knight.kotlin.module_home.api.HomeSearchResultApiService
import com.knight.kotlin.module_home.entity.HomeArticleListBean
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/4/20 10:53
 * Description:HomeSearchResultRepo
 */
class HomeSearchResultRepo @Inject constructor(): BaseRepository(){
    @Inject
    lateinit var mHomeSearchResultApiService: HomeSearchResultApiService

    /**
     * 通过关键字搜索
     *
     */
    suspend fun searchArticleByKeyword(page:Int,keyword:String) = request<HomeArticleListBean>{
        mHomeSearchResultApiService.searchArticleByKeyword(page, keyword).run{
            responseCodeExceptionHandler(code, msg)
            emit(data)
        }

    }


    /**
     * 收藏文章
     *
     */
    suspend fun collectArticle(collectArticleId:Int) = request<Any>{
        mHomeSearchResultApiService.collectArticle(collectArticleId).run {
            responseCodeExceptionHandler(code,msg)
            emit(data)
        }
    }


    /**
     *
     * 取消收藏文章
     */
    suspend fun unCollectArticle(unCollectArticleId:Int) = request<Any> {
        mHomeSearchResultApiService.unCollectArticle(unCollectArticleId).run {
            responseCodeExceptionHandler(code,msg)
            emit(data)
        }
    }






}