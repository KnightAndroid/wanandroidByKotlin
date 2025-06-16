package com.knight.kotlin.library_database.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.knight.kotlin.library_database.converter.DateConverter
import com.knight.kotlin.library_database.dao.EveryDayPushArticleDao
import com.knight.kotlin.library_database.dao.HistoryReadRecordsDao
import com.knight.kotlin.library_database.dao.PushArticlesDateDao
import com.knight.kotlin.library_database.dao.SearchCityDao
import com.knight.kotlin.library_database.dao.SearchDayPushArticleDao
import com.knight.kotlin.library_database.entity.CityBean
import com.knight.kotlin.library_database.entity.EveryDayPushEntity
import com.knight.kotlin.library_database.entity.HistoryReadRecordsEntity
import com.knight.kotlin.library_database.entity.PushDateEntity
import com.knight.kotlin.library_database.entity.SearchHistroyKeywordEntity


/**
 * Author:Knight
 * Time:2021/12/27 15:19
 * Description:AppDataBase
 * DataBase
 */
//

@Database(entities = [EveryDayPushEntity::class,PushDateEntity::class, SearchHistroyKeywordEntity::class,HistoryReadRecordsEntity::class,CityBean::class],version = 2,exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class AppDataBase :RoomDatabase(){

    //每日推荐Dao
    abstract fun mEveryDayPushArticleDao(): EveryDayPushArticleDao
    //每日推荐
    abstract fun mPushDateDao():PushArticlesDateDao
    //搜索记录
    abstract fun mHistroyKeywordDao(): SearchDayPushArticleDao
    //阅读历史记录
    abstract fun mHistoryReadRecordsDao():HistoryReadRecordsDao
    //搜索city
    abstract fun mSearchCityDao():SearchCityDao

    companion object {
        @Volatile private var INSTANCE:AppDataBase?=null
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // 创建新表的 SQL 语句
                database.execSQL(
                    """
            CREATE TABLE IF NOT EXISTS searchcity_table (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                city TEXT NOT NULL,
                province TEXT NOT NULL,
                area TEXT NOT NULL DEFAULT ''
            )
            """
                )
            }
        }
        fun getDatabase(context: Context, dbName: String): AppDataBase? {
            if (INSTANCE == null) {
                synchronized(AppDataBase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDataBase::class.java, dbName
                        )
                            .addMigrations(MIGRATION_1_2)
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