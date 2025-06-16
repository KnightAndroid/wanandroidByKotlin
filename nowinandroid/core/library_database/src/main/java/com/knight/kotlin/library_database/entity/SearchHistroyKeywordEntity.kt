package com.knight.kotlin.library_database.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.Date

/**
 * Author:Knight
 * Time:2022/4/19 10:51
 * Description:SearchHistroyKeywordEntity
 */
@Parcelize
@Entity(tableName = "searchhistroy_table",indices = [Index(value = ["name"], unique = true)])
data class SearchHistroyKeywordEntity (
    @PrimaryKey(autoGenerate = true)
    var id: Long,
    var name: String?=null,
    var insertTime: Date
) : Parcelable