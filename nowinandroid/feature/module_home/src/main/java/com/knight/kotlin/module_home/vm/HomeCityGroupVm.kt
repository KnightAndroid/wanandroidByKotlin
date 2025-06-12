package com.knight.kotlin.module_home.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.knight.kotlin.library_base.vm.BaseViewModel
import com.knight.kotlin.library_database.entity.CityBean
import com.knight.kotlin.library_database.repository.SearchCityRepository
import com.knight.kotlin.library_widget.GroupCityListBean
import com.knight.kotlin.module_home.repo.HomeCityGroupRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


/**
 * @author created by luguian
 * @organize
 * @Date 2025/4/17 16:53
 * @descript:城市列表vm
 */
@HiltViewModel
class HomeCityGroupVm @Inject constructor(private val mRepo: HomeCityGroupRepo) : BaseViewModel() {


    private val repository =  SearchCityRepository()




    /**
     * 获取城市列表
     */
    fun getCityGroupData(source:String): LiveData<MutableList<GroupCityListBean>> {
        return mRepo.getCityGroupData(source).asLiveData()
    }


    /**
     *
     * 获取本地全部历史数据
     */
    fun queryLocalSearchCitys():  LiveData<List<CityBean>>{
        return repository.queryAllCitys().asLiveData()
    }

    /**
     *
     * 插入一个搜索城市
     */
    fun insertCityData(mCityBean: CityBean): LiveData<Long> {
        return repository.insertSearchCity(mCityBean).asLiveData()
    }
    /**
     *
     * 删除全部本地搜索城市数据
     */
    fun deleteAllSearchCitys():LiveData<Int> {
        return repository.deleteAllCitys().asLiveData()
    }

    /**
     *
     * 根据省市区删除
     */
    fun deleteCityByCityArea(province:String,city:String,area:String):LiveData<Int> {
        return repository.deleteCityByCityArea(province, city, area).asLiveData()
    }


    /**
     *
     * 根据搜索城市
      */
    fun getSearchCityByKey(source: String,
                          city:String): LiveData<Map<String, String>> {
        return mRepo.getSearchCityByKey(source, city).asLiveData()
    }
}