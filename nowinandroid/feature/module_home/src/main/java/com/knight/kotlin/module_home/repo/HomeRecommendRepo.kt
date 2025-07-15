package com.knight.kotlin.module_home.repo

import com.knight.kotlin.library_base.entity.BaiduCardDataBean
import com.knight.kotlin.library_common.entity.UserInfoEntity
import com.knight.kotlin.library_base.entity.WeatherDetailBean
import com.knight.kotlin.library_base.repository.BaseRepository
import com.knight.kotlin.library_base.entity.AppUpdateBean
import com.knight.kotlin.library_base.entity.OfficialAccountEntity
import com.knight.kotlin.library_network.model.responseCodeExceptionHandler
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.module_home.api.HomeRecommendApiService
import com.knight.kotlin.module_home.entity.BannerBean
import com.knight.kotlin.module_home.entity.EveryDayPushArticlesBean
import com.knight.kotlin.module_home.entity.HomeArticleListBean
import com.knight.kotlin.module_home.entity.RainDayFallBean
import com.knight.kotlin.module_home.entity.TopArticleBean
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/2/18 10:19
 * Description:HomeRecommendRepo
 */
class HomeRecommendRepo @Inject constructor(): BaseRepository() {
    @Inject
    lateinit var mHomeRecommendApiService: HomeRecommendApiService




    /**
     *
     * 检查APP版本更新接口
     */
    fun checkAppUpdateMessage() = request<AppUpdateBean>({
        mHomeRecommendApiService.checkAppUpdateMessage().run {
            responseCodeExceptionHandler(code, msg)
            emit(data)
        }
    }){
        it?.let { it1 -> toast(it1) }
    }


    /**
     * 获取推送文章信息接口
     */
    fun getEveryDayPushArticle(failureCallBack:((String?) ->Unit) ?= null) = request<EveryDayPushArticlesBean>({
        mHomeRecommendApiService.getEveryDayPushArticle().run {
            responseCodeExceptionHandler(code, msg)
            emit(data)
        }
    }) {
        it?.let { it1 -> toast(it1) }
    }
    /**
     * 获取未读消息
     */
    fun getUnreadMessage() = request<Int> ({
        mHomeRecommendApiService.getUnreadMessage().run {
            responseCodeExceptionHandler(code, msg)
            emit(data)
        }
    }){
        it?.let { it1 -> toast(it1) }
    }


    /**
     * 获取置顶文章数据
     *
     */
    fun getTopArticle() = request<MutableList<TopArticleBean>> ({
        mHomeRecommendApiService.getTopArticle().run {
            responseCodeExceptionHandler(code, msg)
            emit(data)
        }
    }){
        it?.let { it1 -> toast(it1) }
    }

    /**
     *
     * 获取百度热搜
     */
    fun getTopBaiduRealTime() = request<BaiduCardDataBean> ({
        mHomeRecommendApiService.getBaiduRealTime().run {
            responseCodeExceptionHandler(error.code,error.message)
            emit(data)
        }
    }) {
        it?.let { it1 -> toast(it1) }
    }


    /**
     * 获取Banner数据
     */
    fun getBanner() = request<MutableList<BannerBean>> ({
        mHomeRecommendApiService.getBanner().run {
            responseCodeExceptionHandler(code,msg)
            emit(data)
        }
    }){
        it?.let { it1 -> toast(it1) }
    }


    /**
     * 获取公众号数据
     *
     */
    fun getOfficialAccount() = request<MutableList<OfficialAccountEntity>> ({
        mHomeRecommendApiService.getOfficialAccount().run {
            responseCodeExceptionHandler(code,msg)
            emit(data)
        }
    }){
        it?.let { it1 -> toast(it1) }
    }


    /**
     * 获取首页文章列表
     */
    fun getHomeArticle(page:Int) = request<HomeArticleListBean> ({
        mHomeRecommendApiService.getHomeArticle(page).run {
            responseCodeExceptionHandler(code,msg)
            emit(data)
        }
    }){
        it?.let { it1 -> toast(it1) }
    }


    /**
     * 收藏文章
     *
     */
    fun collectArticle(collectArticleId:Int) = request<Any>({
        mHomeRecommendApiService.collectArticle(collectArticleId).run {
            responseCodeExceptionHandler(code,msg)
            emit(true)
        }
    }){
        it?.run {
            toast(it)
        }
    }


    /**
     *
     * 取消收藏文章
     */
    fun unCollectArticle(unCollectArticleId:Int) = request<Any> ({
        mHomeRecommendApiService.unCollectArticle(unCollectArticleId).run {
            responseCodeExceptionHandler(code,msg)
            emit(true)
        }
    }){
        it?.let { it1 -> toast(it1) }
    }

    /**
     *
     * 登录
     */
    fun login(userName:String,passWord:String) = request<UserInfoEntity>({
        mHomeRecommendApiService.login(userName,passWord).run {
            responseCodeExceptionHandler(code,msg)
            emit(data)
        }
    }){
        it?.let { it1 -> toast(it1) }
    }

    /**
     *
     * 获取一周详细天气
     */
    fun getDetailWeekWeather(source:String, refer:String,weather_type:String,
                             province:String,
                             city:String,
                             country:String) = request<WeatherDetailBean> ({
          mHomeRecommendApiService.getDetailWeekWeather(source, refer,weather_type,
              province,
              city,
              country).run{
              responseCodeExceptionHandler(0,msg)
              emit(data)
          }
    }){
        it?.let { it1 -> toast(it1) }
    }


    /**
     *
     * 获取未来两周降雨量
     */
    fun getTwoWeekDayRainFall(
        latitude: Double,
        longitude:Double,
        start_date:String,
        end_date:String,
        current_weather:Boolean,
        daily :String
    )  = request<RainDayFallBean> ({
        mHomeRecommendApiService.getTwoWeekDayRainFall(latitude, longitude, start_date, end_date, current_weather, daily).run {
            emit(this)
        }
    }){
        it?.let { it1 -> toast(it1) }
    }







}