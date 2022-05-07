package com.knight.kotlin.module_navigate.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.knight.kotlin.library_base.vm.BaseViewModel
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.module_navigate.entity.HierachyTabArticleListEntity
import com.knight.kotlin.module_navigate.repo.HierachyArticleRepository
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
 * Time:2022/5/6 16:12
 * Description:HIerachyArticleVm
 */

@HiltViewModel
class HIerachyArticleVm @Inject constructor(private val mRepo: HierachyArticleRepository) : BaseViewModel() {

    //体系文章数据
    val hierachyArticleList = MutableLiveData<HierachyTabArticleListEntity>()

    //是否收藏成功
    val collectArticle = MutableLiveData<Boolean>()
    //是否取消收藏成功
    val unCollectArticle = MutableLiveData<Boolean>()
    //获取体系数据
    fun getHierachyArticle(page:Int,cid:Int) {
        viewModelScope.launch(Dispatchers.IO) {
            mRepo.getHierachyArticle(page,cid)
                .onStart {

                }
                .onEach {
                    hierachyArticleList.postValue(it)
                }
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
     * 收藏本文章
     *
     */
    fun collectArticle(collectArticleId:Int) {
        viewModelScope.launch {
            mRepo.collectArticle(collectArticleId)
                .flowOn(Dispatchers.IO)
                .onStart {
                    //开始
                }
                .onEach {
                    collectArticle.postValue(true)
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