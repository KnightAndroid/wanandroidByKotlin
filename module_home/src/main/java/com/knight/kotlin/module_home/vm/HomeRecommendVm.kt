package com.knight.kotlin.module_home.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.knight.kotlin.library_base.entity.UserInfoEntity
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
import com.knight.kotlin.module_home.repo.HomeRecommendRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/2/18 10:30
 * Description:HomeRecommendVm
 */
@HiltViewModel
class HomeRecommendVm @Inject constructor(private val mRepo: HomeRecommendRepo) : BaseViewModel() {

    val everyDayPushArticles = MutableLiveData<EveryDayPushArticlesBean>()

    private val repository: PushArticlesDataRepository


    init {
        val mDao =  AppDataBase.getInstance()?.mPushDateDao()!!
        repository = PushArticlesDataRepository(mDao)
    }
    //获取每日推荐文章结果
    val articles = MutableLiveData<List<PushDateEntity>>()
    //获取置顶文章数据结果
    val topArticles = MutableLiveData<MutableList<TopArticleBean>>()
    //首页文章数据结果
    val articleList = MutableLiveData<HomeArticleListBean>()
    //广告数据
    val bannerList = MutableLiveData<MutableList<BannerBean>>()
    //公众号
    val officialAccountList = MutableLiveData<MutableList<OfficialAccountEntity>>()

    //是否收藏成功
    val collectArticle = MutableLiveData<Boolean>()
    //是否取消收藏成功
    val unCollectArticle = MutableLiveData<Boolean>()
    //获取未读消息
    val unReadMessageNumber = MutableLiveData<Int>()
    //登录信息
    val userInfo = MutableLiveData<UserInfoEntity>()


    /**
     * 获取每天推送文章
     */
    fun getEveryDayPushArticle() {
        viewModelScope.launch {
            mRepo.getEveryDayPushArticle()
                //指定线程
                .flowOn(Dispatchers.IO)
                .onStart {
                    //开始
                }
                .onEach {
                    everyDayPushArticles.postValue(it)
                }
                .onCompletion {
                    //结束
                }
                .catch { toast(it.message ?: "") }
                .collect()

        }
    }

    /**
     *
     * 查询本地历史记录
     */
    fun queryPushDate(){
        viewModelScope.launch {
            repository.findPushArticlesDate()
                //指定线程
                .flowOn(Dispatchers.IO)
                .onStart {
                    //开始
                }
                .onEach {
                    articles.postValue(it)
                }
                .onCompletion {
                    //结束
                }
                .catch { toast(it.message ?: "") }
                .collect()

        }

    }


    /**
     * 更新
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
        viewModelScope.launch {
            repository.insertPushArticlesDate(pushDateEntity)
                //指定线程
                .flowOn(Dispatchers.IO)
                .onStart {
                    //开始
                }
                .onEach {
                }
                .onCompletion {
                    //结束
                }
                .catch { toast(it.message ?: "") }
                .collect()

        }
    }

    /**
     *
     * 获取未读消息
     */
    fun getUnreadMessage() {
        viewModelScope.launch {
            mRepo.getUnreadMessage()
                //指定线程
                .flowOn(Dispatchers.IO)
                .onStart {
                    //开始
                }
                .onEach {
                    unReadMessageNumber.postValue(it)
                }
                .onCompletion {
                    //结束
                    dimissLoading()
                }
                .catch { toast(it.message ?: "") }
                .collect()

        }

    }
    /**
     * 获取指置顶文章
     */
    fun getTopArticle() {
        viewModelScope.launch(Dispatchers.IO) {
            mRepo.getTopArticle()
                .onStart {
                    //开始
                }
                .onEach {
                    topArticles.postValue(it)
                }
                .onCompletion {
                    //结束
                }
                .catch { toast(it.message ?: "") }
                .collect()
        }
    }


    /**
     *
     * 获取广告数据
     *
     */
    fun getBanner() {
        viewModelScope.launch(Dispatchers.IO) {
            mRepo.getBanner()
                .onStart {
                    //开始
                }
                .onEach {
                    bannerList.postValue(it)
                }
                .onCompletion {
                    //结束
                }
                .catch { toast(it.message ?: "") }
                .collect()
        }



    }

    /**
     * 获取公众号数据
     *
     */
    fun getOfficialAccount() {
        viewModelScope.launch(Dispatchers.IO) {
            mRepo.getOfficialAccount()
                .onStart {
                    //开始
                }
                .onEach {
                    officialAccountList.postValue(it)
                }
                .onCompletion {
                    //结束
                }
                .catch { toast(it.message ?: "") }
                .collect()
        }


    }



    /**
     *
     * 首页获取文章数据
     */
    fun getHomeArticle(page:Int) {
        viewModelScope.launch(Dispatchers.IO) {
            mRepo.getHomeArticle(page)
                .onStart {
                    //开始
                }
                .onEach {
                    articleList.postValue(it)
                }
                .onCompletion {
                    //结束
                }
                .catch { toast(it.message ?: "") }
                .collect()
        }


    }

    /**
     * 收藏文章
     *
     */
    fun collectArticle(collectArticleId:Int) {
        viewModelScope.launch(Dispatchers.IO) {
            mRepo.collectArticle(collectArticleId)
                .onStart {
                    //开始
                }
                .onEach {
                    collectArticle.postValue(true)
                }
                .onCompletion {
                    //结束
                }
                .catch { toast(it.message ?: "") }
                .collect()
        }
    }

    /**
     *
     * 取消收藏文章
     */
    fun unCollectArticle(unCollectArticleId:Int) {
        viewModelScope.launch(Dispatchers.IO) {
            mRepo.unCollectArticle(unCollectArticleId)
                .onStart {
                    //开始
                }
                .onEach {
                    unCollectArticle.postValue(true)
                }
                .onCompletion {
                    //结束
                }
                .catch { toast(it.message ?: "") }
                .collect()
        }


    }



    /**
     * 登录
     */
    fun login(userName:String,passWord:String) {
        viewModelScope.launch {
            mRepo.login(userName, passWord)
                //指定线程
                .flowOn(Dispatchers.IO)
                .onStart {
                    //开始
                }
                .onEach {
                    userInfo.postValue(it)
                }
                .onCompletion {
                    //结束
                }
                .catch { toast(it.message ?: "") }
                .collect()

        }

    }


}
