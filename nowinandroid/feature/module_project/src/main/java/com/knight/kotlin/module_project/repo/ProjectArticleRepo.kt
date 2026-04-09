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
class ProjectArticleRepo @Inject constructor(
    private val api: ProjectArticleApi
) : BaseRepository() {

    fun getProjectArticle(page: Int, cid: Int) = request<ProjectArticleListBean> {
        val res = api.getProjectArticle(page, cid)
        responseCodeExceptionHandler(res.code, res.msg)
        emit(res.data)
    }

    fun getNewProjectArticle(page: Int) = request<ProjectArticleListBean> {
        val res = api.getNewProjectArticle(page)
        responseCodeExceptionHandler(res.code, res.msg)
        emit(res.data)
    }

    fun collectArticle(id: Int) = request<Boolean> {
        val res = api.collectArticle(id)
        responseCodeExceptionHandler(res.code, res.msg)
        emit(true)
    }

    fun unCollectArticle(id: Int) = request<Boolean> {
        val res = api.unCollectArticle(id)
        responseCodeExceptionHandler(res.code, res.msg)
        emit(true)
    }
}