package com.knight.kotlin.module_mine.repo

import com.knight.kotlin.library_base.repository.BaseRepository
import com.knight.kotlin.library_network.model.responseCodeExceptionHandler
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.module_mine.api.OtherShareApiService
import com.knight.kotlin.module_mine.entity.OtherShareArticleListEntity
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/5/7 16:30
 * Description:OtherShareArticleRepo
 */
class OtherShareArticleRepo @Inject constructor() : BaseRepository() {
    @Inject
    lateinit var mOtherShareApiService: OtherShareApiService

    /**
     *
     * 获取他人文章列表
     */
    fun getOtherShareArticle(uid:Int,page:Int) = request<OtherShareArticleListEntity>({
        mOtherShareApiService.getOtherShareArticle(uid, page).run {
            responseCodeExceptionHandler(code,msg)
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
        mOtherShareApiService.collectArticle(collectArticleId).run {
            responseCodeExceptionHandler(code, msg)
            emit(data)
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
        mOtherShareApiService.unCollectArticle(unCollectArticleId).run {
            responseCodeExceptionHandler(code, msg)
            emit(data)
        }
    }){
        it?.run {
            toast(it)
        }
    }
}