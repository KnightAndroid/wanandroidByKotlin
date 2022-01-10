package com.knight.kotlin.library_database.repository

import com.knight.kotlin.library_base.repository.BaseRepository
import com.knight.kotlin.library_database.dao.PushArticlesDateDao
import com.knight.kotlin.library_database.entity.PushDateEntity

/**
 * Author:Knight
 * Time:2021/12/27 18:14
 * Description:PushArticlesDataRepository
 */
class PushArticlesDataRepository(private val mDao:PushArticlesDateDao): BaseRepository() {


    /**
     *
     * 查询本地历史记录
     */
    suspend fun findPushArticlesDate() = request<List<PushDateEntity>>{
        mDao.queryPushDateEntity().run {
            emit(this)
        }
    }


    /**
     *
     * 更新文章推送记录
     */
    suspend fun updatePushArticlesDate(vararg pushDateEntitys:PushDateEntity){
       mDao.updatePushArticleDate(*pushDateEntitys)
    }

    /**
     *
     * 插入推送文章
     */
    suspend fun insertPushArticlesDate(pushDateEntity: PushDateEntity) = request<Long>{
        mDao.insertPushArticlesDate(pushDateEntity).run {
            emit(this)
        }

    }








}