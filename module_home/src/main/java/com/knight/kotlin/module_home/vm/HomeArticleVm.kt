package com.knight.kotlin.module_home.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.knight.kotlin.library_base.vm.BaseViewModel
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.module_home.entity.HomeArticleListBean
import com.knight.kotlin.module_home.repo.HomeArticleRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/4/11 11:21
 * Description:HomeArticleVm
 */
@HiltViewModel
class HomeArticleVm @Inject constructor(private val mRepo: HomeArticleRepo) : BaseViewModel(){
    //首页文章数据结果
    val articleList = MutableLiveData<HomeArticleListBean>()
    val articleListSuccess = MutableLiveData<Boolean>(true)
    //是否收藏成功
    val collectArticle = MutableLiveData<Boolean>()
    //是否取消收藏成功
    val unCollectArticle = MutableLiveData<Boolean>()



    /**
     *
     * 获取广告数据
     *
     */
    fun getArticleByTag(page:Int,keyword:String) {
        viewModelScope.launch(Dispatchers.IO) {
            mRepo.getArticleByTag(page,keyword)
                .onStart {
                    //开始
                }
                .onEach {
                    articleList.postValue(it)
                }
                .onCompletion {
                    //结束
                }
                .catch {
                    toast(it.message ?: "")
                    articleListSuccess.postValue(false)
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