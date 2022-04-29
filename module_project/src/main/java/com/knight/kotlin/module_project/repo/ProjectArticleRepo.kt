package com.knight.kotlin.module_project.repo

import com.knight.kotlin.library_base.repository.BaseRepository
import com.knight.kotlin.library_network.model.responseCodeExceptionHandler
import com.knight.kotlin.module_project.api.ProjectArticleApi
import com.knight.kotlin.module_project.entity.ProjectArticleListBean
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/4/29 9:50
 * Description:ProjectArticleRepo
 */
class ProjectArticleRepo @Inject constructor() : BaseRepository() {
    @Inject
    lateinit var mProjectViewPagerApi: ProjectArticleApi

    /**
     * 获取项目文章列表
     */
    suspend fun getProjectArticle(page:Int,cid:Int) = request<ProjectArticleListBean>{
        mProjectViewPagerApi.getProjectArticle(page,cid).run {
            responseCodeExceptionHandler(code, msg)
            emit(data)
        }
    }

    /**
     *
     * 获取最新项目文章列表
     */
    suspend fun getNewProjectArticle(page:Int) = request<ProjectArticleListBean> {
        mProjectViewPagerApi.getNewProjectArticle(page).run {
            responseCodeExceptionHandler(code, msg)
            emit(data)
        }
    }


    /**
     * 收藏文章
     *
     */
    suspend fun collectArticle(collectArticleId:Int) = request<Any>{
        mProjectViewPagerApi.collectArticle(collectArticleId).run {
            responseCodeExceptionHandler(code,msg)
            emit(data)
        }
    }


    /**
     *
     * 取消收藏文章
     */
    suspend fun unCollectArticle(unCollectArticleId:Int) = request<Any> {
        mProjectViewPagerApi.unCollectArticle(unCollectArticleId).run {
            responseCodeExceptionHandler(code,msg)
            emit(data)
        }
    }
}