package com.knight.kotlin.module_home.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.knight.kotlin.library_base.entity.BaiduCardDataBean
import com.knight.kotlin.library_base.entity.UserInfoEntity
import com.knight.kotlin.library_base.entity.WeatherDetailBean
import com.knight.kotlin.library_base.vm.BaseViewModel
import com.knight.kotlin.library_common.entity.OfficialAccountEntity
import com.knight.kotlin.library_database.db.AppDataBase
import com.knight.kotlin.library_database.entity.PushDateEntity
import com.knight.kotlin.library_database.repository.PushArticlesDataRepository
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.module_home.entity.BannerBean
import com.knight.kotlin.module_home.entity.EveryDayPushArticlesBean
import com.knight.kotlin.module_home.entity.HomeArticleListBean
import com.knight.kotlin.module_home.entity.TopArticleBean
import com.knight.kotlin.module_home.entity.ZaoBaoBean
import com.knight.kotlin.module_home.repo.HomeRecommendRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/2/18 10:30
 * Description:HomeRecommendVm
 */
@HiltViewModel
class HomeRecommendVm @Inject constructor(private val mRepo: HomeRecommendRepo) : BaseViewModel() {

    private val repository: PushArticlesDataRepository


    init {
        val mDao =  AppDataBase.getInstance()?.mPushDateDao()!!
        repository = PushArticlesDataRepository(mDao)
    }

    /**
     * 获取每天推送文章
     */
    fun getEveryDayPushArticle() : LiveData<EveryDayPushArticlesBean> {
        return mRepo.getEveryDayPushArticle().asLiveData()
    }

    /**
     *
     * 查询本地历史记录
     */
    fun queryPushDate():LiveData<List<PushDateEntity>> {
        return repository.findPushArticlesDate(failureCallBack = {
            it?.let { it1 -> toast(it1) }
        }).asLiveData()


    }

    /**
     * 获取早报信息
     *
     */
    fun getZaoBao():LiveData<ZaoBaoBean> {
        return mRepo.getZaoBao().asLiveData()  //#1DD4EC #4B97F8
    }


    /**
     * 更新本地记录
     */
    fun updatePushArticlesDate(pushDateEntity: PushDateEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updatePushArticlesDate(pushDateEntity)
        }

    }

    /**
     *
     * 插入
     */
    fun insertPushArticlesDate(pushDateEntity: PushDateEntity){
        repository.insertPushArticlesDate(pushDateEntity, failureCallBack = {
            it?.let { it1 -> toast(it1) }
        })
    }

    /**
     *
     * 获取未读消息
     */
    fun getUnreadMessage():LiveData<Int> {
        return mRepo.getUnreadMessage().asLiveData()
    }


    /**
     * 获取指置顶文章
     */
    fun getTopArticle() :LiveData<MutableList<TopArticleBean>>{
        return mRepo.getTopArticle().asLiveData()
    }

    /**
     * 获取百度热搜
     *
     */
    fun getTopBaiduRealTime() : LiveData<BaiduCardDataBean> {
        return mRepo.getTopBaiduRealTime().asLiveData()
    }


    /**
     *
     * 获取广告数据
     *
     */
    fun getBanner():LiveData<MutableList<BannerBean>> = mRepo.getBanner().asLiveData()


    /**
     * 获取公众号数据
     *
     */
    fun getOfficialAccount() : LiveData<MutableList<OfficialAccountEntity>> {
        return mRepo.getOfficialAccount().asLiveData()

    }



    /**
     *
     * 首页获取文章数据
     */
    fun getHomeArticle(page:Int): LiveData<HomeArticleListBean> {
        return mRepo.getHomeArticle(page).asLiveData()
    }

    /**
     * 收藏文章
     *
     */
    fun collectArticle(collectArticleId:Int):LiveData<Any> {
        return mRepo.collectArticle(collectArticleId).asLiveData()
    }

    /**
     *
     * 取消收藏文章
     */
    fun unCollectArticle(unCollectArticleId:Int):LiveData<Any> {
        return mRepo.unCollectArticle(unCollectArticleId).asLiveData()
    }



    /**
     * 登录
     */
    fun login(userName:String,passWord:String):LiveData<UserInfoEntity> {
        return mRepo.login(userName, passWord).asLiveData()
    }


    /**
     *
     * 获取一周天气
     */
    fun getDetailWeekWeather(province:String,
                             city:String,
                             country:String):LiveData<WeatherDetailBean> {
        return mRepo.getDetailWeekWeather("pc","observe|forecast_1h|forecast_24h|index|alarm|limit|tips|rise",province, city, country).asLiveData()
    }


}
