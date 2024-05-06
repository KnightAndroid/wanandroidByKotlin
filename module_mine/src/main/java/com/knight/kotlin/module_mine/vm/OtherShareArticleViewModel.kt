package com.knight.kotlin.module_mine.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.knight.kotlin.library_base.vm.BaseViewModel
import com.knight.kotlin.module_mine.entity.OtherShareArticleListEntity
import com.knight.kotlin.module_mine.repo.OtherShareArticleRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/5/7 16:32
 * Description:OtherShareArticleViewModel
 */
@HiltViewModel
class OtherShareArticleViewModel @Inject constructor(private val mRepo: OtherShareArticleRepo) : BaseViewModel(){

    /**
     * 获取他人分享文章
     */
    fun getOtherShareArticle(uid:Int,page:Int): LiveData<OtherShareArticleListEntity> {
        return mRepo.getOtherShareArticle(uid, page).asLiveData()
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