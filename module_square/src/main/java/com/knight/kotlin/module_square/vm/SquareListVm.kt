package com.knight.kotlin.module_square.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.knight.kotlin.library_base.vm.BaseViewModel
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.module_square.entity.SquareShareArticleListBean
import com.knight.kotlin.module_square.repo.SquareShareListRepo
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
 * Time:2022/4/27 17:05
 * Description:SquareListVm
 */
@HiltViewModel
class SquareListVm @Inject constructor(private val mRepo:SquareShareListRepo) : BaseViewModel() {

    //广场数据
    val squareArticleList = MutableLiveData<SquareShareArticleListBean>()
    //是否收藏成功
    val collectArticleStatus = MutableLiveData<Boolean>()
    //是否取消收藏成功
    val unCollectArticleStatus = MutableLiveData<Boolean>()


    /**
     *
     * 请求广场文章列表
     */
    fun getSquareArticles(page:Int) {
        viewModelScope.launch(Dispatchers.IO) {
            mRepo.getSquareArticles(page)
                .onStart {

                }
                .onEach {
                    squareArticleList .postValue(it) }
                .onCompletion {

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
                    collectArticleStatus.postValue(true)
                }
                .onCompletion {
                    //结束
                }
                .catch {
                    toast(it.message ?: "")
                    collectArticleStatus.postValue(false)
                }
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
                    unCollectArticleStatus.postValue(true)
                }
                .onCompletion {
                    //结束
                }
                .catch {
                    toast(it.message ?: "")
                    unCollectArticleStatus.postValue(false)
                }
                .collect()
        }


    }
}