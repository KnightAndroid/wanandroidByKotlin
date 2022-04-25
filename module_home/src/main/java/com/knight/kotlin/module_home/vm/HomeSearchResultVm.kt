package com.knight.kotlin.module_home.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.knight.kotlin.library_base.vm.BaseViewModel
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.module_home.entity.HomeArticleListBean
import com.knight.kotlin.module_home.repo.HomeSearchResultRepo
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
 * Time:2022/4/20 11:21
 * Description:HomeSearchResultVm
 */
@HiltViewModel
class HomeSearchResultVm @Inject constructor(private val mRepo: HomeSearchResultRepo) : BaseViewModel(){

    //首页文章数据结果
    val articleResultList = MutableLiveData<HomeArticleListBean>()

    //是否收藏成功
    val collectArticle = MutableLiveData<Boolean>()
    //是否取消收藏成功
    val unCollectArticle = MutableLiveData<Boolean>()


    /**
     *
     * 通过关键字搜索列表文章
     */
    fun searchArticleByKeyword(page:Int,keywords:String) {
        viewModelScope.launch {
            mRepo.searchArticleByKeyword(page,keywords)
                //指定线程
                .flowOn(Dispatchers.IO)
                .onStart {
                    //开始

                }
                .onEach {
                    articleResultList.postValue(it)
                }
                .onCompletion {
                    //开始
                }
                .catch {
                    toast(it.message ?: "")
                }
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


}