package com.knight.kotlin.module_home.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.knight.kotlin.library_base.vm.BaseViewModel
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.module_home.entity.BannerBean
import com.knight.kotlin.module_home.entity.HomeArticleListBean
import com.knight.kotlin.module_home.entity.TopArticleBean
import com.knight.kotlin.module_home.repo.HomeRecommendRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/2/18 10:30
 * Description:HomeRecommendVm
 */
@HiltViewModel
class HomeRecommendVm @Inject constructor(private val mRepo: HomeRecommendRepo) : BaseViewModel() {
    //获取置顶文章数据结果
    val topArticles = MutableLiveData<MutableList<TopArticleBean>>()
    //首页文章数据结果
    val articleList = MutableLiveData<HomeArticleListBean>()
    //广告数据
    val bannerList = MutableLiveData<MutableList<BannerBean>>()
    //是否收藏成功
    val collectArticle = MutableLiveData<Boolean>()
    //是否取消收藏成功
    val unCollectArticle = MutableLiveData<Boolean>()
    //获取未读消息
    val unReadMessageNumber = MutableLiveData<Int>()


    /**
     *
     * 获取未读消息
     */
    fun getUnreadMessage() {
        viewModelScope.launch(Dispatchers.IO) {
            mRepo.getUnreadMessage()
                .catch { toast(it.message?:"") }
                .collect {
                    unReadMessageNumber.postValue(it)
                }
        }
    }
    /**
     * 获取指置顶文章
     */
    fun getTopArticle() {
        viewModelScope.launch(Dispatchers.IO) {
            mRepo.getTopArticle()
                .catch { toast(it.message ?: "") }
                .collect {
                    topArticles.postValue(it)
                }
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
                .catch { toast(it.message ?: "") }
                .collect {
                    bannerList.postValue(it)
                }
        }
    }

    /**
     *
     * 首页获取文章数据
     */
    fun getHomeArticle(page:Int) {
        viewModelScope.launch(Dispatchers.IO) {
            mRepo.getHomeArticle(page)
                .catch { toast(it.message?:"") }
                .collect {
                    articleList.postValue(it)
                }
        }
    }

    /**
     * 收藏文章
     *
     */
    fun collectArticle(collectArticleId:Int) {
        viewModelScope.launch(Dispatchers.IO) {
            mRepo.collectArticle(collectArticleId)
                .catch { toast(it.message?:"") }
                .collect {
                    collectArticle.postValue(true)
                }
        }

    }

    /**
     *
     * 取消收藏文章
     */
    fun unCollectArticle(unCollectArticleId:Int) {
        viewModelScope.launch(Dispatchers.IO) {
            mRepo.unCollectArticle(unCollectArticleId)
                .catch { toast(it.message?:"") }
                .collect {
                    unCollectArticle.postValue(true)
                }
        }

    }


}
