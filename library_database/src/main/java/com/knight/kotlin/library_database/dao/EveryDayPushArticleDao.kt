package com.knight.kotlin.library_database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.knight.kotlin.library_database.entity.EveryDayPushEntity




/**
 * Author:Knight
 * Time:2021/12/27 15:37
 * Description:EveryDayPushArticleDao
 */

@Dao
interface EveryDayPushArticleDao {

     //插入
     @Insert(onConflict = OnConflictStrategy.IGNORE)
     suspend fun insertEveryDayPushArticle(everyDayPushEntity: EveryDayPushEntity):Long

     //更新
     @Update(onConflict = OnConflictStrategy.REPLACE)
     suspend fun updateEveryDayPushArticle(vararg everyDayPushEntities:EveryDayPushEntity):Int

     //查询所有历史记录
     @Query("select * from everydaypush_table")
     suspend fun queryEveryDatPush():List<EveryDayPushEntity>

     //根据文章查找有没有这条记录
     @Query("select * from everydaypush_table WHERE articleLink=:articleLink")
     suspend fun findEveryDayPushArticle(articleLink:String):EveryDayPushEntity

     @Query("delete from everydaypush_table")
     suspend fun deleteAlleverydayArticles()

     //批量插入
     @Insert(onConflict = OnConflictStrategy.REPLACE)
     suspend fun insertEveryDayArticles(everyDayPushEntities : List<EveryDayPushEntity>)



}