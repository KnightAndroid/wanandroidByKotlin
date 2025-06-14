package com.knight.kotlin.module_navigate.repo

import com.knight.kotlin.library_base.repository.BaseRepository
import com.knight.kotlin.library_network.model.responseCodeExceptionHandler
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.module_navigate.api.HierachyArticleApi
import com.knight.kotlin.module_navigate.entity.HierachyTabArticleListEntity
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/5/6 16:05
 * Description:HierachyArticleRepository
 */
class HierachyArticleRepository @Inject constructor(): BaseRepository() {
    @Inject
    lateinit var mHierachyArticleApi: HierachyArticleApi


    /**
     *
     * 获取体系文章页码
     */
    fun getHierachyArticle(page:Int,cid:Int) = request<HierachyTabArticleListEntity> ({
        mHierachyArticleApi.getHierachyArticle(page,cid).run {
            responseCodeExceptionHandler(code, msg)
            emit(data)
        }
    }){
        it?.run {
            toast(it)
        }
    }

    /**
     *
     * 收藏文章
     */
    fun collectArticle(collectArticleId:Int) = request<Any>({
        mHierachyArticleApi.collectArticle(collectArticleId).run {
            responseCodeExceptionHandler(code, msg)
            emit(true)
        }
    }){
        it?.run {
            toast(it)
        }
    }

    /**
     * 取消文章点赞/收藏
     *
     */
    fun cancelCollectArticle(unCollectArticleId:Int) = request<Any> ({
        mHierachyArticleApi.unCollectArticle(unCollectArticleId).run {
            responseCodeExceptionHandler(code, msg)
            emit(data)
        }
    }){
        it?.run {
            toast(it)
        }
    }
}