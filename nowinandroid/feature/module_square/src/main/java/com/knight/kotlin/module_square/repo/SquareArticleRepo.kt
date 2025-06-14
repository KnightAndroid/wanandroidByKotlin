package com.knight.kotlin.module_square.repo

import com.knight.kotlin.library_base.repository.BaseRepository
import com.knight.kotlin.library_network.model.responseCodeExceptionHandler
import com.knight.kotlin.library_util.toast
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
    fun getArticleByTag(page:Int,keyword:String,failureCallBack:((String?) ->Unit) ?= null) = request<SquareArticleListBean> ({
        mSquareArticleApi.getArticlebyTag(page,keyword).run {
            responseCodeExceptionHandler(code,msg)
            emit(data)
        }
    }){
       it?.run {
           toast(it)
       }
        failureCallBack?.run {
            this(it)
        }
    }

    /**
     * 收藏文章
     *
     */
    fun collectArticle(collectArticleId:Int) = request<Any>({
        mSquareArticleApi.collectArticle(collectArticleId).run {
            responseCodeExceptionHandler(code,msg)
            emit(true)
        }
    }){
        it?.run {
            toast(it)
        }
    }


    /**
     *
     * 取消收藏文章
     */
    fun unCollectArticle(unCollectArticleId:Int) = request<Any> ({
        mSquareArticleApi.unCollectArticle(unCollectArticleId).run {
            responseCodeExceptionHandler(code,msg)
            emit(true)
        }
    }){
        it?.run {
            toast(it)
        }
    }
}