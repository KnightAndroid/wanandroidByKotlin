package com.knight.kotlin.module_wechat.repo

import com.knight.kotlin.library_base.repository.BaseRepository
import com.knight.kotlin.library_network.model.responseCodeExceptionHandler
import com.knight.kotlin.module_wechat.api.WechatApiService
import com.knight.kotlin.module_wechat.entity.WechatArticleListEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 *
 * @ProjectName:    wanandroid
 * @Package:        com.knight.kotlin.module_wechat.repo
 * @ClassName:      WechatRepo
 * @Description:    java类作用描述
 * @Author:         knight
 * @CreateDate:     2022/3/21 10:05 上午
 * @UpdateUser:     更新者
 * @UpdateDate:     2022/3/21 10:05 上午
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */

class WechatRepo @Inject constructor(
    private val wechatApiService: WechatApiService
) : BaseRepository() {

    /**
     * 获取微信文章列表
     */
    fun getWechatArticle(
        cid: Int,
        page: Int
    ): Flow<WechatArticleListEntity> = request {
        val resp = wechatApiService.getWechatArticle(cid, page)
        responseCodeExceptionHandler(resp.code, resp.msg)
        emit(resp.data)
    }

    /**
     * 收藏文章
     */
    fun collectArticle(
        articleId: Int
    ): Flow<Unit> = request {
        val resp = wechatApiService.collectArticle(articleId)
        responseCodeExceptionHandler(resp.code, resp.msg)
        emit(Unit)
    }

    /**
     * 取消收藏
     */
    fun unCollectArticle(
        articleId: Int
    ): Flow<Unit> = request {
        val resp = wechatApiService.unCollectArticle(articleId)
        responseCodeExceptionHandler(resp.code, resp.msg)
        emit(Unit)
    }

    /**
     * 搜索公众号文章
     */
    fun getWechatArticleByKeyWords(
        cid: Int,
        page: Int,
        keyword: String
    ): Flow<WechatArticleListEntity> = request {
        val resp = wechatApiService.getWechatArticleByKeywords(cid, page, keyword)
        responseCodeExceptionHandler(resp.code, resp.msg)
        emit(resp.data)
    }
}