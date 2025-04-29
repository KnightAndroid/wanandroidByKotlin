package com.knight.kotlin.library_database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.knight.kotlin.library_database.entity.CityBean


/**
 * @author created by luguian
 * @organize
 * @Date 2025/4/28 9:38
 * @descript:搜索城市Dao
 */
@Dao
interface SearchCityDao {

    //插入
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSearchCity(cityBean: CityBean): Long

    //更新
    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateSearchCity(vararg citys: CityBean): Int

    //查询所有历史记录
    @Query("select * from searchcity_table")
    suspend fun queryAllCitys(): List<CityBean>

    //删除某个
    @Query("delete from searchcity_table where id=:id")
    suspend fun deleteCityById(id: Long)

    @Query("delete from searchcity_table where province=:province AND city=:city AND area=:area")
    suspend fun deleteCityByCityArea(province:String,city:String,area:String):Int

    //删除全部
    @Query("delete from searchcity_table")
    suspend fun deleteAllCitys():Int
}