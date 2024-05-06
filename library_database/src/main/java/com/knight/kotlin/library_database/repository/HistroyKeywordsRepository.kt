package com.knight.kotlin.library_database.repository

import com.knight.kotlin.library_base.repository.BaseRepository
import com.knight.kotlin.library_database.dao.SearchDayPushArticleDao
import com.knight.kotlin.library_database.db.AppDataBase
import com.knight.kotlin.library_database.entity.SearchHistroyKeywordEntity

/**
 * Author:Knight
 * Time:2022/4/19 11:06
 * Description:HistroyKeywordsRepository
 */
class HistroyKeywordsRepository(private val mDao:SearchDayPushArticleDao = AppDataBase.getInstance()?.mHistroyKeywordDao()!!): BaseRepository() {




    companion object {
        private var instance: HistroyKeywordsRepository? = null
        fun getInstance(): HistroyKeywordsRepository? {
            if (instance == null) {
                instance = HistroyKeywordsRepository()
            }
            return instance
        }
    }
    /**
     *
     * 插入点击搜索数据
     */
     suspend fun insertHistroyKeyword(searchHistroyKeywordEntity: SearchHistroyKeywordEntity) = request<Long>{
        mDao.insertHistroyKeyword(searchHistroyKeywordEntity).run {
            emit(this)
        }
       // mDao.insertHistroyKeyword(searchHistroyKeywordEntity)
    }

    /**
     *
     * 查询本地历史记录
     */
    fun queryHistroyKeywords(failureCallBack:((String?) ->Unit) ?= null) = request<MutableList<SearchHistroyKeywordEntity>>({
        mDao.queryAllHistroyWordkeys().run {
            emit(this)
        }
    }){
        failureCallBack?.run {
            this(it)
        }
    }

    /**
     *
     * 删除单个
     */
    fun deleteHistroyKeyword(id: Long) {
        mDao.deleteKetwordsById(id)
    }

    /**
     *
     * 删除单个
     */
    fun deleteAllKeywords() {
        mDao.deleteAllKeywords()
    }





}