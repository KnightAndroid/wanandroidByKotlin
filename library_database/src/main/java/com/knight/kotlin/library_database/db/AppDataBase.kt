package com.knight.kotlin.library_database.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.knight.kotlin.library_database.converter.DateConverter
import com.knight.kotlin.library_database.dao.EveryDayPushArticleDao
import com.knight.kotlin.library_database.dao.PushArticlesDateDao
import com.knight.kotlin.library_database.dao.SearchDayPushArticleDao
import com.knight.kotlin.library_database.entity.EveryDayPushEntity
import com.knight.kotlin.library_database.entity.PushDateEntity
import com.knight.kotlin.library_database.entity.SearchHistroyKeywordEntity


/**
 * Author:Knight
 * Time:2021/12/27 15:19
 * Description:AppDataBase
 * DataBase
 */
//

@Database(entities = [EveryDayPushEntity::class,PushDateEntity::class, SearchHistroyKeywordEntity::class],version = 1,exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class AppDataBase :RoomDatabase(){

    //每日推荐Dao
    abstract fun mEveryDayPushArticleDao(): EveryDayPushArticleDao
    //每日推荐
    abstract fun mPushDateDao():PushArticlesDateDao
    //搜索记录
    abstract fun mHistroyKeywordDao(): SearchDayPushArticleDao

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