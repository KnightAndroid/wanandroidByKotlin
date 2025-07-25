package com.knight.kotlin.module_home.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.core.library_base.vm.BaseViewModel
import com.knight.kotlin.module_home.entity.RainHourFallBean
import com.knight.kotlin.module_home.entity.WeatherNewBean
import com.knight.kotlin.module_home.entity.ZaoBaoBean
import com.knight.kotlin.module_home.repo.HomeWeatherNewRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


/**
 * @author created by luguian
 * @organize
 * @Date 2025/3/27 14:54
 * @descript:
 */
@HiltViewModel
class HomeWeatherNewVm @Inject constructor(private val mRepo: HomeWeatherNewRepo) : BaseViewModel() {


    /**
     *
     * 查询未来两个小时降雨量
      */
    fun getTwoHourRainFall(latitude: Double,
                                      longitude:Double,
                                      hourly:String,
                                      forecast_hours:Int,
                                      timezone:String) : LiveData<RainHourFallBean>{
        return mRepo.getTwoHourRainFall(latitude,longitude,hourly,forecast_hours,timezone).asLiveData()

    }

    /**
     *
     * 查询每日一图
     */
    fun getTodayImage(format:String, idx:String, n:String): LiveData<WeatherNewBean> {
        return mRepo.getTodayImage(format,idx,n).asLiveData()
    }


    /**
     * 获取早报信息
     *
     */
    fun getZaoBao():LiveData<ZaoBaoBean> {
        return mRepo.getZaoBao().asLiveData()  //#1DD4EC #4B97F8
    }







}