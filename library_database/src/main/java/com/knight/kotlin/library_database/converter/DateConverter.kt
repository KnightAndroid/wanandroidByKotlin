package com.knight.kotlin.library_database.converter

import androidx.room.TypeConverter
import java.util.Date


/**
 * Author:Knight
 * Time:2021/12/27 14:59
 * Description:DateConverter
 */
class DateConverter {


    @TypeConverter
    fun revertDate(value: Long): Date {
        return Date(value)
    }

    @TypeConverter
    fun converterDate(value: Date): Long {
        return value.time

    }


}