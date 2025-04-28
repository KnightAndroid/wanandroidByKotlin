package com.knight.kotlin.library_database.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


/**
 * @author created by luguian
 * @organize
 * @Date 2025/4/17 10:43
 * @descript:城市bean
 */
@Parcelize
@Entity(tableName = "searchcity_table")
data class CityBean (
    @PrimaryKey(autoGenerate = true)
    var id: Long,
    val city:String,
    val province:String,
    val area:String = ""
) : Parcelable