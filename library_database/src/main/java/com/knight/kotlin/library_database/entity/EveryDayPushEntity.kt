package com.knight.kotlin.library_database.entity

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

/**
 * Author:Knight
 * Time:2021/12/27 15:42
 * Description:EveryDayPushEntity
 */

@Parcelize
@Entity(tableName = "everydaypush_table")
data class EveryDayPushEntity (

    @NonNull
    @PrimaryKey(autoGenerate = true)
    val id:Long,

    val articlePicture:String,

    @NonNull
    val articleLink:String,

    @NonNull
    val time:String,
    val author:String,
    val pushStatus:Boolean,
    val articledesc:String,
    val articleTitle:String,
    val popupTitle:String
): Parcelable