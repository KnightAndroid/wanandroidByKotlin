package com.knight.kotlin.library_util

import org.junit.Test
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale




/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val format = SimpleDateFormat("yyyy/MM/dd HH:m:s")
       // val date = format.parse("2013/11/11 18:35:35")

        val originalFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())
        val targetFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

        try {
            val date: Date = originalFormat.parse("2018/07/04 01:14:04")
            System.out.println(TimeAgoUtils.format(targetFormat.format(date)))
        } catch (e: ParseException) {
            e.printStackTrace()
        }



    }
}