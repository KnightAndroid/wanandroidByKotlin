package com.knight.kotlin.module_home.repo

import com.knight.kotlin.library_base.repository.BaseRepository
import com.knight.kotlin.library_network.model.responseCodeExceptionHandler
import com.knight.kotlin.module_home.api.HomeRecommendApiService
import com.knight.kotlin.module_home.entity.BannerBean
import com.knight.kotlin.module_home.entity.HomeArticleListBean
import com.knight.kotlin.library_common.entity.OfficialAccountEntity
import com.knight.kotlin.module_home.entity.TopArticleBean
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/2/18 10:19
 * Description:HomeRecommendRepo
 */
class HomeRecommendRepo @Inject constructor(): BaseRepository() {
    @Inject
    lateinit var mHomeRecommendApiService: HomeRecommendApiService


    /**
     * 获取未读消息
     */
    suspend fun getUnreadMessage() = request<Int> {
        mHomeRecommendApiService.getUnreadMessage().run {
            responseCodeExceptionHandler(code, msg)
            emit(data)
        }
    }


    /**
     * 获取置顶文章数据
     *
     */
    suspend fun getTopArticle() = request<MutableList<TopArticleBean>> {
        mHomeRecommendApiService.getTopArticle().run {
            responseCodeExceptionHandler(code, msg)
            emit(data)
        }
    }


    /**
     * 获取Banner数据
     */
    suspend fun getBanner() = request<MutableList<BannerBean>> {
        mHomeRecommendApiService.getBanner().run {
            responseCodeExceptionHandler(code,msg)
            emit(data)
        }
    }


    /**
     * 获取公众号数据
     *
     */
    suspend fun getOfficialAccount() = request<MutableList<OfficialAccountEntity>> {
        mHomeRecommendApiService.getOfficialAccount().run {
            responseCodeExceptionHandler(code,msg)
            emit(data)
        }
    }


    /**
     * 获取首页文章列表
     */
    suspend fun getHomeArticle(page:Int) = request<HomeArticleListBean> {
        mHomeRecommendApiService.getHomeArticle(page).run {
            responseCodeExceptionHandler(code,msg)
            emit(data)
        }
    }


    /**
     * 收藏文章
     *
     */
    suspend fun collectArticle(collectArticleId:Int) = request<Any>{
        mHomeRecommendApiService.collectArticle(collectArticleId).run {
            responseCodeExceptionHandler(code,msg)
            emit(data)
        }
    }


    /**
     *
     * 取消收藏文章
     */
    suspend fun unCollectArticle(unCollectArticleId:Int) = request<Any> {
        mHomeRecommendApiService.unCollectArticle(unCollectArticleId).run {
            responseCodeExceptionHandler(code,msg)
            emit(data)
        }
    }



}