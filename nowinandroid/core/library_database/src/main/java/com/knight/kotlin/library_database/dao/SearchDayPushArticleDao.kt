package com.knight.kotlin.library_database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.knight.kotlin.library_database.entity.SearchHistroyKeywordEntity

/**
 * Author:Knight
 * Time:2022/4/19 11:03
 * Description:SearchDayPushArticleDao
 */
@Dao
interface SearchDayPushArticleDao {

    //插入
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertHistroyKeyword(searchHistroyKeywordEntity: SearchHistroyKeywordEntity):Long

    //查询全部
    @Query("select * from searchhistroy_table")
    fun queryAllHistroyWordkeys(): MutableList<SearchHistroyKeywordEntity>

    //删除某个
    @Query("delete from searchhistroy_table where id=:id")
    fun deleteKetwordsById(id: Long)

    //删除全部
    @Query("delete from searchhistroy_table")
    fun deleteAllKeywords()
}