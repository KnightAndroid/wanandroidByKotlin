package com.knight.kotlin.module_square.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.core.library_base.vm.BaseViewModel
import com.knight.kotlin.module_square.entity.SquareArticleListBean
import com.knight.kotlin.module_square.repo.SquareArticleRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/4/11 11:21
 * Description:SquareArticleVm
 */
@HiltViewModel
class SquareArticleVm @Inject constructor(private val mRepo: SquareArticleRepo) : BaseViewModel(){


    /**
     *
     * 根据关键词获取文章列表
     *
     */
    fun getArticleByTag(page:Int,keyword:String,failureCallBack:((String?) ->Unit) ?= null) : LiveData<SquareArticleListBean> {
        return mRepo.getArticleByTag(page, keyword,failureCallBack).asLiveData()
    }

    /**
     * 收藏文章
     *
     */
    fun collectArticle(collectArticleId:Int) : LiveData<Any> {
        return mRepo.collectArticle(collectArticleId).asLiveData()
    }

    /**
     *
     * 取消收藏文章
     */
    fun unCollectArticle(unCollectArticleId:Int): LiveData<Any> {
        return mRepo.unCollectArticle(unCollectArticleId).asLiveData()
    }











}