package com.knight.kotlin.module_mine.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.knight.kotlin.library_base.vm.BaseViewModel
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.module_mine.entity.MyShareArticleEntity
import com.knight.kotlin.module_mine.repo.MyShareArticlesRepo
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
 * Time:2022/5/17 10:30
 * Description:MyShareArticlesViewModel
 */
@HiltViewModel
class MyShareArticlesViewModel @Inject constructor(private val mRepo: MyShareArticlesRepo) : BaseViewModel(){

    /**
     * 自己分享文章
     */
    val myShareArticlesList = MutableLiveData<MyShareArticleEntity>()

    /**
     * 删除自己分享的文章
     *
     */
    val deleteMyArticleSuccess = MutableLiveData<Boolean>()

    /**
     * 获取自己分享文章
     */
    fun getMyShareArticles(page:Int) {
        viewModelScope.launch {
            mRepo.getMyShareArticles(page)
                .flowOn(Dispatchers.IO)
                .onStart {
                    //开始
                }
                .onEach {
                    myShareArticlesList.postValue(it)
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
     * 删除自己分享的文章
     */
    fun deleteMyShareArticles(articleId:Int) {
        viewModelScope.launch {
            mRepo.deleteMyShareArticles(articleId)
                .flowOn(Dispatchers.IO)
                .onStart {
                    //开始
                }
                .onEach {
                    deleteMyArticleSuccess.postValue(true)
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