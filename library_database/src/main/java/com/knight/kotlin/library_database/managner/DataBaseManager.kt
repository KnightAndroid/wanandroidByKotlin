package com.knight.kotlin.library_database.managner

import android.content.Context
import com.knight.kotlin.library_database.db.AppDataBase

/**
 * Author:Knight
 * Time:2021/12/27 17:11
 * Description:DataBaseManager
 *
 */
class DataBaseManager {


    companion object {
        /**
         * 初始化数据库
         */
        fun getDataBase(context: Context,dbName:String) {
            AppDataBase.getDatabase(context,dbName)

        }
    }
}