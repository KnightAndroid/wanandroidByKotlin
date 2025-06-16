package com.knight.kotlin.library_database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.knight.kotlin.library_database.entity.PushDateEntity

/**
 * Author:Knight
 * Time:2021/12/27 17:42
 * Description:PushArticlesDateDao
 */
@Dao
interface PushArticlesDateDao {


    //插入
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPushArticlesDate(pushDateEntity:PushDateEntity):Long

    //更新
    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePushArticleDate(vararg pushDateEntitys:PushDateEntity)

    //查询所有历史记录
    @Query("select * from pushdate_table")
    suspend fun queryPushDateEntity():List<PushDateEntity>

    //根据时间去找有没有这条记录
    @Query("select * from pushdate_table WHERE time=:time")
    suspend fun findPushDate(time:String):PushDateEntity






}