package com.knight.kotlin.module_square.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.core.library_base.vm.BaseViewModel
import com.knight.kotlin.module_square.entity.SquareShareArticleListBean
import com.knight.kotlin.module_square.repo.SquareShareListRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/4/27 17:05
 * Description:SquareListVm
 */
@HiltViewModel
class SquareListVm @Inject constructor(private val mRepo:SquareShareListRepo) : BaseViewModel() {



    /**
     *
     * 请求广场文章列表
     */
    fun getSquareArticles(page:Int): LiveData<SquareShareArticleListBean> {
        return mRepo.getSquareArticles(page).asLiveData()
    }



    /**
     * 收藏文章
     *
     */
    fun collectArticle(collectArticleId:Int):LiveData<Any> {
        return mRepo.collectArticle(collectArticleId).asLiveData()
    }

    /**
     *
     * 取消收藏文章
     */
    fun unCollectArticle(unCollectArticleId:Int):LiveData<Any> {
        return mRepo.unCollectArticle(unCollectArticleId).asLiveData()
    }
}