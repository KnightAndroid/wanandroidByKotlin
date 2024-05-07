package com.knight.kotlin.module_navigate.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.knight.kotlin.library_base.vm.BaseViewModel
import com.knight.kotlin.module_navigate.entity.HierachyTabArticleListEntity
import com.knight.kotlin.module_navigate.repo.HierachyArticleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/5/6 16:12
 * Description:HIerachyArticleVm
 */

@HiltViewModel
class HierachyArticleVm @Inject constructor(private val mRepo: HierachyArticleRepository) : BaseViewModel() {
    //获取体系数据
    fun getHierachyArticle(page:Int,cid:Int): LiveData<HierachyTabArticleListEntity> {
        return mRepo.getHierachyArticle(page, cid).asLiveData()
    }

    /**
     *
     * 收藏本文章
     *
     */
    fun collectArticle(collectArticleId:Int):LiveData<Any> {
        return mRepo.collectArticle(collectArticleId).asLiveData()
    }

    /**
     *
     * 取消收藏
     *
     */
    fun unCollectArticle(unCollectArticleId:Int):LiveData<Any> {
        return mRepo.cancelCollectArticle(unCollectArticleId).asLiveData()
    }


}