package com.knight.kotlin.module_set.repo

import android.content.Context
import com.knight.kotlin.library_base.repository.BaseRepository
import com.knight.kotlin.library_network.model.responseCodeExceptionHandler
import com.knight.kotlin.library_util.CacheFileUtils
import com.knight.kotlin.module_set.api.SetApiService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/3/30 15:24
 * Description:SetRepo
 */
class SetRepo @Inject constructor(
    private val api: SetApiService,
    @ApplicationContext private val context: Context
) : BaseRepository() {

    /**
     * 退出登录
     */
    fun logout(): Flow<Unit> = flow {
        val response = api.logout()
        responseCodeExceptionHandler(response.code, response.msg)
        emit(Unit)
    }

    /**
     * 清缓存（新增）
     */
    fun clearCache() {
        CacheFileUtils.cleadAllCache(context)
    }

    /**
     * 获取缓存大小（建议一起下沉🔥）
     */
    fun getCacheSize(): String {
        return CacheFileUtils.getToalCacheSize(context)
    }
}