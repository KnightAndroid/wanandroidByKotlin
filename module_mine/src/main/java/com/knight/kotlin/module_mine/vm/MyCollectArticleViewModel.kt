package com.knight.kotlin.module_mine.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.knight.kotlin.library_base.vm.BaseViewModel
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.module_mine.entity.MyCollectArticleListEntity
import com.knight.kotlin.module_mine.repo.MyCollectArticleRepo
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
 * Time:2022/5/13 10:45
 * Description:MyCollectArticleViewModel
 */
@HiltViewModel
class MyCollectArticleViewModel @Inject constructor(private val mRepo: MyCollectArticleRepo) : BaseViewModel()  {


    /**
     * 自己收藏文章
     */
    val myCollectArticleLists = MutableLiveData<MyCollectArticleListEntity>()
    //是否取消收藏成功
    val unCollectArticle = MutableLiveData<Boolean>()


    /**
     * 获取用户金币
     */
    fun getMyCollectArticles(page:Int) {
        viewModelScope.launch {
            mRepo.getMyCollectArticles(page)
                .flowOn(Dispatchers.IO)
                .onStart {
                    //开始
                }
                .onEach {
                    myCollectArticleLists.postValue(it)
                }
                .onCompletion {
                    //结束
                }
                .catch {
                    toast(it.message ?: "")
                }
                .collect()
        }
    }

    /**
     *
     * 取消收藏
     *
     */
    fun unCollectArticle(unCollectArticleId:Int) {
        viewModelScope.launch {
            mRepo.cancelCollectArticle(unCollectArticleId)
                .flowOn(Dispatchers.IO)
                .onStart {
                    //开始
                }
                .onEach {
                    unCollectArticle.postValue(true)
                }
                .onCompletion {
                    //结束
                }
                .catch {
                    toast(it.message ?: "")
                }
                .collect()
        }
    }


}