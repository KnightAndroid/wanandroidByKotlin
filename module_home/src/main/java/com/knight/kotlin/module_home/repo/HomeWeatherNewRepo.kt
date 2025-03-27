package com.knight.kotlin.module_home.repo

import com.knight.kotlin.library_base.repository.BaseRepository
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.module_home.api.HomeWeatherNewsApiService
import com.knight.kotlin.module_home.entity.WeatherNewBean
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
}