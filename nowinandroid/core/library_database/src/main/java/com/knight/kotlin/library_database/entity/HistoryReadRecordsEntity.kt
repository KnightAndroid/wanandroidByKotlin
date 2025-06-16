package com.knight.kotlin.library_database.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.Date

/**
 * Author:Knight
 * Time:2022/5/17 14:26
 * Description:HistoryReadRecordsEntity
 */
@Parcelize
@Entity(
    tableName = "historyreadrecords_table",
    indices = [Index(value = ["webUrl"], unique = true), Index(
        value = ["articleId"],
        unique = true
    )]
)
data class HistoryReadRecordsEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long,
    var userId: Int,
    var isCollect: Boolean,
    var webUrl: String,
    var articleId: Int,
    var title: String,
    var envelopePic: String? = "",
    var insertTime: Date,
    var author: String? = "",
    var chapterName: String? = "",
    var articledesc: String? = ""
) : Parcelable