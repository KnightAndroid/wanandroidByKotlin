package com.knight.kotlin.library_database.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

/**
 * Author:Knight
 * Time:2021/12/27 17:35
 * Description:PushDateEntity
 */
@Parcelize
@Entity(tableName = "pushdate_table")
class PushDateEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long?=null,
    var time: String?=null
) : Parcelable
