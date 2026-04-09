package com.knight.kotlin.module_web.repo

import com.knight.kotlin.library_base.repository.BaseRepository
import com.knight.kotlin.library_network.model.responseCodeExceptionHandler
import com.knight.kotlin.module_web.api.WebApiService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/2/21 18:11
 * Description:WebRepo
 */
class WebRepo @Inject constructor(
    private val api: WebApiService
) : BaseRepository() {

    /**
     * 收藏文章
     *
     * 只负责：
     * 1. 调接口
     * 2. 校验 code
     * 3. emit data
     */
    fun collectArticle(collectArticleId: Int): Flow<Unit> = request {
        val response = api.collectArticle(collectArticleId)
        responseCodeExceptionHandler(response.code, response.msg)
        emit(Unit)
    }
}