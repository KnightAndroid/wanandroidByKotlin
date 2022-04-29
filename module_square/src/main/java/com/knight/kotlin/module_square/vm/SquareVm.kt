package com.knight.kotlin.module_square.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.knight.kotlin.library_base.entity.SearchHotKeyEntity
import com.knight.kotlin.library_base.vm.BaseViewModel
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.module_square.entity.SquareArticleListBean
import com.knight.kotlin.module_square.entity.SquareQuestionListBean
import com.knight.kotlin.module_square.repo.SquareRepo
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
 * Description:SquareVm
 */
@HiltViewModel
class SquareVm @Inject constructor(private val mRepo:SquareRepo) : BaseViewModel() {
    //问答列表
    val questionsList = MutableLiveData<SquareQuestionListBean>()
    //热词数据
    val searchHotKeyList = MutableLiveData<MutableList<SearchHotKeyEntity>>()
    //广场数据
    val squareArticleList = MutableLiveData<SquareArticleListBean>()
    //是否收藏成功
    val collectArticle = MutableLiveData<Boolean>()
    //是否取消收藏成功
    val unCollectArticle = MutableLiveData<Boolean>()
    /**
     * 问答列表数据
     */
    fun getQuestion(page:Int) {
        viewModelScope.launch(Dispatchers.IO) {
            mRepo.getQuestions(page)
                .onStart {

                }
                .onEach {
                    questionsList.postValue(it) }
                .onCompletion {

                }
                .catch {
                    toast(it.message ?: "")

                }
                .collect()
        }
    }

    /**
     * 热词
     */
    fun getHotKey() {
        viewModelScope.launch(Dispatchers.IO) {
            mRepo.getHotKey()
                .onStart {

                }
                .onEach {
                    searchHotKeyList.postValue(it) }
                .onCompletion {

                }
                .catch {
                    toast(it.message ?: "")

                }
                .collect()
        }
    }

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