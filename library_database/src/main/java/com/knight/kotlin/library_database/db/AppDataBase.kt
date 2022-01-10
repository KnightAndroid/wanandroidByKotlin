package com.knight.kotlin.library_database.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.knight.kotlin.library_database.dao.EveryDayPushArticleDao
import com.knight.kotlin.library_database.dao.PushArticlesDateDao
import com.knight.kotlin.library_database.entity.EveryDayPushEntity
import com.knight.kotlin.library_database.entity.PushDateEntity


/**
 * Author:Knight
 * Time:2021/12/27 15:19
 * Description:AppDataBase
 * DataBase
 */

//@TypeConverters(value = [DateConverter::class])
@Database(entities = [EveryDayPushEntity::class,PushDateEntity::class],version = 1,exportSchema = false)
abstract class AppDataBase :RoomDatabase(){

    //每日推荐Dao
    abstract fun mEveryDayPushArticleDao(): EveryDayPushArticleDao
    //每日推荐
    abstract fun mPushDateDao():PushArticlesDateDao
    companion object {
        @Volatile private var INSTANCE:AppDataBase?=null
        fun getDatabase(context: Context, dbName: String): AppDataBase? {
            if (INSTANCE == null) {
                synchronized(AppDataBase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDataBase::class.java, dbName
                        )
                            .allowMainThreadQueries()
                            .enableMultiInstanceInvalidation()
                            .build()
                    }
                }
            }
            return INSTANCE
        }


        fun getInstance():AppDataBase?{
            return INSTANCE
        }





    }



}