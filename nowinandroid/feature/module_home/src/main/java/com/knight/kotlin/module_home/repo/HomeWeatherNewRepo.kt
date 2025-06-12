package com.knight.kotlin.module_home.repo

import com.knight.kotlin.library_base.repository.BaseRepository
import com.knight.kotlin.library_network.model.responseCodeExceptionHandler
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.module_home.api.HomeWeatherNewsApiService
import com.knight.kotlin.module_home.entity.RainHourFallBean
import com.knight.kotlin.module_home.entity.WeatherNewBean
import com.knight.kotlin.module_home.entity.ZaoBaoBean
import javax.inject.Inject


/**
 * @author created by luguian
 * @organize
 * @Date 2025/3/27 14:39
 * @descript:天气 新闻 仓库
 */
class HomeWeatherNewRepo @Inject constructor(): BaseRepository(){

    @Inject
    lateinit var mHomeWeatherNewsApiService: HomeWeatherNewsApiService




    /**
     * 通过关键字搜索
     *
     */
    fun getTodayImage(format:String, idx:String, n:String) = request<WeatherNewBean>({
        mHomeWeatherNewsApiService.getTodayImage(format,idx,n).run{
            emit(this)
        }

    }){
        it?.run {
            toast(it)
        }
    }


    /**
     *
     * 获取未来两小时降雨量
     */
    fun getTwoHourRainFall(latitude: Double,
                              longitude:Double,
                              hourly:String,
                              forecast_hours:Int,
                              timezone:String)= request<RainHourFallBean>({
        mHomeWeatherNewsApiService.getTwoHourRainFall(latitude,longitude,hourly,forecast_hours,timezone).run{
            emit(this)
        }

    }){
        it?.run {
            toast(it)
        }
    }



    /**
     *
     * 获取早报新闻
     */
    fun getZaoBao() = request<ZaoBaoBean>({
        mHomeWeatherNewsApiService.getZaoBao().run {
            responseCodeExceptionHandler(code,msg)
            emit(data)
        }
    }) {
        it?.let { it1 -> toast(it1) }
    }
}