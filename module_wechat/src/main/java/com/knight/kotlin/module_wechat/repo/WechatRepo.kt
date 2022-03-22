package com.knight.kotlin.module_wechat.repo

import com.knight.kotlin.library_base.repository.BaseRepository
import com.knight.kotlin.library_network.model.responseCodeExceptionHandler
import com.knight.kotlin.module_wechat.api.WechatApiService
import com.knight.kotlin.module_wechat.entity.WechatArticleListEntity
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

class WechatRepo @Inject constructor(): BaseRepository() {

    @Inject
    lateinit var mWechatApiService: WechatApiService

    /**
     * 获取微信文章数据
     *
     */
    suspend fun getWechatArticle(cid:Int,page:Int) = request<WechatArticleListEntity> {
        mWechatApiService.getWechatArticle(cid,page).run {
            responseCodeExceptionHandler(code, msg)
            emit(data)
        }
    }

    /**
     *
     * 收藏文章
     */
    suspend fun collectArticle(collectArticleId:Int) = request<Any>{
        mWechatApiService.collectArticle(collectArticleId).run {
            responseCodeExceptionHandler(code, msg)
            emit(data)
        }
    }

    /**
     * 取消文章点赞/收藏
     *
     */
    suspend fun cancelCollectArticle(unCollectArticleId:Int) = request<Any> {
        mWechatApiService.uncollectArticle(unCollectArticleId).run {
            responseCodeExceptionHandler(code, msg)
            emit(data)
        }
    }

    /**
     * 根据关键词搜索公众号
     *
     */
    suspend fun getWechatArticleByKeyWords(cid:Int,page:Int,keyword:String) = request<WechatArticleListEntity> {
        mWechatApiService.getWechatArticleByKeywords(cid,page,keyword).run {
            responseCodeExceptionHandler(code, msg)
            emit(data)
        }
    }

}