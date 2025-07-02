package com.knight.kotlin.module_mine.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.core.library_base.vm.BaseViewModel
import com.knight.kotlin.module_mine.entity.MyCollectArticleListEntity
import com.knight.kotlin.module_mine.repo.MyCollectArticleRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/5/13 10:45
 * Description:MyCollectArticleViewModel
 */
@HiltViewModel
class MyCollectArticleViewModel @Inject constructor(private val mRepo: MyCollectArticleRepo) : BaseViewModel()  {


    /**
     * 获取用户金币
     */
    fun getMyCollectArticles(page:Int): LiveData<MyCollectArticleListEntity> {
        return mRepo.getMyCollectArticles(page).asLiveData()
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