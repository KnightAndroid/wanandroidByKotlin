package com.knight.kotlin.module_mine.repo

import com.knight.kotlin.library_base.repository.BaseRepository
import com.knight.kotlin.library_network.model.responseCodeExceptionHandler
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.module_mine.api.MyShareArticlesApiService
import com.knight.kotlin.module_mine.entity.MyShareArticleEntity
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/5/17 10:25
 * Description:MyShareArticlesRepo
 */
class MyShareArticlesRepo @Inject constructor() : BaseRepository() {
    @Inject
    lateinit var mMyShareArticlesApiService: MyShareArticlesApiService

    /**
     *
     * 获取自己分享文章
     */
    fun getMyShareArticles(page:Int) = request<MyShareArticleEntity>({
        mMyShareArticlesApiService.getMyShareArticles(page).run {
            responseCodeExceptionHandler(code,msg)
            emit(data)
        }

    }){
        it?.run {
            toast(it)
        }
    }

    /***
     *
     * 删除自己分享的文章
     *
     */
    fun deleteMyShareArticles(articleId:Int)  = request<Any>({
        mMyShareArticlesApiService.deleteMyShareArticles(articleId).run {
            responseCodeExceptionHandler(code,msg)
            emit(data)
        }

    }){
        it?.run{
            toast(it)
        }
    }
}