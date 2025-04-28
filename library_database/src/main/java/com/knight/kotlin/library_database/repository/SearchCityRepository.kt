package com.knight.kotlin.library_database.repository

import com.knight.kotlin.library_base.repository.BaseRepository
import com.knight.kotlin.library_database.dao.SearchCityDao
import com.knight.kotlin.library_database.db.AppDataBase
import com.knight.kotlin.library_database.entity.CityBean


/**
 * @author created by luguian
 * @organize
 * @Date 2025/4/28 9:56
 * @descript:搜索城市仓库
 */
class SearchCityRepository : BaseRepository(){



    private val mDao: SearchCityDao = AppDataBase.getInstance()?.mSearchCityDao()!!
    companion object {
        private lateinit var instance: SearchCityRepository
        fun getInstance(): SearchCityRepository {
            if (!::instance.isInitialized) {
                instance = SearchCityRepository()
            }
            return instance
        }
    }



    /**
     *
     * 插入搜索城市记录
     */
    fun insertSearchCity(mCityBean: CityBean) = request<Long>{
        mDao.insertSearchCity(mCityBean).run {
            emit(this)
        }
    }

    /**
     *
     * 更新搜索城市记录
     */
    fun updateSearchCity(mCityBean: CityBean) = request<Int>{
        mDao.updateSearchCity(mCityBean).run {
            emit(this)
        }
    }

    /**
     *
     * 根据省市区删除
     */
    fun deleteCityByCityArea(province:String,city:String,area:String)= request<Int> ({
        mDao.deleteCityByCityArea(province,city,area).run {
            emit(this)
        }
    })

    /**
     *
     * 查询部分阅读历史
     */
    fun queryAllCitys(failureCallBack:((String?) ->Unit) ?= null) = request<List<CityBean>> ({
        mDao.queryAllCitys().run {
            emit(this)
        }
    }){
        failureCallBack?.run {
            this(it)
        }
    }



    /**
     *
     * 删除单个
     */
    fun deleteCityBeanByid(id:Long,failureCallBack:((String?) ->Unit) ?= null) = request<Unit> ({
        mDao.deleteCityById(id).run {
            emit(Unit)
        }

    }){
        failureCallBack?.run {
            this(it)
        }
    }

    /**
     *
     * 删除全部
     */
    fun deleteAllCitys(failureCallBack:((String?) ->Unit) ?= null) = request<Unit> ({
        mDao.deleteAllCitys().run {
            emit(Unit)
        }
    }){
        failureCallBack?.run {
            this(it)
        }
    }



}