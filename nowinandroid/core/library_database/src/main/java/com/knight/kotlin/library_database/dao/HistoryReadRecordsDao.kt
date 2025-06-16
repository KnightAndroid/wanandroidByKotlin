package com.knight.kotlin.library_database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.knight.kotlin.library_database.entity.HistoryReadRecordsEntity

/**
 * Author:Knight
 * Time:2022/5/17 15:15
 * Description:HistoryReadRecordsDao
 */
@Dao
interface HistoryReadRecordsDao {

    //插入
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertHistoryReadRecords(historyReadRecordsEntity: HistoryReadRecordsEntity): Long

    //更新
    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateHistoryReadRecord(vararg historyReadRecordsEntities: HistoryReadRecordsEntity): Int

    //查询所有历史记录
    @Query("select * from historyreadrecords_table")
    suspend fun queryAllHistoryRecords(): List<HistoryReadRecordsEntity>

    //删除某个
    @Query("delete from historyreadrecords_table where id=:id")
    suspend fun deleteHistoryRecordsById(id: Long)

    //删除全部
    @Query("delete from historyreadrecords_table")
    suspend fun deleteAllHistoryRecords()

    //根据用户Id查询部分
    @Query("select id,webUrl,articleId,title,envelopePic,insertTime,author,chapterName,articledesc,userId,isCollect FROM historyreadrecords_table Where userId =:userId order by insertTime desc limit :start,:end ")
    suspend fun queryPartHistoryRecords(start: Int, end: Int, userId: Int): MutableList<HistoryReadRecordsEntity>

    //根据用户指定查询
    @Query("select * from historyreadrecords_table WHERE userId=:userId AND webUrl=:webUrl AND articleId=:articleId")
    suspend fun findHistoryReadRecords(webUrl: String, articleId: Int, userId: Int): HistoryReadRecordsEntity?
}